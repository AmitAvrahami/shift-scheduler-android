package com.smartschedule.data.repository

import com.smartschedule.data.local.dao.ConstraintDao
import com.smartschedule.data.mappers.toDomain
import com.smartschedule.data.mappers.toEntity
import com.smartschedule.domain.models.Constraint
import com.smartschedule.domain.repositories.ConstraintRepository

class ConstraintRepositoryImpl(
    private val constraintDao: ConstraintDao
) : ConstraintRepository {
    override suspend fun getAllConstraints(): List<Constraint> {
        return constraintDao.getAllConstraints().map { it.toDomain() }
    }

    override suspend fun getConstraintById(id: Long): Constraint? {
        return constraintDao.getConstraintById(id)?.toDomain()
    }

    override suspend fun insertConstraint(constraint: Constraint) {
        return constraintDao.insertConstraint(constraint.toEntity())
    }

    override suspend fun updateConstraint(constraint: Constraint) {
        constraintDao.updateConstraint(constraint.toEntity())
    }

    override suspend fun deleteConstraint(constraint: Constraint) {
        constraintDao.deleteConstraint(constraint.toEntity())
    }
}
