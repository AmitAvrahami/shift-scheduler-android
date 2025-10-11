package com.smartschedule.domain.repositories

import com.smartschedule.domain.models.ShiftExchange

interface ShiftExchangeRepository {
    suspend fun getAllShiftExchanges(): List<ShiftExchange>
    suspend fun getShiftExchangeById(id: Long): ShiftExchange?
    suspend fun insertShiftExchange(shiftExchange: ShiftExchange)
    suspend fun updateShiftExchange(shiftExchange: ShiftExchange)
    suspend fun deleteShiftExchange(shiftExchange: ShiftExchange)
}
