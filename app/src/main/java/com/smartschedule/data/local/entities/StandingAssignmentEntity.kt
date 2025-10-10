package com.smartschedule.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smartschedule.domain.models.ShiftType
import java.time.DayOfWeek

@Entity(tableName = "standing_assignments")
data class StandingAssignmentEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "employee_id")
    val employeeId: Long,

    @ColumnInfo(name = "day_of_week")
    val dayOfWeek: DayOfWeek,

    @ColumnInfo(name = "shift_type")
    val shiftType: ShiftType,

    @ColumnInfo(name = "active")
    val active: Boolean
)