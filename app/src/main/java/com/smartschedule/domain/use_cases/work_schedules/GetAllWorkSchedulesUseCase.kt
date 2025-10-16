package com.smartschedule.domain.use_cases.work_schedules

import com.smartschedule.domain.models.WorkSchedule
import com.smartschedule.domain.repositories.WorkScheduleRepository
import com.smartschedule.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllWorkSchedulesUseCase @Inject constructor(
    private val repository: WorkScheduleRepository
) {
    suspend operator fun invoke(): Resource<Flow<List<WorkSchedule>>> {
        TODO("Not yet implemented")
    }
}
