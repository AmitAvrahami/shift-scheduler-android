package com.example.smartschedule.domain.usecase

import com.example.smartschedule.domain.common.AuthenticationResult
import com.example.smartschedule.domain.repository.UserRepository

class LoginUseCase(
    private val userRepository: UserRepository
) {
    suspend fun execute(username: String, password: String): AuthenticationResult {
        // TODO: Add business logic here
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
}