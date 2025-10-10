package com.smartschedule.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.smartschedule.data.local.entities.ShiftEntity
import com.smartschedule.data.local.entities.WorkScheduleEntity

data class WorkScheduleWithShifts(
    @Embedded val workSchedule: WorkScheduleEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "work_schedule_id"
    )
    val shifts: List<ShiftEntity>
)