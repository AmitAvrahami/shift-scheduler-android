package com.smartschedule.data.local.dao

import androidx.room.*
import com.smartschedule.data.local.entities.ShiftAssignmentEntity

/**
 * Data Access Object for the shift_assignments table.
 */
@Dao
interface ShiftAssignmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShiftAssignment(assignment: ShiftAssignmentEntity)

    @Update
    suspend fun updateShiftAssignment(assignment: ShiftAssignmentEntity)

    @Delete
    suspend fun deleteShiftAssignment(assignment: ShiftAssignmentEntity)

    @Query("SELECT * FROM shift_assignments")
    suspend fun getAllShiftAssignments(): List<ShiftAssignmentEntity>

    @Query("SELECT * FROM shift_assignments WHERE id = :id")
    suspend fun getShiftAssignmentById(id: Long): ShiftAssignmentEntity?

    @Query("SELECT * FROM shift_assignments WHERE shift_id = :shiftId")
    suspend fun getAssignmentsByShift(shiftId: Long): List<ShiftAssignmentEntity>

    @Query("SELECT * FROM shift_assignments WHERE employee_id = :employeeId")
    suspend fun getAssignmentsByEmployee(employeeId: Long): List<ShiftAssignmentEntity>
}
