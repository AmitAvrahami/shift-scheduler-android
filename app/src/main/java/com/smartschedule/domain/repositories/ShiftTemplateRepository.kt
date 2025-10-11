package com.smartschedule.domain.repositories

import com.smartschedule.domain.models.ShiftTemplate

interface ShiftTemplateRepository {
    suspend fun getAllShiftTemplates(): List<ShiftTemplate>
    suspend fun getShiftTemplateById(id: Long): ShiftTemplate?
    suspend fun insertShiftTemplate(shiftTemplate: ShiftTemplate)
    suspend fun updateShiftTemplate(shiftTemplate: ShiftTemplate)
    suspend fun deleteShiftTemplate(shiftTemplate: ShiftTemplate)
}
