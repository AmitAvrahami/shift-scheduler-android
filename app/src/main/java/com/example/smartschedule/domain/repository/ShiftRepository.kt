package com.example.smartschedule.domain.repository

import com.example.smartschedule.domain.common.Result
import com.example.smartschedule.domain.models.Shift
import kotlinx.coroutines.flow.Flow

interface ShiftRepository {
    fun getAllShifts(): Flow<List<Shift>>
    suspend fun insertShift(shift: Shift)
    suspend fun deleteShift(shift: Shift)
    suspend fun getShiftById(id: String): Shift?
    fun getShiftsByEmployee(employeeId: String): Flow<List<Shift>>

//    fun getAllShifts(): Flow<Result<List<Shift>>>
//    suspend fun insertShift(shift: Shift) : Result<Shift>
//    suspend fun deleteShift(shift: Shift): Result<Shift>
//    suspend fun getShiftById(id: String): Result<Shift?>
//    fun getShiftsByEmployee(employeeId: String): Flow<Result<List<Shift>>>
}