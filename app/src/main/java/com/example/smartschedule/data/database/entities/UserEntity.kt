package com.example.smartschedule.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id : String,
    val name : String,
    val email : String,
    val passwordHash : String,
    val userType : String = "EMPLOYEE"
)