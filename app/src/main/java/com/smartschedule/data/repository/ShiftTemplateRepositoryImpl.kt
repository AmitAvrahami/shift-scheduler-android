package com.smartschedule.data.repository

import com.smartschedule.data.local.dao.ShiftTemplateDao
import com.smartschedule.data.mappers.toDomain
import com.smartschedule.data.mappers.toEntity
import com.smartschedule.domain.models.ShiftTemplate
import com.smartschedule.domain.repositories.ShiftTemplateRepository

class ShiftTemplateRepositoryImpl(
    private val shiftTemplateDao: ShiftTemplateDao
) : ShiftTemplateRepository {
    override suspend fun getAllShiftTemplates(): List<ShiftTemplate> {
        return shiftTemplateDao.getAllShiftTemplates().map { it.toDomain() }
    }

    override suspend fun getShiftTemplateById(id: Long): ShiftTemplate? {
        return shiftTemplateDao.getShiftTemplateById(id)?.toDomain()
    }

    override suspend fun insertShiftTemplate(shiftTemplate: ShiftTemplate) {
        shiftTemplateDao.insertShiftTemplate(shiftTemplate.toEntity())
    }

    override suspend fun updateShiftTemplate(shiftTemplate: ShiftTemplate) {
        shiftTemplateDao.updateShiftTemplate(shiftTemplate.toEntity())
    }

    override suspend fun deleteShiftTemplate(shiftTemplate: ShiftTemplate) {
        shiftTemplateDao.deleteShiftTemplate(shiftTemplate.toEntity())
    }
}
