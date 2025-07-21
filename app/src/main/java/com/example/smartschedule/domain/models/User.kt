package com.example.smartschedule.domain.models

open class User(
    open val id: Int,
    open val name: String,
    open val email: String,
    open val type : UserType
)