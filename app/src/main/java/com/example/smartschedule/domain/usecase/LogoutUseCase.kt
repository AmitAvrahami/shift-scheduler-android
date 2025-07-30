// File: app/src/main/java/com/example/smartschedule/domain/usecase/LogoutUseCase.kt

package com.example.smartschedule.domain.usecase

import com.example.smartschedule.domain.common.Result
import com.example.smartschedule.domain.repository.UserRepository

class LogoutUseCase(
    private val userRepository: UserRepository
) {

    // 🆕 NEW - Result<T> based implementation
    suspend fun executeWithResult(): Result<Unit> {
        // Simple delegation to repository
        return userRepository.logoutWithResult()
    }
}