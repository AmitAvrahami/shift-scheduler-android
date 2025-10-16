package com.smartschedule.domain.models

import java.time.LocalDate
import java.time.LocalTime


data class Shift(
    val id: Long,
    val workScheduleId: Long,
    val date: LocalDate,
    val shiftType: ShiftType,
    val requiredHeadcount: Int,
    val notes: String? = null
){

    init {
        require(id >= 0L) { "id must be >= 0" }
        require(workScheduleId >= 0L) { "workScheduleId must be >= 0" }
        require(requiredHeadcount > 0) { "requiredHeadcount must be > 0" }
    }

    val startTime: LocalTime = when (shiftType) {
        ShiftType.MORNING -> LocalTime.of(6, 45)
        ShiftType.NOON    -> LocalTime.of(14, 45)
        ShiftType.NIGHT   -> LocalTime.of(22, 45)
    }
    val endTime: LocalTime = when (shiftType) {
        ShiftType.MORNING -> LocalTime.of(14, 45)
        ShiftType.NOON    -> LocalTime.of(22, 45)
        ShiftType.NIGHT   -> LocalTime.of(6, 45)
    }
    fun spansToNextDay(): Boolean = shiftType == ShiftType.NIGHT

    fun endsOnDate(): LocalDate = if (spansToNextDay()) date.plusDays(1) else date
}