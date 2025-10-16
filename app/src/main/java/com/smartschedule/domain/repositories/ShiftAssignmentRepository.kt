package com.smartschedule.domain.repositories

import com.smartschedule.domain.models.ShiftAssignment
import java.time.LocalDate

interface ShiftAssignmentRepository {
    suspend fun getAllShiftAssignments(): List<ShiftAssignment>
    suspend fun getShiftAssignmentById(id: Long): ShiftAssignment?
    suspend fun insertShiftAssignment(shiftAssignment: ShiftAssignment)
    suspend fun updateShiftAssignment(shiftAssignment: ShiftAssignment)
    suspend fun deleteShiftAssignment(shiftAssignment: ShiftAssignment)
    suspend fun getForEmployee(employeeId : Long, weekStart : LocalDate, weekEnd : LocalDate) : List<ShiftAssignment>
}
