package com.smartschedule.data.mappers

import com.smartschedule.data.local.entities.WorkScheduleEntity
import com.smartschedule.domain.models.WorkSchedule

fun WorkScheduleEntity.toDomain(): WorkSchedule {
    return WorkSchedule(
        id = id,
        weekStartDate = weekStartDate,
        status = status,
        createdBy = createdBy,
        createdAt = createdAt,
        approvedAt = approvedAt
    )
}

fun WorkSchedule.toEntity(): WorkScheduleEntity {
    return WorkScheduleEntity(
        id = id,
        weekStartDate = weekStartDate,
        status = status,
        createdBy = createdBy,
        createdAt = createdAt,
        approvedAt = approvedAt
    )
}
