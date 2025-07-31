
package com.example.smartschedule.domain.usecase

import com.example.smartschedule.domain.common.Result
import com.example.smartschedule.domain.models.User
import com.example.smartschedule.domain.models.UserType
import com.example.smartschedule.domain.repository.UserRepository
import com.example.smartschedule.domain.validation.EmployeeValidation
import com.example.smartschedule.domain.validation.UserValidation
import com.example.smartschedule.domain.validation.ValidationResult
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    // 🆕 NEW - Result<T> based user registration
        suspend fun executeWithResult(
            currentUser: User,
            newUser: User,
            password: String
        ): Result<User> {

        try {// 🔐 STEP 1: בדיקת הרשאות לפני הכל
            val authorizationResult = validateUserCreationPermissions(currentUser, newUser)
            if (authorizationResult != null) {
                return Result.Error(IllegalArgumentException(authorizationResult))
            }

            // 🔍 STEP 2: validation רגיל של נתונים (כמו שהיה קודם)
            val validationErrors = validateInput(newUser, password)
            if (validationErrors.isNotEmpty()) {
                val errorMessage = validationErrors.joinToString(", ")
                return Result.Error(IllegalArgumentException(errorMessage))
            }

            // ✅ STEP 3: validation פורמטים (כמו שהיה קודם)
            when (val emailValidation = UserValidation.validateEmail(newUser.email)) {
                is ValidationResult.Error -> {
                    return Result.Error(IllegalArgumentException(emailValidation.message))
                }
                ValidationResult.Success -> { /* Continue */ }
            }

            when (val passwordValidation = UserValidation.validatePassword(password)) {
                is ValidationResult.Error -> {
                    return Result.Error(IllegalArgumentException(passwordValidation.message))
                }
                ValidationResult.Success -> { /* Continue */ }
            }

            // 🚀 STEP 4: אם הכל עבר - יוצרים את המשתמש
            return userRepository.registerUserWithResult(newUser, password)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

        // 🔐 פונקציה חדשה שבודקת הרשאות
        private fun validateUserCreationPermissions(currentUser: User, newUser: User): String? {
            return when (newUser.type) {
                UserType.EMPLOYEE -> {
                    // כל אחד יכול ליצור EMPLOYEE
                    null
                }
                UserType.MANAGER -> {
                    // רק ADMIN יכול ליצור MANAGER
                    if (currentUser.type != UserType.ADMIN) {
                        "רק מנהל מערכת יכול ליצור משתמש מנהל"
                    } else null
                }
                UserType.ADMIN -> {
                    // רק ADMIN יכול ליצור ADMIN אחר
                    if (currentUser.type != UserType.ADMIN) {
                        "רק מנהל מערכת יכול ליצור מנהל מערכת אחר"
                    } else null
                }
            }
        }

    private fun validateInput(user: User, password: String): List<String> {
        val errors = mutableListOf<String>()

        val nameValidationResult = UserValidation.validateName(user.name)
        if (nameValidationResult is ValidationResult.Error) {
            errors.add(nameValidationResult.message)
        }

        when (val emailValidation = UserValidation.validateEmail(user.email)) {
            is ValidationResult.Error -> {
                errors.add(emailValidation.message)
            }
            ValidationResult.Success -> { /* Continue */ }
        }

        when (val passwordValidation = UserValidation.validatePassword(password)) {
            is ValidationResult.Error -> {
                errors.add(passwordValidation.message)
            }
            ValidationResult.Success -> { /* Continue */ }
        }

        // Admin users can only be created by other admins (future business rule)
        if (user.type == UserType.ADMIN) {
            // TODO: Add authorization check in future versions
        }

        return errors
    }


}