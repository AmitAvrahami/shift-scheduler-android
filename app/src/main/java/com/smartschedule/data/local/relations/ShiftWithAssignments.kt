package com.smartschedule.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.smartschedule.data.local.entities.ShiftAssignmentEntity
import com.smartschedule.data.local.entities.ShiftEntity

data class ShiftWithAssignments(
    @Embedded val shift: ShiftEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "shift_id"
    )
    val assignments: List<ShiftAssignmentEntity>
)