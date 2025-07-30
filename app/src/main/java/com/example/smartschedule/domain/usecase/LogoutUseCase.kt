// File: app/src/main/java/com/example/smartschedule/domain/usecase/LogoutUseCase.kt

package com.example.smartschedule.domain.usecase

import com.example.smartschedule.domain.common.AuthenticationResult
import com.example.smartschedule.domain.common.Result
import com.example.smartschedule.domain.repository.UserRepository

class LogoutUseCase(
    private val userRepository: UserRepository
) {

    // ✅ EXISTING - Keep for backwards compatibility (temporary)
    suspend fun execute(): AuthenticationResult {
        return try {
            userRepository.logout()
            AuthenticationResult.Success(null)
        } catch (e: Exception) {
            AuthenticationResult.Error("שגיאה ביציאה: ${e.message}")
        }
    }

    // 🆕 NEW - Result<T> based implementation
    suspend fun executeWithResult(): Result<Unit> {
        // Simple delegation to repository
        return userRepository.logoutWithResult()
    }
}