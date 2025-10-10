package com.smartschedule.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.smartschedule.data.local.entities.EmployeeEntity
import com.smartschedule.data.local.entities.RecurringConstraintEntity

data class EmployeeWithRecurringConstraints(
    @Embedded val employee: EmployeeEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "employee_id"
    )
    val recurringConstraints: List<RecurringConstraintEntity>
)