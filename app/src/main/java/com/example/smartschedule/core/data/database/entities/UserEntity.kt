package com.example.smartschedule.core.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id : String,
    val name : String,
    val email : String,
    val passwordHash : String,
    val userType : String = "EMPLOYEE",
    val status: String = "ACTIVE",
    val createdDate: LocalDateTime = LocalDateTime.now()
)