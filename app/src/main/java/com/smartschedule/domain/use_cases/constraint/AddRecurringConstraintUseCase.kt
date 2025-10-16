package com.smartschedule.domain.use_cases.constraint

import com.smartschedule.domain.models.RecurringConstraint
import com.smartschedule.domain.repositories.RecurringConstraintRepository
import com.smartschedule.domain.use_cases.common.safeCall
import javax.inject.Inject

class AddRecurringConstraintUseCase @Inject constructor(
    private val repository: RecurringConstraintRepository
) {
    suspend operator fun invoke(recurringConstraint: RecurringConstraint) = safeCall(
        validate = {
            require(recurringConstraint.employeeId > 0) { "מזהה עובד לא חוקי" }
            requireNotNull(recurringConstraint.dayOfWeek) { "יום בשבוע חייב להיות מוגדר" }
            require(recurringConstraint.shiftTypes.isNotEmpty()) { "חייב להיות לפחות סוג משמרת אחד" }
        },
        block = {
            repository.insertRecurringConstraint(recurringConstraint)
        }
    )
    }

