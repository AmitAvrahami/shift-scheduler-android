package com.example.smartschedule.domain.models

import java.time.LocalDateTime

data class Shift(
    val id : String,
    val startTime : LocalDateTime,
    val endTime : LocalDateTime,
    val assignedEmployeeId : String? = null,
)
