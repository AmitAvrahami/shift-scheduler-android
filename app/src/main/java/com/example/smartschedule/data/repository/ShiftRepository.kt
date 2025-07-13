package com.example.smartschedule.data.repository

import com.example.smartschedule.domain.models.Shift
import kotlinx.coroutines.flow.Flow

interface ShiftRepository {
    fun getAllShifts(): Flow<List<Shift>>
    suspend fun insertShift(shift: Shift)
    suspend fun deleteShift(shift: Shift)
    suspend fun getShiftById(id: String): Shift?
    fun getShiftsByEmployee(employeeId: String): Flow<List<Shift>>
}