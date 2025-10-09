package com.smartschedule.domain.models

import java.time.LocalDate

data class Constraint (
    val id: Long = 0L,
    val employeeId: Long,
    val dateStart: LocalDate,
    val dateEnd: LocalDate,
    val constraintType: ConstraintType,
    val shiftType: ShiftType? = null,
    val reason: String? = null,
){

    init {
        require(employeeId > 0L) { "employeeId חייב להיות > 0" }
        require(!dateStart.isAfter(dateEnd)) { "dateStart חייב להיות לפני או שווה ל-dateEnd" }

        when (constraintType) {
            ConstraintType.SINGLE_SHIFT -> {
                require(dateStart == dateEnd) { "SINGLE_SHIFT: חייב להיות תאריך יחיד" }
                require(shiftType != null) { "SINGLE_SHIFT: חובה לספק shiftType" }
            }
            ConstraintType.DAY_OFF -> {
                require(dateStart == dateEnd) { "DAY_OFF: חייב להיות תאריך יחיד" }
                require(shiftType == null) { "DAY_OFF: לא מספקים shiftType (חוסם יום שלם)" }
            }
            ConstraintType.DATE_RANGE -> {
                // מותר shiftType == null (חוסם כל המשמרות בטווח),
                // או shiftType != null (חוסם משמרת מסוימת לאורך הטווח)
            }
        }

        require(reason == null || reason.length <= 200) { "reason עד 200 תווים" }
    }
}