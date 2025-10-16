package com.smartschedule.data.mappers

import com.smartschedule.data.local.entities.ShiftAssignmentEntity
import com.smartschedule.domain.models.ShiftAssignment
import com.smartschedule.domain.models.ShiftType

fun ShiftAssignmentEntity.toDomain(): ShiftAssignment {
    return ShiftAssignment(
        id = id,
        shiftId = shiftId,
        employeeId = employeeId,
        assignedAt = assignedAt,
        assignedBy = assignedBy,
        shiftType = ShiftType.NOON, // TODO: Implement shift type
    )
}

fun ShiftAssignment.toEntity(): ShiftAssignmentEntity {
    return ShiftAssignmentEntity(
        id = id,
        shiftId = shiftId,
        employeeId = employeeId,
        assignedAt = assignedAt,
        assignedBy = assignedBy
    )
}
