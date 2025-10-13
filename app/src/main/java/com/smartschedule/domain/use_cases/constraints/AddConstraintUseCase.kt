package com.smartschedule.domain.use_cases.constraints

import com.smartschedule.domain.models.Constraint
import com.smartschedule.domain.repositories.ConstraintRepository
import com.smartschedule.utils.Resource
import javax.inject.Inject

class AddConstraintUseCase @Inject constructor(
    private val repository: ConstraintRepository
) {

    suspend operator fun invoke(constraint: Constraint): Resource<Unit> {
        return try {
            repository.insertConstraint(constraint)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("שגיאה בעת הוספת אילוץ: ${e.localizedMessage ?: "שגיאה לא צפויה"}")
        }
    }
}
