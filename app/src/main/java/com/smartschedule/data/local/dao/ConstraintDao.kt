package com.smartschedule.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.smartschedule.data.local.entities.ConstraintEntity
import java.time.LocalDate

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

    @Query("SELECT * FROM constraints WHERE id = :id")
    suspend fun getConstraintById(id: Long): ConstraintEntity?

    @Query("SELECT * FROM constraints WHERE employee_id = :employeeId AND start_date >= :weekStart AND end_date <= :weekEnd")
    suspend fun getForEmployee(employeeId: Long, weekStart: LocalDate, weekEnd: LocalDate): List<ConstraintEntity>
}
