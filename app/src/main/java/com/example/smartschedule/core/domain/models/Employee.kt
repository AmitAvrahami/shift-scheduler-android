package com.example.smartschedule.core.domain.models

import java.time.LocalDateTime

data class Employee(
    override val id: String,
    override val name: String,
    override val email: String,
    val employeeNumber: String,
    val maxShiftPerWeek: Int = 5,
    override val type: UserType = UserType.EMPLOYEE,
    override val status: UserStatus = UserStatus.ACTIVE,
    override val createdDate: LocalDateTime = LocalDateTime.now()
): User(id,name,email,type){

    fun canWorkShift(shift: Shift): Boolean {
        if (!isVisible()) return false

        return shift.assignedEmployeeId == null || shift.assignedEmployeeId == this.id
    }

    /**
     * מחזיר את סך השעות שהעובד עובד בשבוע
     * TODO: לשפר את החישוב בעתיד
     */
    fun getTotalHoursInWeek(shifts: List<Shift>): Int {
        return shifts.filter {
            it.assignedEmployeeId == id
        }.size * 8
    }


}
