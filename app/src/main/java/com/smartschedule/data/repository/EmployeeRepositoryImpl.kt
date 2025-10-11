package com.smartschedule.data.repository

import com.smartschedule.data.local.dao.EmployeeDao
import com.smartschedule.data.mappers.toDomain
import com.smartschedule.data.mappers.toEntity
import com.smartschedule.domain.models.Employee
import com.smartschedule.domain.repositories.EmployeeRepository

class EmployeeRepositoryImpl(
    private val employeeDao: EmployeeDao
) : EmployeeRepository {
    override suspend fun getAllEmployees(): List<Employee> {
        return employeeDao.getAllEmployees().map { it.toDomain() }
    }

    override suspend fun getEmployeeById(id: Long): Employee? {
        return employeeDao.getEmployeeById(id)?.toDomain()
    }

    override suspend fun insertEmployee(employee: Employee) {
        employeeDao.insertEmployee(employee.toEntity())
    }

    override suspend fun updateEmployee(employee: Employee) {
        employeeDao.updateEmployee(employee.toEntity())
    }

    override suspend fun deleteEmployee(employee: Employee) {
        employeeDao.deleteEmployee(employee.toEntity())
    }
}
