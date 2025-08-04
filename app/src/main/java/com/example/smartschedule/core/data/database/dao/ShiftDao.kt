package com.example.smartschedule.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smartschedule.core.data.database.entities.ShiftEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShiftDao {

    @Query("SELECT * FROM shifts")
    fun getAllShifts(): Flow<List<ShiftEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShift(shift: ShiftEntity)

    @Delete
    suspend fun deleteShift(shift: ShiftEntity)

    @Query("SELECT * FROM shifts WHERE id = :id")
    suspend fun getShiftById(id: String): ShiftEntity?

    @Query("SELECT * FROM shifts WHERE assignedEmployeeId = :employeeId")
    fun getShiftsByEmployee(employeeId: String): Flow<List<ShiftEntity>>
}