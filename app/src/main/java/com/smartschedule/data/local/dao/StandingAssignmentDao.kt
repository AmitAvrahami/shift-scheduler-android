package com.smartschedule.data.local.dao

import androidx.room.*
import com.smartschedule.data.local.entities.StandingAssignmentEntity

/**
 * Data Access Object for the standing_assignments table.
 */
@Dao
interface StandingAssignmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStandingAssignment(assignment: StandingAssignmentEntity)

    @Update
    suspend fun updateStandingAssignment(assignment: StandingAssignmentEntity)

    @Delete
    suspend fun deleteStandingAssignment(assignment: StandingAssignmentEntity)

    @Query("SELECT * FROM standing_assignments")
    suspend fun getAllStandingAssignments(): List<StandingAssignmentEntity>

    @Query("SELECT * FROM standing_assignments WHERE id = :id")
    suspend fun getStandingAssignmentById(id: Long): StandingAssignmentEntity?

    @Query("SELECT * FROM standing_assignments WHERE employee_id = :employeeId")
    suspend fun getStandingAssignmentsByEmployee(employeeId: Long): List<StandingAssignmentEntity>
}
