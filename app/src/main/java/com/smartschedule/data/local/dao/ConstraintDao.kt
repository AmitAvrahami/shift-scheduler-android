package com.smartschedule.data.local.dao

import androidx.room.*
import com.smartschedule.data.local.entities.ConstraintEntity

@Dao
interface ConstraintDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConstraint(constraint: ConstraintEntity)

    @Update
    suspend fun updateConstraint(constraint: ConstraintEntity)

    @Delete
    suspend fun deleteConstraint(constraint: ConstraintEntity)

    @Query("SELECT * FROM constraints")
    suspend fun getAllConstraints(): List<ConstraintEntity>

    @Query("SELECT * FROM constraints WHERE employee_id = :employeeId")
    suspend fun getConstraintsByEmployee(employeeId: Long): List<ConstraintEntity>
}
