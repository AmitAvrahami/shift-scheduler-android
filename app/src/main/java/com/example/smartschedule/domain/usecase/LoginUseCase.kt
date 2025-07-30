// File: app/src/main/java/com/example/smartschedule/domain/usecase/LoginUseCase.kt

package com.example.smartschedule.domain.usecase

import com.example.smartschedule.domain.common.Result
import com.example.smartschedule.domain.models.User
import com.example.smartschedule.domain.repository.UserRepository

class LoginUseCase(
    private val userRepository: UserRepository
) {

    // 🆕 NEW - Result<T> based implementation
    suspend fun executeWithResult(email: String, password: String): Result<User> {
        // Input validation
        if (email.isBlank()) {
            return Result.Error(IllegalArgumentException("כתובת אימייל נדרשת"))
        }
        if (password.isBlank()) {
            return Result.Error(IllegalArgumentException("סיסמה נדרשת"))
        }

        // Delegate to repository (which handles all error cases)
        return userRepository.loginWithResult(email, password)
    }
}