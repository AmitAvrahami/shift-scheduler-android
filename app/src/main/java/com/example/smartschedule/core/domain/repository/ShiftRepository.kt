package com.example.smartschedule.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface ShiftRepository {
    fun getAllShifts(): Flow<List<com.example.smartschedule.core.domain.models.Shift>>
    suspend fun insertShift(shift: com.example.smartschedule.core.domain.models.Shift)
    suspend fun deleteShift(shift: com.example.smartschedule.core.domain.models.Shift)
    suspend fun getShiftById(id: String): com.example.smartschedule.core.domain.models.Shift?
    fun getShiftsByEmployee(employeeId: String): Flow<List<com.example.smartschedule.core.domain.models.Shift>>

//    fun getAllShifts(): Flow<Result<List<Shift>>>
//    suspend fun insertShift(shift: Shift) : Result<Shift>
//    suspend fun deleteShift(shift: Shift): Result<Shift>
//    suspend fun getShiftById(id: String): Result<Shift?>
//    fun getShiftsByEmployee(employeeId: String): Flow<Result<List<Shift>>>
}