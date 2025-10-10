package com.smartschedule.data.local.dao

import androidx.room.*
import com.smartschedule.data.local.entities.RecurringConstraintEntity

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

    @Query("SELECT * FROM recurring_constraints WHERE employee_id = :employeeId")
    suspend fun getRecurringConstraintsByEmployee(employeeId: Long): List<RecurringConstraintEntity>
}
