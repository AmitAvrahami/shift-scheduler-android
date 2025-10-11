package com.smartschedule.data.mappers

import com.smartschedule.data.local.entities.RecurringConstraintEntity
import com.smartschedule.domain.models.RecurringConstraint

fun RecurringConstraintEntity.toDomain(): RecurringConstraint {
    return RecurringConstraint(
        id = id,
        employeeId = employeeId,
        dayOfWeek = dayOfWeek,
        shiftTypes = shiftTypes,
        reason = reason,
        validFrom = validFrom,
        validUntil = validUntil
    )
}

fun RecurringConstraint.toEntity(): RecurringConstraintEntity {
    return RecurringConstraintEntity(
        id = id,
        employeeId = employeeId,
        dayOfWeek = dayOfWeek,
        shiftTypes = shiftTypes,
        reason = reason,
        validFrom = validFrom,
        validUntil = validUntil
    )
}
