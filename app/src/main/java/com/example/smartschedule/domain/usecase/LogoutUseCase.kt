package com.example.smartschedule.domain.usecase

import com.example.smartschedule.domain.common.AuthenticationResult
import com.example.smartschedule.domain.repository.UserRepository

class LogoutUseCase(
    private val userRepository: UserRepository
) {

    suspend fun execute(): AuthenticationResult {
        return try {
            userRepository.logout()
            AuthenticationResult.Success(null)
        } catch (e: Exception) {
            AuthenticationResult.Error("שגיאה ביציאה: ${e.message}")
        }
    }
}