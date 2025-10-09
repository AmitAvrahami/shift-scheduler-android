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
    init {
        require(employeeId > 0L) { "employeeId חייב להיות > 0" }
        require(shiftTypes.isNotEmpty()) { "shiftTypes לא יכול להיות ריק" }
        if (validFrom != null && validUntil != null) {
            require(!validFrom.isAfter(validUntil)) { "validFrom חייב להיות לפני/שווה ל-validUntil" }
        }
        require(reason == null || reason.length <= 200) { "reason עד 200 תווים" }
    }
}
