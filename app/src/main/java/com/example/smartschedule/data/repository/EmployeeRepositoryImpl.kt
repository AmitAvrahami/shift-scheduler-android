package com.example.smartschedule.data.repository

import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteException
import com.example.smartschedule.data.database.dao.EmployeeDao
import com.example.smartschedule.data.mappers.toDomain
import com.example.smartschedule.data.mappers.toEntity
import com.example.smartschedule.domain.models.Employee
import com.example.smartschedule.domain.repository.EmployeeRepository
import com.example.smartschedule.domain.common.Result
import com.example.smartschedule.domain.common.safeDbOperation
import com.example.smartschedule.domain.errors.employee_error.EmployeeError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

class EmployeeRepositoryImpl @Inject constructor(
    private val employeeDao: EmployeeDao
) : EmployeeRepository {

    // ✅ Flow that emits Result<List<Employee>>
    override fun getAllEmployees(): Flow<Result<List<Employee>>> = flow {
        try {
            employeeDao.getAllEmployees()
                .map { entities -> entities.map { it.toDomain() } }
                .catch { error ->
                    emit(Result.Error(handleDatabaseError(error)))
                }
                .collect { employees ->
                    emit(Result.Success(employees))
                }
        } catch (e: Exception) {
            emit(Result.Error(handleDatabaseError(e)))
        }
    }

    // ✅ Single employee by ID
    override suspend fun getEmployeeById(id: String): Result<Employee> = withContext(Dispatchers.IO) {
        safeDbOperation {
            val entity = employeeDao.getEmployeeById(id)
                ?: throw EmployeeError.EmployeeNotFound(id)
            entity.toDomain()
        }
    }

    // ✅ Insert with validation
    override suspend fun insertEmployee(employee: Employee): Result<Employee> = withContext(Dispatchers.IO) {
        // Step 3: Insert to database
        safeDbOperation {
            employeeDao.insertEmployee(employee.toEntity())
            employee // Return the original employee
        }
    }

    // ✅ Update employee
    override suspend fun updateEmployee(employee: Employee): Result<Employee> = withContext(Dispatchers.IO) {
        safeDbOperation {
            val entity = employee.toEntity()
            employeeDao.insertEmployee(entity) // Room REPLACE strategy
            employee
        }
    }

    // ✅ Delete employee
    override suspend fun deleteEmployee(employee: Employee): Result<Boolean> = withContext(Dispatchers.IO) {
        safeDbOperation {
            employeeDao.deleteEmployee(employee.toEntity())
            true
        }
    }

    // ✅ Check if employee number exists
    override suspend fun isEmployeeNumberExists(employeeNumber: String): Result<Boolean> = withContext(Dispatchers.IO) {
        safeDbOperation {
            employeeDao.getEmployeeByEmployeeNumber(employeeNumber) != null
        }
    }

    // ✅ Refresh employees (simulates network call)
    override suspend fun refreshEmployees(): Result<List<Employee>> = withContext(Dispatchers.IO) {
        safeDbOperation {
            // Simulate network delay
            delay(Random.nextLong(300, 1000))

            // In real app: fetch from network, update local DB, return updated data
            employeeDao.getAllEmployees().first().map { it.toDomain() }
        }
    }

    private fun handleDatabaseError(throwable: Throwable): EmployeeError {
        return when (throwable) {
            is EmployeeError -> throwable
            is SQLiteException -> EmployeeError.DatabaseCorrupted
            is SQLiteDatabaseCorruptException -> EmployeeError.DatabaseCorrupted
            is IllegalStateException -> EmployeeError.DatabaseError(throwable)
            else -> EmployeeError.DatabaseError(throwable)
        }
    }
}