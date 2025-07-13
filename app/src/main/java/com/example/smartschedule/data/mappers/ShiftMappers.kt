package com.example.smartschedule.data.mappers

import com.example.smartschedule.data.database.entities.ShiftEntity
import com.example.smartschedule.domain.models.Shift
import com.example.smartschedule.domain.models.ShiftType

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