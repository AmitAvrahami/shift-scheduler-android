package com.smartschedule.data.repository

import com.smartschedule.data.local.dao.WorkScheduleDao
import com.smartschedule.data.mappers.toDomain
import com.smartschedule.data.mappers.toEntity
import com.smartschedule.domain.models.WorkSchedule
import com.smartschedule.domain.repositories.WorkScheduleRepository

class WorkScheduleRepositoryImpl(
    private val workScheduleDao: WorkScheduleDao
) : WorkScheduleRepository {
    override suspend fun getAllWorkSchedules(): List<WorkSchedule> {
        return workScheduleDao.getAllWorkSchedules().map { it.toDomain() }
    }

    override suspend fun getWorkScheduleById(id: Long): WorkSchedule? {
        return workScheduleDao.getWorkScheduleById(id)?.toDomain()
    }

    override suspend fun insertWorkSchedule(workSchedule: WorkSchedule) {
        workScheduleDao.insertWorkSchedule(workSchedule.toEntity())
    }

    override suspend fun updateWorkSchedule(workSchedule: WorkSchedule) {
        workScheduleDao.updateWorkSchedule(workSchedule.toEntity())
    }

    override suspend fun deleteWorkSchedule(workSchedule: WorkSchedule) {
        workScheduleDao.deleteWorkSchedule(workSchedule.toEntity())
    }
}
