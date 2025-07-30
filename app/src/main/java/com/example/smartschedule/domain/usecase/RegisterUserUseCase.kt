
package com.example.smartschedule.domain.usecase

import com.example.smartschedule.domain.common.Result
import com.example.smartschedule.domain.models.User
import com.example.smartschedule.domain.models.UserType
import com.example.smartschedule.domain.repository.UserRepository
import com.example.smartschedule.domain.validation.EmployeeValidation
import com.example.smartschedule.domain.validation.ValidationResult
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    // 🆕 NEW - Result<T> based user registration
    suspend fun executeWithResult(user: User, password: String): Result<User> {
        // Input validation
        val validationErrors = validateInput(user, password)
        if (validationErrors.isNotEmpty()) {
            val errorMessage = validationErrors.joinToString(", ")
            return Result.Error(IllegalArgumentException(errorMessage))
        }

        // Business rule validation
        when (val emailValidation = validateEmailFormat(user.email)) {
            is ValidationResult.Error -> {
                return Result.Error(IllegalArgumentException(emailValidation.message))
            }
            ValidationResult.Success -> { /* Continue */ }
        }

        when (val passwordValidation = validatePassword(password)) {
            is ValidationResult.Error -> {
                return Result.Error(IllegalArgumentException(passwordValidation.message))
            }
            ValidationResult.Success -> { /* Continue */ }
        }

        // Delegate to repository for actual registration
        return userRepository.registerUserWithResult(user, password)
    }

    private fun validateInput(user: User, password: String): List<String> {
        val errors = mutableListOf<String>()

        if (user.name.isBlank()) {
            errors.add("שם נדרש")
        }

        if (user.email.isBlank()) {
            errors.add("כתובת אימייל נדרשת")
        }

        if (password.isBlank()) {
            errors.add("סיסמה נדרשת")
        }

        // Admin users can only be created by other admins (future business rule)
        if (user.type == UserType.ADMIN) {
            // TODO: Add authorization check in future versions
        }

        return errors
    }

    private fun validateEmailFormat(email: String): ValidationResult {
        return EmployeeValidation.validateEmail(email)
    }

    private fun validatePassword(password: String): ValidationResult {
        return EmployeeValidation.validatePassword(password)
    }
}