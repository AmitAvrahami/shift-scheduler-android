package com.example.smartschedule.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "employees")
data class EmployeeEntity(
    @PrimaryKey val id : String,
    val name : String,
    val email : String,
    val employeeNumber : String,
    val maxShiftPerWeek : Int = 5,
    val userType : String = "EMPLOYEE",
    val status: String = "ACTIVE",
    val createdDate: LocalDateTime = LocalDateTime.now()
)
