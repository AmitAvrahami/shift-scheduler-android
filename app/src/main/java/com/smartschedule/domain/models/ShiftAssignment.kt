package com.smartschedule.domain.models

import java.time.LocalDateTime

data class ShiftAssignment(
    val id: Long,
    val shiftId: Long,
    val shiftType: ShiftType,
    val employeeId: Long,
    val assignedAt: LocalDateTime,
    val assignedBy: String,
){
    init {
        require(id >= 0L) { "id must be >= 0" }
        require(shiftId > 0L) { "shiftId must be > 0" }
        require(employeeId > 0L) { "employeeId must be > 0" }
        require(assignedBy.isNotBlank()) { "assignedBy cannot be blank" }
    }

}
