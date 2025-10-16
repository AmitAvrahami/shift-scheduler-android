package com.smartschedule.domain.use_cases.constraint

import com.smartschedule.domain.models.Constraint
import com.smartschedule.domain.repositories.ConstraintRepository
import com.smartschedule.domain.use_cases.common.safeListCall
import com.smartschedule.utils.Resource
import java.time.LocalDate
import javax.inject.Inject

class GetConstraintsForWeekUseCase @Inject constructor(
    private val repository: ConstraintRepository
) {
    suspend operator fun invoke(
        weekStart: LocalDate,
        weekEnd: LocalDate
    ): Resource<List<Constraint>> = safeListCall(
        validate = {
            require(!weekEnd.isBefore(weekStart)) {
                "תאריך סיום השבוע לא יכול להיות לפני תאריך התחלה"
            }
        },
        block = {
            val allConstraints = repository.getAllConstraints()

            allConstraints.filter { constraint ->
                constraint.dateStart <= weekEnd && constraint.dateEnd >= weekStart
            }
        }
    )
}