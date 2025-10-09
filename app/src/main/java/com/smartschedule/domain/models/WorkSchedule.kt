package com.smartschedule.domain.models

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

data class WorkSchedule(
    val id: Long,
    val weekStartDate: LocalDate,
    val status: ScheduleStatus,
    val createdBy: String,
    val createdAt: LocalDateTime,
    val approvedAt: LocalDateTime? = null,
){
    init {
        require(id >= 0L) { "id must be >= 0" }
        require(createdBy.isNotBlank()) { "createdBy must not be blank" }
        require(weekStartDate.dayOfWeek == DayOfWeek.SUNDAY) { "weekStartDate must be a Sunday" }

        if (approvedAt != null) {
            require(!approvedAt.isBefore(createdAt)) { "approvedAt must be >= createdAt" }
        }

        when (status) {
            ScheduleStatus.DRAFT -> require(approvedAt == null) { "DRAFT must not have approvedAt" }
            ScheduleStatus.PENDING_APPROVAL -> require(approvedAt == null) { "PENDING_APPROVAL must not have approvedAt" }
            ScheduleStatus.PUBLISHED -> require(approvedAt != null) { "PUBLISHED must have approvedAt" }
        }
    }
}
