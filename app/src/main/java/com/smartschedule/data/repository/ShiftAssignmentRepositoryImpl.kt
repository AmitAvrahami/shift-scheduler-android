package com.smartschedule.data.repository

import com.smartschedule.data.local.dao.ShiftAssignmentDao
import com.smartschedule.data.mappers.toDomain
import com.smartschedule.data.mappers.toEntity
import com.smartschedule.domain.models.ShiftAssignment
import com.smartschedule.domain.repositories.ShiftAssignmentRepository

class ShiftAssignmentRepositoryImpl(
    private val shiftAssignmentDao: ShiftAssignmentDao
) : ShiftAssignmentRepository {
    override suspend fun getAllShiftAssignments(): List<ShiftAssignment> {
        return shiftAssignmentDao.getAllShiftAssignments().map { it.toDomain() }
    }

    override suspend fun getShiftAssignmentById(id: Long): ShiftAssignment? {
        return shiftAssignmentDao.getShiftAssignmentById(id)?.toDomain()
    }

    override suspend fun insertShiftAssignment(shiftAssignment: ShiftAssignment) {
        shiftAssignmentDao.insertShiftAssignment(shiftAssignment.toEntity())
    }

    override suspend fun updateShiftAssignment(shiftAssignment: ShiftAssignment) {
        shiftAssignmentDao.updateShiftAssignment(shiftAssignment.toEntity())
    }

    override suspend fun deleteShiftAssignment(shiftAssignment: ShiftAssignment) {
        shiftAssignmentDao.deleteShiftAssignment(shiftAssignment.toEntity())
    }
}
