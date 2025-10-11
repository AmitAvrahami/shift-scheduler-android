package com.smartschedule.domain.repositories

import com.smartschedule.domain.models.StandingAssignment

interface StandingAssignmentRepository {
    suspend fun getAllStandingAssignments(): List<StandingAssignment>
    suspend fun getStandingAssignmentById(id: Long): StandingAssignment?
    suspend fun insertStandingAssignment(standingAssignment: StandingAssignment)
    suspend fun updateStandingAssignment(standingAssignment: StandingAssignment)
    suspend fun deleteStandingAssignment(standingAssignment: StandingAssignment)
}
