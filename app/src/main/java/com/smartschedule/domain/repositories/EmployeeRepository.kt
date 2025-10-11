package com.smartschedule.domain.repositories

import com.smartschedule.domain.models.Employee

interface EmployeeRepository {
    suspend fun getAllEmployees(): List<Employee>
    suspend fun getEmployeeById(id: Long): Employee?
    suspend fun insertEmployee(employee: Employee)
    suspend fun updateEmployee(employee: Employee)
    suspend fun deleteEmployee(employee: Employee)
}
