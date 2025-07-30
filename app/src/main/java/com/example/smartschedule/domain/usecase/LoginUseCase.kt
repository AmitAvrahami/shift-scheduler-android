// File: app/src/main/java/com/example/smartschedule/domain/usecase/LoginUseCase.kt

package com.example.smartschedule.domain.usecase

import com.example.smartschedule.domain.common.AuthenticationResult
import com.example.smartschedule.domain.common.Result
import com.example.smartschedule.domain.models.User
import com.example.smartschedule.domain.repository.UserRepository

class LoginUseCase(
    private val userRepository: UserRepository
) {

    // ✅ EXISTING - Keep for backwards compatibility (temporary)
    suspend fun execute(username: String, password: String): AuthenticationResult {
        return try {
            val user = userRepository.login(username, password)
            if (user != null) {
                AuthenticationResult.Success(user)
            } else {
                AuthenticationResult.Error("שם משתמש או סיסמה שגויים")
            }
        } catch (e: Exception) {
            AuthenticationResult.Error("שגיאה בהתחברות: ${e.message}")
        }
    }

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