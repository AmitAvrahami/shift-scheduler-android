package com.smartschedule.domain.repositories

import com.smartschedule.domain.models.Shift

interface ShiftRepository {
    suspend fun getAllShifts(): List<Shift>
    suspend fun getShiftById(id: Long): Shift?
    suspend fun insertShift(shift: Shift)
    suspend fun updateShift(shift: Shift)
    suspend fun deleteShift(shift: Shift)
}
