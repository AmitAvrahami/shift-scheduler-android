// File: app/src/main/java/com/example/smartschedule/domain/usecase/LogoutUseCase.kt

package com.example.smartschedule.core.domain.usecase

class LogoutUseCase(
    private val userRepository: com.example.smartschedule.core.domain.repository.UserRepository
) {

    // 🆕 NEW - Result<T> based implementation
    suspend fun executeWithResult(): com.example.smartschedule.core.domain.common.Result<Unit> {
        // Simple delegation to repository
        return userRepository.logoutWithResult()
    }
}