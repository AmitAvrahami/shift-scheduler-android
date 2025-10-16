package com.smartschedule.domain.models

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

data class WorkSchedule(
    val id: Long,
    val weekStartDate: LocalDate?,
    val status: ScheduleStatus,
    val createdBy: String,
    val createdAt: LocalDateTime,
    val approvedAt: LocalDateTime? = null,
){
    init {
        require(!createdBy.isBlank()) { "createdBy is required"}
    }
}
