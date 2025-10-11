package com.smartschedule.data.local.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * Represents the assignment of an employee to a specific shift in the database.
 */
@Entity(tableName = "shift_assignments")
data class ShiftAssignmentEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "shift_id")
    val shiftId: Long,

    @ColumnInfo(name = "employee_id")
    val employeeId: Long,

    @ColumnInfo(name = "assigned_at")
    val assignedAt: LocalDateTime,

    @ColumnInfo(name = "assigned_by")
    val assignedBy: String
)