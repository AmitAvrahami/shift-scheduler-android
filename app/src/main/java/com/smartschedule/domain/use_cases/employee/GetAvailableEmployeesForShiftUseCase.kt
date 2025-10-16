package com.smartschedule.domain.use_cases.employee



import com.smartschedule.domain.models.Employee
import com.smartschedule.domain.models.Shift
import com.smartschedule.domain.repositories.ConstraintRepository
import com.smartschedule.domain.repositories.EmployeeRepository
import com.smartschedule.domain.use_cases.common.safeListCall
import com.smartschedule.utils.Resource
import java.time.LocalDate
import javax.inject.Inject

class GetAvailableEmployeesForShiftUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val constraintRepository: ConstraintRepository
) {
    suspend operator fun invoke(shift: Shift): Resource<List<Employee>> = safeListCall(
        validate = {
            require(shift.date >= LocalDate.now().minusDays(1)) { "תאריך המשמרת לא תקין" }
            require(shift.requiredHeadcount > 0) { "מספר העובדים הנדרש במשמרת חייב להיות גדול מ־0" }
        },
        block = {
            val allEmployees = employeeRepository.getAllEmployees().filter { it.isActive }

            val constraints = constraintRepository.getAllConstraints()

            val unavailableEmployees = constraints
                .filter { c ->
                    c.dateStart <= shift.date && c.dateEnd >= shift.date &&
                            (c.shiftType == null || c.shiftType == shift.shiftType)
                }
                .map { it.employeeId }

            allEmployees.filterNot { unavailableEmployees.contains(it.id) }
        }
    )
}
