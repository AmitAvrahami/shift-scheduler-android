package com.smartschedule.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smartschedule.domain.models.ConstraintType
import com.smartschedule.domain.models.ShiftType
import java.time.LocalDate
import java.time.LocalTime

/**
 * Represents a constraint in the database.
 */
@Entity(tableName = "constraints")
data class ConstraintEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "employee_id")
    val employeeId: Long,

    @ColumnInfo(name = "start_date")
    val startDate: LocalDate,

    @ColumnInfo(name = "end_date")
    val endDate: LocalDate,

    @ColumnInfo(name = "start_time")
    val startTime: LocalTime? = null,

    @ColumnInfo(name = "end_time")
    val endTime: LocalTime? = null,

    @ColumnInfo("shift_type")
    val shiftType: ShiftType? = null,


    @ColumnInfo(name = "type")
    val type: ConstraintType,



    @ColumnInfo(name = "reason")
    val reason: String? = null
)