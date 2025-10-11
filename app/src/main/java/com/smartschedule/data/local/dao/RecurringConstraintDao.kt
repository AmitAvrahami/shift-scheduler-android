package com.smartschedule.data.local.dao

import androidx.room.*
import com.smartschedule.data.local.entities.RecurringConstraintEntity

/**
 * Data Access Object for the recurring_constraints table.
 */
@Dao
interface RecurringConstraintDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecurringConstraint(constraint: RecurringConstraintEntity)

    @Update
    suspend fun updateRecurringConstraint(constraint: RecurringConstraintEntity)

    @Delete
    suspend fun deleteRecurringConstraint(constraint: RecurringConstraintEntity)

    @Query("SELECT * FROM recurring_constraints")
    suspend fun getAllRecurringConstraints(): List<RecurringConstraintEntity>

    @Query("SELECT * FROM recurring_constraints WHERE id = :id")
    suspend fun getRecurringConstraintById(id: Long): RecurringConstraintEntity?

    @Query("SELECT * FROM recurring_constraints WHERE employee_id = :employeeId")
    suspend fun getRecurringConstraintsByEmployee(employeeId: Long): List<RecurringConstraintEntity>
}
