package com.smartschedule.data.mappers

import com.smartschedule.data.local.entities.StandingAssignmentEntity
import com.smartschedule.domain.models.StandingAssignment

fun StandingAssignmentEntity.toDomain(): StandingAssignment {
    return StandingAssignment(
        id = id,
        employeeId = employeeId,
        dayOfWeek = dayOfWeek,
        shiftType = shiftType,
        active = active
    )
}

fun StandingAssignment.toEntity(): StandingAssignmentEntity {
    return StandingAssignmentEntity(
        id = id,
        employeeId = employeeId,
        dayOfWeek = dayOfWeek,
        shiftType = shiftType,
        active = active
    )
}
