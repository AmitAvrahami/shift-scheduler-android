package com.example.smartschedule.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface EmployeeRepository {  // Flow operations return Result inside the Flow
    fun getAllEmployees(): Flow<com.example.smartschedule.core.domain.common.Result<List<com.example.smartschedule.core.domain.models.Employee>>>

    // Single operations return Result directly
    suspend fun getEmployeeById(id: String): com.example.smartschedule.core.domain.common.Result<com.example.smartschedule.core.domain.models.Employee>
    suspend fun insertEmployee(employee: com.example.smartschedule.core.domain.models.Employee): com.example.smartschedule.core.domain.common.Result<com.example.smartschedule.core.domain.models.Employee>
    suspend fun updateEmployee(employee: com.example.smartschedule.core.domain.models.Employee): com.example.smartschedule.core.domain.common.Result<com.example.smartschedule.core.domain.models.Employee>
    suspend fun deleteEmployee(employee: com.example.smartschedule.core.domain.models.Employee): com.example.smartschedule.core.domain.common.Result<Boolean>
    suspend fun isEmployeeNumberExists(employeeNumber: String): com.example.smartschedule.core.domain.common.Result<Boolean>

    // Refresh operation
    suspend fun refreshEmployees(): com.example.smartschedule.core.domain.common.Result<List<com.example.smartschedule.core.domain.models.Employee>>
}