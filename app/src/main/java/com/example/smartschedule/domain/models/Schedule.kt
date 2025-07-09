package com.example.smartschedule.domain.models

import java.time.LocalDate

data class Schedule(
    val id: String,
    val weekStartDate : LocalDate,
    val shifts : List<Shift> = emptyList(),
    val isPublished : Boolean = false,
){
    fun getUnassignedShiftsCount() : Int{
        return shifts.count{shift -> shift.assignedEmployeeId == null}
    }

    fun isCompletelyAssigned() : Boolean{
        return shifts.all { shift -> shift.assignedEmployeeId != null }
    }
}
