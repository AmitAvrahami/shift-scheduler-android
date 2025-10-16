package com.smartschedule.domain.use_cases.schedule_context_use_case

import com.smartschedule.domain.models.Employee
import com.smartschedule.domain.models.EmployeeContext
import com.smartschedule.domain.models.Shift
import com.smartschedule.domain.repositories.*
import java.time.LocalDate


class BuildEmployeeContextUseCase(
    private val employeeRepo: EmployeeRepository,
    private val constraintRepo: ConstraintRepository,
    private val recurringRepo: RecurringConstraintRepository,
    private val assignmentRepo: ShiftAssignmentRepository,
    private val shiftRepo: ShiftRepository
) {


    suspend fun buildForWeek(weekStart: LocalDate, weekEnd: LocalDate): List<EmployeeContext> {
        val employees = employeeRepo.getAllEmployees().filter { it.isActive }
        val shifts = shiftRepo.getShiftsBetween(weekStart, weekEnd)

        return employees.map { buildForEmployee(it, weekStart, weekEnd, shifts) }
    }

    private suspend fun buildForEmployee(
        employee: Employee,
        weekStart: LocalDate,
        weekEnd: LocalDate,
        shifts: List<Shift>
    ): EmployeeContext {
        val constraints = constraintRepo.getForEmployee(employee.id!!, weekStart, weekEnd)
        val recurringConstraints = recurringRepo.getForEmployee(employee.id)
        val assignedShifts = assignmentRepo.getForEmployee(employee.id, weekStart, weekEnd)

        val availableShifts = shifts.filter { shift ->
            shift.date in weekStart..weekEnd &&
                    !constraints.any { c ->
                        shift.date in c.dateStart..c.dateEnd &&
                                (c.shiftType == null || c.shiftType == shift.shiftType)
                    } &&
                    !recurringConstraints.any { rc ->
                        rc.dayOfWeek == shift.date.dayOfWeek &&
                                rc.shiftTypes.contains(shift.shiftType)
                    }
        }

        return EmployeeContext(
            employee = employee,
            constraints = constraints,
            recurringConstraints = recurringConstraints,
            assignedShifts = assignedShifts,
            availableShifts = availableShifts
        )
    }
}
