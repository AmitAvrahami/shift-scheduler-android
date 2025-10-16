package com.smartschedule.domain.use_cases.employee

import com.smartschedule.domain.models.Employee
import com.smartschedule.domain.repositories.EmployeeRepository
import com.smartschedule.domain.use_cases.common.safeCall
import com.smartschedule.utils.Resource
import javax.inject.Inject


class AddEmployeeUseCase @Inject constructor(
    private val repository: EmployeeRepository
) {
    suspend operator fun invoke(employee: Employee): Resource<Unit> = safeCall(
        validate = {
            require(employee.name.isNotBlank()) { "שם העובד לא יכול להיות ריק" }
            require(employee.maxShiftsPerWeek in 1..7) { "מספר המשמרות לשבוע חייב להיות בין 1 ל־7" }
            requireNotNull(employee.userRole) { "תפקיד העובד חייב להיות מוגדר" }
        },
        block = {
            repository.insertEmployee(employee)
        }
    )
}
