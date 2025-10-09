package com.smartschedule.domain.models

import java.time.DayOfWeek

data class ShiftTemplate(
    val id: Long,
    val dayOfWeek: DayOfWeek,
    val shiftType: ShiftType,
    val requiredHeadcount: Int,
)
