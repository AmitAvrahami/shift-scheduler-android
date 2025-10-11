package com.smartschedule.domain.repositories

import com.smartschedule.domain.models.ShiftAssignment

interface ShiftAssignmentRepository {
    suspend fun getAllShiftAssignments(): List<ShiftAssignment>
    suspend fun getShiftAssignmentById(id: Long): ShiftAssignment?
    suspend fun insertShiftAssignment(shiftAssignment: ShiftAssignment)
    suspend fun updateShiftAssignment(shiftAssignment: ShiftAssignment)
    suspend fun deleteShiftAssignment(shiftAssignment: ShiftAssignment)
}
