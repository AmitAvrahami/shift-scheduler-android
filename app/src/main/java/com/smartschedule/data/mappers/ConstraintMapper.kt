    package com.smartschedule.data.mappers

    import com.smartschedule.data.local.entities.ConstraintEntity
    import com.smartschedule.domain.models.Constraint

    fun ConstraintEntity.toDomain(): Constraint {
        return Constraint(
            id = id,
            employeeId = employeeId,
            dateStart = startDate,
            dateEnd = endDate,
            constraintType = type,
            shiftType = shiftType,
            reason = reason
        )
    }

    fun Constraint.toEntity(): ConstraintEntity {
        return ConstraintEntity(
            id = id,
            employeeId = employeeId,
            startDate = dateStart,
            endDate = dateEnd,
            type = constraintType,
            shiftType = shiftType,
            reason = reason
        )
    }
