package com.smartschedule.domain.use_cases.work_schedules

import com.smartschedule.domain.models.WorkSchedule
import com.smartschedule.domain.repositories.WorkScheduleRepository
import com.smartschedule.utils.Resource
import javax.inject.Inject

class DeleteWorkScheduleUseCase @Inject constructor(
    private val repository: WorkScheduleRepository
) {
    suspend operator fun invoke(workSchedule: WorkSchedule): Resource<Unit> {
        TODO("Not yet implemented")
    }
}
