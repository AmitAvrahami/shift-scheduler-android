package com.smartschedule.data.local.dao

import androidx.room.*
import com.smartschedule.data.local.entities.ShiftEntity

@Dao
interface ShiftDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShift(shift: ShiftEntity)

    @Update
    suspend fun updateShift(shift: ShiftEntity)

    @Delete
    suspend fun deleteShift(shift: ShiftEntity)

    @Query("SELECT * FROM shifts")
    suspend fun getAllShifts(): List<ShiftEntity>

    @Query("SELECT * FROM shifts WHERE work_schedule_id = :workScheduleId")
    suspend fun getShiftsByWorkSchedule(workScheduleId: Long): List<ShiftEntity>
}
