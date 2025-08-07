package com.example.smartschedule.auth.domain.models.session

data class Session(
    val sessionId: String,
    val userId: String,
    val loginTime: Long,
    val expirationTime: Long,
    val isActive: Boolean = true
)