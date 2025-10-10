package com.smartschedule.data.local.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smartschedule.domain.models.ShiftType
import java.time.DayOfWeek
import java.time.LocalDate

@Entity(tableName = "recurring_constraints")
data class RecurringConstraintEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "employee_id")
    val employeeId: Long,

    @ColumnInfo(name = "day_of_week")
    val dayOfWeek: DayOfWeek,

    @ColumnInfo(name = "shift_types")
    val shiftTypes: Set<ShiftType>,

    @ColumnInfo(name = "reason")
    val reason: String? = null,

    @ColumnInfo(name = "valid_from")
    val validFrom: LocalDate? = null,

    @ColumnInfo(name = "valid_until")
    val validUntil: LocalDate? = null
)