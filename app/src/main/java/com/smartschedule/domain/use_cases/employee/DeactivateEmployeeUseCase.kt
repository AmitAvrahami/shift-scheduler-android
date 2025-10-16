package com.smartschedule.domain.use_cases.employee

import com.smartschedule.domain.models.Employee
import com.smartschedule.domain.repositories.EmployeeRepository
import com.smartschedule.domain.use_cases.common.safeCall
import com.smartschedule.utils.Resource
import javax.inject.Inject

class DeactivateEmployeeUseCase @Inject constructor(
    private val repository: EmployeeRepository
) {
    suspend operator fun invoke(employee: Employee): Resource<Unit> = safeCall(
        validate = {
            employee.id?.let { require(it > 0) { "מזהה עובד לא חוקי" } }
            require(employee.isActive) { "העובד כבר לא פעיל" }
        },
        block = {
            val deactivatedEmployee = employee.copy(isActive = false)
            repository.updateEmployee(deactivatedEmployee)
        }
    )
}