package com.smartschedule.data.repository

import com.smartschedule.data.local.dao.ShiftExchangeDao
import com.smartschedule.data.mappers.toDomain
import com.smartschedule.data.mappers.toEntity
import com.smartschedule.domain.models.ShiftExchange
import com.smartschedule.domain.repositories.ShiftExchangeRepository

class ShiftExchangeRepositoryImpl(
    private val shiftExchangeDao: ShiftExchangeDao
) : ShiftExchangeRepository {
    override suspend fun getAllShiftExchanges(): List<ShiftExchange> {
        return shiftExchangeDao.getAllShiftExchanges().map { it.toDomain() }
    }

    override suspend fun getShiftExchangeById(id: Long): ShiftExchange? {
        return shiftExchangeDao.getShiftExchangeById(id)?.toDomain()
    }

    override suspend fun insertShiftExchange(shiftExchange: ShiftExchange) {
        shiftExchangeDao.insertShiftExchange(shiftExchange.toEntity())
    }

    override suspend fun updateShiftExchange(shiftExchange: ShiftExchange) {
        shiftExchangeDao.updateShiftExchange(shiftExchange.toEntity())
    }

    override suspend fun deleteShiftExchange(shiftExchange: ShiftExchange) {
        shiftExchangeDao.deleteShiftExchange(shiftExchange.toEntity())
    }
}
