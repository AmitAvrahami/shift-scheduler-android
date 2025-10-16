package com.smartschedule.domain.models

import java.time.DayOfWeek
import java.time.LocalDate

data class RecurringConstraint(
    val id: Long,
    val employeeId: Long,
    val dayOfWeek: DayOfWeek,
    val shiftTypes: Set<ShiftType>,
    val reason: String? = null,
    val validFrom: LocalDate? = null,
    val validUntil: LocalDate? = null
){

}
