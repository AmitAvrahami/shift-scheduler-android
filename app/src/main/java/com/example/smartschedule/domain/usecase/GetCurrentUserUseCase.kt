// File: app/src/main/java/com/example/smartschedule/domain/usecase/GetCurrentUserUseCase.kt

package com.example.smartschedule.domain.usecase

import com.example.smartschedule.domain.common.Result
import com.example.smartschedule.domain.models.User
import com.example.smartschedule.domain.repository.UserRepository

class GetCurrentUserUseCase(
    private val userRepository: UserRepository
) {

    // 🆕 NEW - Result<T> based implementation
    suspend fun executeWithResult(): Result<User?> {
        // Simple delegation to repository
        return userRepository.getCurrentUserWithResult()
    }

    // 🆕 BONUS - Additional helper for non-null current user
    suspend fun requireCurrentUserWithResult(): Result<User> {
        return when (val result = userRepository.getCurrentUserWithResult()) {
            is Result.Success -> {
                val user = result.data
                if (user != null) {
                    Result.Success(user)
                } else {
                    Result.Error(IllegalStateException("משתמש לא מחובר למערכת"))
                }
            }
            is Result.Error -> result
        }
    }
}