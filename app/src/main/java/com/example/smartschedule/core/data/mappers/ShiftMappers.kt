package com.example.smartschedule.core.data.mappers

import com.example.smartschedule.core.data.database.entities.ShiftEntity
import com.example.smartschedule.core.domain.models.Shift
import com.example.smartschedule.core.domain.models.ShiftType

fun Shift.toEntity(): ShiftEntity {
    return ShiftEntity(
        id = this.id,
        startTime = this.startTime,
        endTime = this.endTime,
        shiftType = this.shiftType.name,
        assignedEmployeeId = this.assignedEmployeeId,
    )
}


fun ShiftEntity.toDomain(): Shift {
    return Shift(
        id = this.id,
        startTime = this.startTime,
        endTime = this.endTime,
        shiftType = ShiftType.valueOf(this.shiftType),
        assignedEmployeeId = this.assignedEmployeeId
    )
}