package com.smartschedule.domain.use_cases.constraint

import com.smartschedule.domain.models.Constraint
import com.smartschedule.domain.repositories.ConstraintRepository
import com.smartschedule.domain.use_cases.common.safeCall
import com.smartschedule.utils.Resource
import javax.inject.Inject

class AddConstraintUseCase @Inject constructor(
    private val repository: ConstraintRepository
) {
    suspend operator fun invoke(constraint: Constraint): Resource<Unit> = safeCall(
        validate = {
            require(constraint.dateStart <= constraint.dateEnd) { "תאריך התחלה לא יכול להיות אחרי תאריך סיום" }
            requireNotNull(constraint.constraintType) { "סוג אילוץ חייב להיות מוגדר" }
        },
        block = { repository.insertConstraint(constraint) }
    )
}
