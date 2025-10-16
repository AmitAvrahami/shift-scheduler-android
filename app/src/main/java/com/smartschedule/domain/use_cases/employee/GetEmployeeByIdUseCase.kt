package com.smartschedule.domain.use_cases.employee

import com.smartschedule.domain.models.Employee
import com.smartschedule.domain.repositories.EmployeeRepository
import com.smartschedule.domain.use_cases.common.safeCall
import com.smartschedule.utils.Resource
import javax.inject.Inject

class GetEmployeeByIdUseCase @Inject constructor(
    private val repository: EmployeeRepository
) {
    suspend operator fun invoke(id: Long): Resource<Employee> = safeCall(
        validate = {
            require(id > 0) { "מזהה העובד חייב להיות גדול מ־0" }
        },
        block = {
            val employee = repository.getEmployeeById(id)
            requireNotNull(employee) { "לא נמצא עובד עם מזהה $id" }
            employee
        }
    )
}
