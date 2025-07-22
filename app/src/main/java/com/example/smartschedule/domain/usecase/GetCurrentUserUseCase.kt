package com.example.smartschedule.domain.usecase

import com.example.smartschedule.domain.common.AuthenticationResult
import com.example.smartschedule.domain.repository.UserRepository

class GetCurrentUserUseCase(
    private val userRepository: UserRepository
) {

    suspend fun execute(): AuthenticationResult {
        return try {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                AuthenticationResult.Success(user)
            } else {
                AuthenticationResult.Error("אין משתמש מחובר")
            }
        } catch (e: Exception) {
            AuthenticationResult.Error("שגיאה בקבלת משתמש: ${e.message}")
        }
    }
}