// File: app/src/main/java/com/example/smartschedule/domain/usecase/AddEmployeeUseCase.kt

package com.example.smartschedule.domain.usecase

import com.example.smartschedule.domain.models.Employee
import com.example.smartschedule.domain.repository.EmployeeRepository
import com.example.smartschedule.domain.repository.UserRepository
import com.example.smartschedule.domain.common.Result
import com.example.smartschedule.domain.common.fold
import javax.inject.Inject

class AddEmployeeUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val userRepository: UserRepository
) {

    // ✅ EXISTING - Keep for backwards compatibility (temporary)
    suspend operator fun invoke(employee: Employee, password: String): Boolean {
        val userRegisteredSuccessfully = userRepository.registerUser(employee, password)
        if (userRegisteredSuccessfully) {
            employeeRepository.insertEmployee(employee)
            return true
        }
        return false
    }

    // 🆕 NEW - Result<T> based implementation
    suspend fun executeWithResult(employee: Employee, password: String): Result<Employee> {
        // Step 1: Register user (which includes validation and password hashing)
        val userResult = userRepository.registerUserWithResult(employee, password)

        return when (userResult) {
            is Result.Success -> {
                // Step 2: Insert employee (using the Result pattern)
                employeeRepository.insertEmployee(employee)
                // Note: employeeRepository.insertEmployee already returns Result<Employee>
            }
            is Result.Error -> {
                // User registration failed - return the error
                Result.Error(userResult.exception)
            }
        }
    }
}