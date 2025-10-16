package com.smartschedule.data.repository

import com.smartschedule.data.local.dao.RecurringConstraintDao
import com.smartschedule.data.mappers.toDomain
import com.smartschedule.data.mappers.toEntity
import com.smartschedule.domain.models.RecurringConstraint
import com.smartschedule.domain.repositories.RecurringConstraintRepository

class RecurringConstraintRepositoryImpl(
    private val recurringConstraintDao: RecurringConstraintDao
) : RecurringConstraintRepository {
    override suspend fun getAllRecurringConstraints(): List<RecurringConstraint> {
        return recurringConstraintDao.getAllRecurringConstraints().map { it.toDomain() }
    }

    override suspend fun getRecurringConstraintById(id: Long): RecurringConstraint? {
        return recurringConstraintDao.getRecurringConstraintById(id)?.toDomain()
    }

    override suspend fun insertRecurringConstraint(recurringConstraint: RecurringConstraint) {
        recurringConstraintDao.insertRecurringConstraint(recurringConstraint.toEntity())
    }

    override suspend fun updateRecurringConstraint(recurringConstraint: RecurringConstraint) {
        recurringConstraintDao.updateRecurringConstraint(recurringConstraint.toEntity())
    }

    override suspend fun deleteRecurringConstraint(recurringConstraint: RecurringConstraint) {
        recurringConstraintDao.deleteRecurringConstraint(recurringConstraint.toEntity())
    }

    override suspend fun getForEmployee(employeeId: Long): List<RecurringConstraint> {
        return recurringConstraintDao.getForEmployee(employeeId).map { it.toDomain() }
    }
}
