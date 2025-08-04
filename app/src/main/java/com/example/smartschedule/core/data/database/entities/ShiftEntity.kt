package com.example.smartschedule.core.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(tableName = "shifts")
data class ShiftEntity(
    @PrimaryKey val id: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val shiftType : String,
    val assignedEmployeeId : String?
)
