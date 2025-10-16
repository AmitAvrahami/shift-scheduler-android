package com.smartschedule.domain.use_cases.shift

import com.smartschedule.domain.models.ShiftAssignment
import com.smartschedule.domain.repositories.ShiftAssignmentRepository
import java.time.LocalDate
import javax.inject.Inject

class GetShiftAssignmentsForWeekUseCase @Inject constructor(
    private val repository: ShiftAssignmentRepository
) {
    suspend operator fun invoke(weekStart: LocalDate, weekEnd: LocalDate): List<ShiftAssignment> {
        TODO("Not yet implemented")
    }
}
