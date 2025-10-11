package com.smartschedule.data.mappers

import com.smartschedule.data.local.entities.ShiftEntity
import com.smartschedule.domain.models.Shift

fun ShiftEntity.toDomain(): Shift {
    return Shift(
        id = id,
        workScheduleId = workScheduleId,
        date = date,
        shiftType = shiftType,
        requiredHeadcount = requiredHeadcount,
        notes = notes
    )
}

fun Shift.toEntity(): ShiftEntity {
    return ShiftEntity(
        id = id,
        workScheduleId = workScheduleId,
        date = date,
        shiftType = shiftType,
        requiredHeadcount = requiredHeadcount,
        notes = notes
    )
}
