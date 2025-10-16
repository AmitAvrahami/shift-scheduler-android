package com.smartschedule.domain.use_cases.employee

import com.smartschedule.domain.models.Employee
import com.smartschedule.domain.repositories.EmployeeRepository
import com.smartschedule.domain.use_cases.common.safeListCall
import com.smartschedule.utils.Resource
import javax.inject.Inject

class GetAllEmployeesUseCase @Inject constructor(
    private val repository: EmployeeRepository
) {
    suspend operator fun invoke(): Resource<List<Employee>> = safeListCall(
        validate = null,
        block = { repository.getAllEmployees() }
    )
}