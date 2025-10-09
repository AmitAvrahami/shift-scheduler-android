package com.smartschedule.domain.models

import java.time.DayOfWeek

data class StandingAssignment(
    val id: Long,
    val employeeId: Long,
    val dayOfWeek: DayOfWeek,
    val shiftType: ShiftType,
    val active: Boolean,
)
