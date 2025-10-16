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

}