package com.example.smartschedule.domain.repository

import com.example.smartschedule.domain.models.Employee
import kotlinx.coroutines.flow.Flow

interface EmployeeRepository {
    fun getAllEmployees(): Flow<List<Employee>>
    suspend fun insertEmployee(employee: Employee)
    suspend fun deleteEmployee(employee: Employee)
    suspend fun getEmployeeById(id: String): Employee?
    suspend fun isEmployeeNumberExists(employeeNumber: String): Boolean
}