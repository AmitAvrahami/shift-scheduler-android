package com.smartschedule.data.local.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smartschedule.domain.models.ScheduleStatus
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Represents a work schedule for a specific week in the database.
 */
@Entity(tableName = "work_schedules")
data class WorkScheduleEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "week_start_date")
    val weekStartDate: LocalDate?,

    @ColumnInfo(name = "status")
    val status: ScheduleStatus,

    @ColumnInfo(name = "created_by")
    val createdBy: String,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime,

    @ColumnInfo(name = "approved_at")
    val approvedAt: LocalDateTime? = null
)