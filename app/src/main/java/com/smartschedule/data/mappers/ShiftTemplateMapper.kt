package com.smartschedule.data.mappers

import com.smartschedule.data.local.entities.ShiftTemplateEntity
import com.smartschedule.domain.models.ShiftTemplate

fun ShiftTemplateEntity.toDomain(): ShiftTemplate {
    return ShiftTemplate(
        id = id,
        dayOfWeek = dayOfWeek,
        shiftType = shiftType,
        requiredHeadcount = requiredHeadcount
    )
}

fun ShiftTemplate.toEntity(): ShiftTemplateEntity {
    return ShiftTemplateEntity(
        id = id,
        dayOfWeek = dayOfWeek,
        shiftType = shiftType,
        requiredHeadcount = requiredHeadcount
    )
}
