package com.example.smartschedule.data.repository

import com.example.smartschedule.data.database.dao.ShiftDao
import com.example.smartschedule.data.mappers.toDomain
import com.example.smartschedule.data.mappers.toEntity
import com.example.smartschedule.domain.models.Shift
import com.example.smartschedule.domain.repository.ShiftRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShiftRepositoryImpl(
    private val shiftDao: ShiftDao
) : ShiftRepository {

    override fun getAllShifts(): Flow<List<Shift>> {
        return shiftDao.getAllShifts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertShift(shift: Shift) {
        shiftDao.insertShift(shift.toEntity())
    }

    override suspend fun deleteShift(shift: Shift) {
        shiftDao.deleteShift(shift.toEntity())
    }

    override suspend fun getShiftById(id: String): Shift? {
        return shiftDao.getShiftById(id)?.toDomain()
    }

    override fun getShiftsByEmployee(employeeId: String): Flow<List<Shift>> {
        return shiftDao.getShiftsByEmployee(employeeId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}