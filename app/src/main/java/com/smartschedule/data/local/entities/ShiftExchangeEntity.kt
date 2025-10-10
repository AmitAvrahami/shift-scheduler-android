package com.smartschedule.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smartschedule.domain.models.ExchangeStatus
import java.time.LocalDateTime

@Entity(tableName = "shift_exchanges")
data class ShiftExchangeEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "from_employee_id")
    val fromEmployeeId: Long,

    @ColumnInfo(name = "to_employee_id")
    val toEmployeeId: Long? = null,

    @ColumnInfo(name = "shift_id")
    val shiftId: Long,

    @ColumnInfo(name = "status")
    val status: ExchangeStatus,

    @ColumnInfo(name = "requested_at")
    val requestedAt: LocalDateTime,

    @ColumnInfo(name = "resolved_at")
    val resolvedAt: LocalDateTime? = null,

    @ColumnInfo(name = "manager_note")
    val managerNote: String? = null
)