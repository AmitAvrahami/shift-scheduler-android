package com.smartschedule.data.mappers

import com.smartschedule.data.local.entities.ShiftExchangeEntity
import com.smartschedule.domain.models.ShiftExchange

fun ShiftExchangeEntity.toDomain(): ShiftExchange {
    return ShiftExchange(
        id = id,
        fromEmployeeId = fromEmployeeId,
        toEmployeeId = toEmployeeId,
        shiftId = shiftId,
        status = status,
        requestedAt = requestedAt,
        resolvedAt = resolvedAt,
        managerNote = managerNote
    )
}

fun ShiftExchange.toEntity(): ShiftExchangeEntity {
    return ShiftExchangeEntity(
        id = id,
        fromEmployeeId = fromEmployeeId,
        toEmployeeId = toEmployeeId,
        shiftId = shiftId,
        status = status,
        requestedAt = requestedAt,
        resolvedAt = resolvedAt,
        managerNote = managerNote
    )
}
