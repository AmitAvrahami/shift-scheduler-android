package com.example.smartschedule.domain.common

import com.example.smartschedule.domain.models.User

sealed class AuthenticationResult {
    object Loading : AuthenticationResult()
    data class Success(val user: User?) : AuthenticationResult()
    data class Error(val message: String) : AuthenticationResult()
}