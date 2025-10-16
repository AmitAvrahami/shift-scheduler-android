package com.smartschedule.data.local.dao

import androidx.room.*
import com.smartschedule.data.local.entities.ShiftEntity
import java.time.LocalDate

/**
 * Data Access Object for the shifts table.
 */
@Dao
interface ShiftDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShift(shift: ShiftEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShifts(shifts: List<ShiftEntity>)

    @Update
    suspend fun updateShift(shift: ShiftEntity)

    @Delete
    suspend fun deleteShift(shift: ShiftEntity)

    @Query("SELECT * FROM shifts")
    suspend fun getAllShifts(): List<ShiftEntity>

    @Query("SELECT * FROM shifts WHERE id = :id")
    suspend fun getShiftById(id: Long): ShiftEntity?

    @Query("SELECT * FROM shifts WHERE work_schedule_id = :workScheduleId")
    suspend fun getShiftsByWorkSchedule(workScheduleId: Long): List<ShiftEntity>

    @Query("SELECT s.* FROM shifts s LEFT JOIN shift_assignments sa ON s.id = sa.shift_id GROUP BY s.id HAVING COUNT(sa.id) < s.required_headcount AND s.date BETWEEN :weekStart AND :weekEnd")
    suspend fun getAvailableShifts(weekStart: LocalDate, weekEnd: LocalDate): List<ShiftEntity>

    @Query("SELECT * FROM shifts WHERE date BETWEEN :start AND :end")
    suspend fun getShiftsBetween(start: LocalDate, end: LocalDate): List<ShiftEntity>
}
