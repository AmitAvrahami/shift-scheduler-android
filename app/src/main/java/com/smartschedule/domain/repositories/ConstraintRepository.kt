package com.smartschedule.domain.repositories

import com.smartschedule.domain.models.Constraint

interface ConstraintRepository {
    suspend fun getAllConstraints(): List<Constraint>
    suspend fun getConstraintById(id: Long): Constraint?
    suspend fun insertConstraint(constraint: Constraint)
    suspend fun updateConstraint(constraint: Constraint)
    suspend fun deleteConstraint(constraint: Constraint)
}
