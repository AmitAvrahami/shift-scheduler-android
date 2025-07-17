package com.example.smartschedule.data.repository

import com.example.smartschedule.data.database.dao.EmployeeDao
import com.example.smartschedule.data.mappers.toDomain
import com.example.smartschedule.data.mappers.toEntity
import com.example.smartschedule.domain.models.Employee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EmployeeRepositoryImpl(
    private val employeeDao: EmployeeDao
) : EmployeeRepository {

    override fun getAllEmployees(): Flow<List<Employee>> {
        return employeeDao.getAllEmployees().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertEmployee(employee: Employee) {
        employeeDao.insertEmployee(employee.toEntity())
    }

    override suspend fun deleteEmployee(employee: Employee) {
        employeeDao.deleteEmployee(employee.toEntity())
    }

    override suspend fun getEmployeeById(id: String): Employee? {
        return employeeDao.getEmployeeById(id)?.toDomain()
    }

    override suspend fun isEmployeeNumberExists(employeeNumber: String): Boolean {
        return employeeDao.getEmployeeByEmployeeNumber(employeeNumber) != null
    }
}