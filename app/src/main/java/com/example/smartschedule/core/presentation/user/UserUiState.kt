package com.example.smartschedule.core.presentation.user

import com.example.smartschedule.core.domain.models.User
import com.example.smartschedule.core.presentation.common.ErrorState

sealed class UserUiState {
    object Loading : UserUiState()
    data class Success(override val users: List<User>) : UserUiState()
    data class Error(val errorState: ErrorState) : UserUiState()

    val isLoading: Boolean get() = this is Loading
    open val users: List<User>? get() = (this as? Success)?.users
    val errorMessage: String? get() = (this as? Error)?.errorState?.message
}