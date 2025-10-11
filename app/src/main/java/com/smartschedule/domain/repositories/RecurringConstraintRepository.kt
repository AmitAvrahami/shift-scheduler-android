package com.smartschedule.domain.repositories

import com.smartschedule.domain.models.RecurringConstraint

interface RecurringConstraintRepository {
    suspend fun getAllRecurringConstraints(): List<RecurringConstraint>
    suspend fun getRecurringConstraintById(id: Long): RecurringConstraint?
    suspend fun insertRecurringConstraint(recurringConstraint: RecurringConstraint)
    suspend fun updateRecurringConstraint(recurringConstraint: RecurringConstraint)
    suspend fun deleteRecurringConstraint(recurringConstraint: RecurringConstraint)
}
