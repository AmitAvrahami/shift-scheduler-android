package com.smartschedule.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.smartschedule.data.local.entities.ConstraintEntity
import com.smartschedule.data.local.entities.EmployeeEntity

data class EmployeeWithConstraints(
    @Embedded val employee: EmployeeEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "employee_id"
    )
    val constraints: List<ConstraintEntity>
)