package com.example.smartschedule.domain.validation

import com.example.smartschedule.domain.models.Employee
import com.example.smartschedule.domain.models.Shift
import java.time.Duration

object ShiftValidation {

    fun isValidShift(shift: Shift): Boolean {
        return shift.startTime.isBefore(shift.endTime)
    }

    fun canEmployeeWork(employee: Employee, shift: Shift): Boolean {
        return shift.assignedEmployeeId == null || employee.id == shift.assignedEmployeeId
    }

    fun getShiftDurationHours(shift: Shift): Long {
        return Duration.between(shift.startTime, shift.endTime).toHours()
    }

    fun isStandardShift(shift: Shift): Boolean {
        return getShiftDurationHours(shift) == 8L // TODO: Make a constant for this value
    }
}