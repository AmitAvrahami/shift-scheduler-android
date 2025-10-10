package com.smartschedule.data.local.dao

import androidx.room.*
import com.smartschedule.data.local.entities.WorkScheduleEntity

@Dao
interface WorkScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkSchedule(schedule: WorkScheduleEntity)

    @Update
    suspend fun updateWorkSchedule(schedule: WorkScheduleEntity)

    @Delete
    suspend fun deleteWorkSchedule(schedule: WorkScheduleEntity)

    @Query("SELECT * FROM work_schedules")
    suspend fun getAllWorkSchedules(): List<WorkScheduleEntity>

    @Query("SELECT * FROM work_schedules WHERE id = :id LIMIT 1")
    suspend fun getWorkScheduleById(id: Long): WorkScheduleEntity?
}
