package com.smartschedule.data.mappers

import com.smartschedule.data.local.entities.EmployeeEntity
import com.smartschedule.domain.models.Employee

fun EmployeeEntity.toDomain(): Employee {
    return Employee(
        id = id,
        name = name,
        isStudent = isStudent,
        maxShiftsPerWeek = maxShiftsPerWeek,
        isActive = isActive,
        canWorkFriday = canWorkFriday,
        canWorkSaturday = canWorkSaturday,
        userRole = userRole,
        notes = notes
    )
}

fun Employee.toEntity(): EmployeeEntity {
    return EmployeeEntity(
        id = id ?: 0,
        name = name,
        isStudent = isStudent,
        maxShiftsPerWeek = maxShiftsPerWeek,
        isActive = isActive,
        canWorkFriday = canWorkFriday,
        canWorkSaturday = canWorkSaturday,
        userRole = userRole,
        notes = notes
    )
}
