package com.smartschedule.domain.models


import com.smartschedule.domain.models.*


data class EmployeeContext(
    val employee: Employee,
    val constraints: List<Constraint> = emptyList(),
    val recurringConstraints: List<RecurringConstraint> = emptyList(),
    val assignedShifts: List<ShiftAssignment> = emptyList(),
    val availableShifts: List<Shift> = emptyList()
){
    fun canWork(shift: Shift): Boolean {
        // אילוצים חד פעמיים (Constraint)
        val blockedByConstraint = constraints.any { c ->
            shift.date in c.dateStart..c.dateEnd &&
                    (c.shiftType == null || c.shiftType == shift.shiftType)
        }

        // אילוצים חוזרים (RecurringConstraint)
        val blockedByRecurring = recurringConstraints.any { rc ->
            rc.dayOfWeek == shift.date.dayOfWeek &&
                    rc.shiftTypes.contains(shift.shiftType)
        }

        return !blockedByConstraint && !blockedByRecurring
    }

    /**
     * סך כל המשמרות שהעובד כבר שובץ להן השבוע.
     */
    fun currentShiftCount(): Int = assignedShifts.size

    /**
     * האם לעובד יש עוד מקום לשיבוץ לפי הגבלת המקסימום השבועית.
     */
    fun hasRemainingCapacity(): Boolean = currentShiftCount() < employee.maxShiftsPerWeek
}