package com.smartschedule.data.repository

import com.smartschedule.data.local.dao.ShiftDao
import com.smartschedule.data.mappers.toDomain
import com.smartschedule.data.mappers.toEntity
import com.smartschedule.domain.models.Shift
import com.smartschedule.domain.repositories.ShiftRepository

class ShiftRepositoryImpl(
    private val shiftDao: ShiftDao
) : ShiftRepository {
    override suspend fun getAllShifts(): List<Shift> {
        return shiftDao.getAllShifts().map { it.toDomain() }
    }

    override suspend fun getShiftById(id: Long): Shift? {
        return shiftDao.getShiftById(id)?.toDomain()
    }

    override suspend fun insertShift(shift: Shift) {
        shiftDao.insertShift(shift.toEntity())
    }
    override suspend fun insertShifts(shifts: List<Shift>) =
        shiftDao.insertShifts(shifts.map { it.toEntity() })

    override suspend fun updateShift(shift: Shift) {
        shiftDao.updateShift(shift.toEntity())
    }

    override suspend fun deleteShift(shift: Shift) {
        shiftDao.deleteShift(shift.toEntity())
    }
}
