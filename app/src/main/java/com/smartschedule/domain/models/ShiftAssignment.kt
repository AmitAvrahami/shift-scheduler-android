package com.smartschedule.domain.models

import java.time.LocalDateTime

data class ShiftAssignment(
    val id: Long,
    val shiftId: Long,
    val employeeId: Long,
    val assignedAt: LocalDateTime,
    val assignedBy: String,
){
    init {
        require(id >= 0L) { "id must be >= 0" }
        require(shiftId > 0L) { "shiftId must be > 0" }
        require(employeeId > 0L) { "employeeId must be > 0" }
        require(assignedBy.isNotBlank()) { "assignedBy must not be blank" }
        require(assignedBy.length <= 100) { "assignedBy must be at most 100 chars" }
    }
}
