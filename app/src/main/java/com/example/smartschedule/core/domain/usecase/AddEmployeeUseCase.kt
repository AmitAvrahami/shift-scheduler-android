// File: app/src/main/java/com/example/smartschedule/domain/usecase/AddEmployeeUseCase.kt

package com.example.smartschedule.core.domain.usecase

import javax.inject.Inject

class AddEmployeeUseCase @Inject constructor(
    private val employeeRepository: com.example.smartschedule.core.domain.repository.EmployeeRepository,
    private val userRepository: com.example.smartschedule.core.domain.repository.UserRepository
) {

    // ✅ EXISTING - Keep for backwards compatibility (temporary)
    suspend operator fun invoke(employee: com.example.smartschedule.core.domain.models.Employee, password: String): Boolean {
        val userRegisteredSuccessfully = userRepository.registerUser(employee, password)
        if (userRegisteredSuccessfully) {
            employeeRepository.insertEmployee(employee)
            return true
        }
        return false
    }

    // 🆕 NEW - Result<T> based implementation
    suspend fun executeWithResult(employee: com.example.smartschedule.core.domain.models.Employee, password: String): com.example.smartschedule.core.domain.common.Result<com.example.smartschedule.core.domain.models.Employee> {
        // Step 1: Register user (which includes validation and password hashing)
        val userResult = userRepository.registerUserWithResult(employee, password)

        return when (userResult) {
            is com.example.smartschedule.core.domain.common.Result.Success -> {
                // Step 2: Insert employee (using the Result pattern)
                employeeRepository.insertEmployee(employee)
                // Note: employeeRepository.insertEmployee already returns Result<Employee>
            }
            is com.example.smartschedule.core.domain.common.Result.Error -> {
                // User registration failed - return the error
                _root_ide_package_.com.example.smartschedule.core.domain.common.Result.Error(userResult.exception)
            }
        }
    }
}