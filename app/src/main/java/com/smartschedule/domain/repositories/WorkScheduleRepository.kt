package com.smartschedule.domain.repositories

import com.smartschedule.domain.models.WorkSchedule

interface WorkScheduleRepository {
    suspend fun getAllWorkSchedules(): List<WorkSchedule>
    suspend fun getWorkScheduleById(id: Long): WorkSchedule?
    suspend fun insertWorkSchedule(workSchedule: WorkSchedule)
    suspend fun updateWorkSchedule(workSchedule: WorkSchedule)
    suspend fun deleteWorkSchedule(workSchedule: WorkSchedule)
}
