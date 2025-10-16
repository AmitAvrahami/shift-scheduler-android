package com.smartschedule.domain.use_cases.schedule_engine

import com.smartschedule.domain.models.Employee
import com.smartschedule.domain.models.EmployeeContext
import com.smartschedule.domain.repositories.ConstraintRepository
import com.smartschedule.domain.repositories.RecurringConstraintRepository
import com.smartschedule.domain.repositories.ShiftAssignmentRepository
import com.smartschedule.domain.repositories.ShiftRepository
import com.smartschedule.domain.use_cases.common.safeListCall
import com.smartschedule.utils.Resource
import java.time.LocalDate
import javax.inject.Inject

class BuildEmployeeContextUseCase @Inject constructor(
    private val constraintRepository: ConstraintRepository,
    private val recurringConstraintRepository: RecurringConstraintRepository,
    private val assignmentRepository: ShiftAssignmentRepository,
    private val shiftRepository: ShiftRepository,
) {
    suspend operator fun invoke(
        employees: List<Employee>,
        weekStart: LocalDate,
        weekEnd: LocalDate
    ): Resource<List<EmployeeContext>> = safeListCall(
        validate = {
            require(employees.isNotEmpty()) { "רשימת העובדים ריקה" }
            require(!weekEnd.isBefore(weekStart)) { "תאריכי השבוע לא תקינים" }
        },
        block = {
            employees.map { employee ->
                val constraints = constraintRepository.getForEmployee(employee.id!!, weekStart, weekEnd)
                val recurringConstraints = recurringConstraintRepository.getForEmployee(employee.id)
                val assignedShifts = assignmentRepository.getForEmployee(employee.id, weekStart, weekEnd)
                val availableShifts = shiftRepository.getShiftsBetween(weekStart, weekEnd)
                    .filter { shift ->
                        constraints.none { c ->
                            shift.date in c.dateStart..c.dateEnd &&
                                    (c.shiftType == null || c.shiftType == shift.shiftType)
                        }
                    }

                EmployeeContext(
                    employee = employee,
                    constraints = constraints,
                    recurringConstraints = recurringConstraints,
                    assignedShifts = assignedShifts,
                    availableShifts = availableShifts
                )
            }
        }
    )
}
