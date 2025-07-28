package com.example.smartschedule.domain.repository

import com.example.smartschedule.domain.models.Employee
import kotlinx.coroutines.flow.Flow
import com.example.smartschedule.domain.common.Result

interface EmployeeRepository {  // Flow operations return Result inside the Flow
    fun getAllEmployees(): Flow<Result<List<Employee>>>

    // Single operations return Result directly
    suspend fun getEmployeeById(id: String): Result<Employee>
    suspend fun insertEmployee(employee: Employee): Result<Employee>
    suspend fun updateEmployee(employee: Employee): Result<Employee>
    suspend fun deleteEmployee(employee: Employee): Result<Boolean>
    suspend fun isEmployeeNumberExists(employeeNumber: String): Result<Boolean>

    // Refresh operation
    suspend fun refreshEmployees(): Result<List<Employee>>
}