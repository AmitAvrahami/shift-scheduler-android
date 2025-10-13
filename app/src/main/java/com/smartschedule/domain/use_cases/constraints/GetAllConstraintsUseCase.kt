package com.smartschedule.domain.use_cases.constraints

import com.smartschedule.domain.models.Constraint
import com.smartschedule.domain.repositories.ConstraintRepository
import com.smartschedule.utils.Resource
import javax.inject.Inject

class GetAllConstraintsUseCase @Inject constructor(
    private val repository: ConstraintRepository
) {

    suspend operator fun invoke(): Resource<List<Constraint>> {
        return try {
            val constraints = repository.getAllConstraints()
            if (constraints.isEmpty()) {
                Resource.Error("לא נמצאו אילוצים במערכת")
            } else {
                Resource.Success(constraints)
            }
        } catch (e: Exception) {
            Resource.Error("שגיאה בעת טעינת אילוצים: ${e.localizedMessage}")
        }
    }
}
