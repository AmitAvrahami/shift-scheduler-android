package com.smartschedule.domain.repositories

import com.smartschedule.domain.models.Shift
import java.time.LocalDate

interface ShiftRepository {
    suspend fun getAllShifts(): List<Shift>
    suspend fun getShiftById(id: Long): Shift?
    suspend fun insertShift(shift: Shift)
    suspend fun insertShifts(shifts: List<Shift>)
    suspend fun updateShift(shift: Shift)
    suspend fun deleteShift(shift: Shift)
    suspend fun getAvailableShifts(weekStart : LocalDate, weekEnd : LocalDate) : List<Shift>

    suspend fun getShiftsBetween(start : LocalDate, end : LocalDate) : List<Shift>
}
