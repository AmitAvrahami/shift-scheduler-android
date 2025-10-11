package com.smartschedule.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smartschedule.domain.models.UserRole

/**
 * Represents an employee in the database.
 */
@Entity(tableName = "employees")
data class EmployeeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "is_student")
    val isStudent: Boolean,

    @ColumnInfo(name = "max_shifts_per_week")
    val maxShiftsPerWeek: Int = 5,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,

    @ColumnInfo(name = "can_work_friday")
    val canWorkFriday: Boolean = false,

    @ColumnInfo(name = "can_work_saturday")
    val canWorkSaturday: Boolean = false,

    @ColumnInfo(name = "user_role")
    val userRole: UserRole,

    @ColumnInfo(name = "notes")
    val notes: String? = null
)