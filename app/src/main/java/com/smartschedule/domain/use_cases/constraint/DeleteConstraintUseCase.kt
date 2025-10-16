package com.smartschedule.domain.use_cases.constraint

import com.smartschedule.domain.models.Constraint
import com.smartschedule.domain.repositories.ConstraintRepository
import com.smartschedule.domain.use_cases.common.safeCall
import com.smartschedule.utils.Resource
import javax.inject.Inject

class DeleteConstraintUseCase @Inject constructor(
    private val repository: ConstraintRepository
) {
    suspend operator fun invoke(constraint: Constraint): Resource<Unit> = safeCall(
        validate = {
            require(constraint.id > 0) { "מזהה אילוץ לא חוקי – לא ניתן למחוק אילוץ ללא מזהה" }
        },
        block = {
            repository.deleteConstraint(constraint)
        }
    )
}