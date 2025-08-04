// File: app/src/main/java/com/example/smartschedule/domain/usecase/GetCurrentUserUseCase.kt

package com.example.smartschedule.core.domain.usecase

import com.example.smartschedule.core.domain.models.User

class GetCurrentUserUseCase(
    private val userRepository: com.example.smartschedule.core.domain.repository.UserRepository
) {

    // 🆕 NEW - Result<T> based implementation
    suspend fun executeWithResult(): com.example.smartschedule.core.domain.common.Result<User?> {
        // Simple delegation to repository
        return userRepository.getCurrentUserWithResult()
    }

    // 🆕 BONUS - Additional helper for non-null current user
    suspend fun requireCurrentUserWithResult(): com.example.smartschedule.core.domain.common.Result<User> {
        return when (val result = userRepository.getCurrentUserWithResult()) {
            is com.example.smartschedule.core.domain.common.Result.Success -> {
                val user = result.data
                if (user != null) {
                    _root_ide_package_.com.example.smartschedule.core.domain.common.Result.Success(user)
                } else {
                    _root_ide_package_.com.example.smartschedule.core.domain.common.Result.Error(IllegalStateException("משתמש לא מחובר למערכת"))
                }
            }
            is com.example.smartschedule.core.domain.common.Result.Error -> result
        }
    }
}