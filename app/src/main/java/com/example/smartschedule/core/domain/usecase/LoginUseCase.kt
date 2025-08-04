// File: app/src/main/java/com/example/smartschedule/domain/usecase/LoginUseCase.kt

package com.example.smartschedule.core.domain.usecase

import com.example.smartschedule.core.domain.models.User

class LoginUseCase(
    private val userRepository: com.example.smartschedule.core.domain.repository.UserRepository
) {

    // 🆕 NEW - Result<T> based implementation
    suspend fun executeWithResult(email: String, password: String): com.example.smartschedule.core.domain.common.Result<User> {
        // Input validation
        if (email.isBlank()) {
            return _root_ide_package_.com.example.smartschedule.core.domain.common.Result.Error(IllegalArgumentException("כתובת אימייל נדרשת"))
        }
        if (password.isBlank()) {
            return _root_ide_package_.com.example.smartschedule.core.domain.common.Result.Error(IllegalArgumentException("סיסמה נדרשת"))
        }

        // Delegate to repository (which handles all error cases)
        return userRepository.loginWithResult(email, password)
    }
}