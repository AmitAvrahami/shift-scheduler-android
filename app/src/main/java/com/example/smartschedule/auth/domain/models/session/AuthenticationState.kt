package com.example.smartschedule.auth.domain.models.session

import com.example.smartschedule.auth.domain.models.user.User


sealed class AuthenticationState {
    object Unauthenticated : AuthenticationState()
    data class Authenticated(val user: User, val session: Session) : AuthenticationState()
    data class Error(val message: String) : AuthenticationState()
    object Loading : AuthenticationState()
}