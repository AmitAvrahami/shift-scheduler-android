package com.smartschedule.data.repository

import com.smartschedule.data.local.dao.StandingAssignmentDao
import com.smartschedule.data.mappers.toDomain
import com.smartschedule.data.mappers.toEntity
import com.smartschedule.domain.models.StandingAssignment
import com.smartschedule.domain.repositories.StandingAssignmentRepository

class StandingAssignmentRepositoryImpl(
    private val standingAssignmentDao: StandingAssignmentDao
) : StandingAssignmentRepository {
    override suspend fun getAllStandingAssignments(): List<StandingAssignment> {
        return standingAssignmentDao.getAllStandingAssignments().map { it.toDomain() }
    }

    override suspend fun getStandingAssignmentById(id: Long): StandingAssignment? {
        return standingAssignmentDao.getStandingAssignmentById(id)?.toDomain()
    }

    override suspend fun insertStandingAssignment(standingAssignment: StandingAssignment) {
        standingAssignmentDao.insertStandingAssignment(standingAssignment.toEntity())
    }

    override suspend fun updateStandingAssignment(standingAssignment: StandingAssignment) {
        standingAssignmentDao.updateStandingAssignment(standingAssignment.toEntity())
    }

    override suspend fun deleteStandingAssignment(standingAssignment: StandingAssignment) {
        standingAssignmentDao.deleteStandingAssignment(standingAssignment.toEntity())
    }
}
