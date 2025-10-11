package com.smartschedule.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smartschedule.domain.models.ShiftType
import java.time.LocalDate

/**
 * Represents a single shift in the database.
 */
@Entity(tableName = "shifts")
data class ShiftEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "work_schedule_id")
    val workScheduleId: Long,

    @ColumnInfo(name = "date")
    val date: LocalDate,

    @ColumnInfo(name = "shift_type")
    val shiftType: ShiftType,

    @ColumnInfo(name = "required_headcount")
    val requiredHeadcount: Int,

    @ColumnInfo(name = "notes")
    val notes: String? = null
)