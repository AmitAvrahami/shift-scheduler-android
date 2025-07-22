package com.example.smartschedule.domain.models

open class User(
    open val id: String,
    open val name: String,
    open val email: String,
    open val type : UserType
){
    fun canManageEmployees(): Boolean {
        return type == UserType.MANAGER || type == UserType.ADMIN
    }

    fun canManageShifts(): Boolean {
        return type == UserType.MANAGER || type == UserType.ADMIN
    }

    fun canViewAllSchedules(): Boolean {
        return type == UserType.MANAGER || type == UserType.ADMIN
    }
}