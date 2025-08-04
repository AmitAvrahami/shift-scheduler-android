package com.example.smartschedule.core.domain.usecase

import com.example.smartschedule.core.domain.models.User
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: com.example.smartschedule.core.domain.repository.UserRepository
) {

    // 🆕 NEW - Result<T> based user registration
        suspend fun executeWithResult(
            currentUser: User,
            newUser: User,
            password: String
        ): com.example.smartschedule.core.domain.common.Result<User> {

        try {// 🔐 STEP 1: בדיקת הרשאות לפני הכל
            val authorizationResult = validateUserCreationPermissions(currentUser, newUser)
            if (authorizationResult != null) {
                return _root_ide_package_.com.example.smartschedule.core.domain.common.Result.Error(IllegalArgumentException(authorizationResult))
            }

            // 🔍 STEP 2: validation רגיל של נתונים (כמו שהיה קודם)
            val validationErrors = validateInput(newUser, password)
            if (validationErrors.isNotEmpty()) {
                val errorMessage = validationErrors.joinToString(", ")
                return _root_ide_package_.com.example.smartschedule.core.domain.common.Result.Error(IllegalArgumentException(errorMessage))
            }

            // ✅ STEP 3: validation פורמטים (כמו שהיה קודם)
            when (val emailValidation = _root_ide_package_.com.example.smartschedule.core.domain.validation.UserValidation.validateEmail(newUser.email)) {
                is com.example.smartschedule.core.domain.validation.ValidationResult.Error -> {
                    return _root_ide_package_.com.example.smartschedule.core.domain.common.Result.Error(IllegalArgumentException(emailValidation.message))
                }
                _root_ide_package_.com.example.smartschedule.core.domain.validation.ValidationResult.Success -> { /* Continue */ }
            }

            when (val passwordValidation = _root_ide_package_.com.example.smartschedule.core.domain.validation.UserValidation.validatePassword(password)) {
                is com.example.smartschedule.core.domain.validation.ValidationResult.Error -> {
                    return _root_ide_package_.com.example.smartschedule.core.domain.common.Result.Error(IllegalArgumentException(passwordValidation.message))
                }
                _root_ide_package_.com.example.smartschedule.core.domain.validation.ValidationResult.Success -> { /* Continue */ }
            }

            // 🚀 STEP 4: אם הכל עבר - יוצרים את המשתמש
            return userRepository.registerUserWithResult(newUser, password)
        } catch (e: Exception) {
            return _root_ide_package_.com.example.smartschedule.core.domain.common.Result.Error(e)
        }
    }

        // 🔐 פונקציה חדשה שבודקת הרשאות
        private fun validateUserCreationPermissions(currentUser: User, newUser: User): String? {
            return when (newUser.type) {
                _root_ide_package_.com.example.smartschedule.core.domain.models.UserType.EMPLOYEE -> {
                    // כל אחד יכול ליצור EMPLOYEE
                    null
                }
                _root_ide_package_.com.example.smartschedule.core.domain.models.UserType.MANAGER -> {
                    // רק ADMIN יכול ליצור MANAGER
                    if (currentUser.type != _root_ide_package_.com.example.smartschedule.core.domain.models.UserType.ADMIN) {
                        "רק מנהל מערכת יכול ליצור משתמש מנהל"
                    } else null
                }
                _root_ide_package_.com.example.smartschedule.core.domain.models.UserType.ADMIN -> {
                    // רק ADMIN יכול ליצור ADMIN אחר
                    if (currentUser.type != _root_ide_package_.com.example.smartschedule.core.domain.models.UserType.ADMIN) {
                        "רק מנהל מערכת יכול ליצור מנהל מערכת אחר"
                    } else null
                }
            }
        }

    private fun validateInput(user: User, password: String): List<String> {
        val errors = mutableListOf<String>()

        val nameValidationResult = _root_ide_package_.com.example.smartschedule.core.domain.validation.UserValidation.validateName(user.name)
        if (nameValidationResult is com.example.smartschedule.core.domain.validation.ValidationResult.Error) {
            errors.add(nameValidationResult.message)
        }

        when (val emailValidation = _root_ide_package_.com.example.smartschedule.core.domain.validation.UserValidation.validateEmail(user.email)) {
            is com.example.smartschedule.core.domain.validation.ValidationResult.Error -> {
                errors.add(emailValidation.message)
            }
            _root_ide_package_.com.example.smartschedule.core.domain.validation.ValidationResult.Success -> { /* Continue */ }
        }

        when (val passwordValidation = _root_ide_package_.com.example.smartschedule.core.domain.validation.UserValidation.validatePassword(password)) {
            is com.example.smartschedule.core.domain.validation.ValidationResult.Error -> {
                errors.add(passwordValidation.message)
            }
            _root_ide_package_.com.example.smartschedule.core.domain.validation.ValidationResult.Success -> { /* Continue */ }
        }

        // Admin users can only be created by other admins (future business rule)
        if (user.type == _root_ide_package_.com.example.smartschedule.core.domain.models.UserType.ADMIN) {
            // TODO: Add authorization check in future versions
        }

        return errors
    }


}