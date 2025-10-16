package com.smartschedule.domain.logic.rules.hard

import com.smartschedule.domain.logic.rules.base.BaseRulesHandler
import com.smartschedule.domain.logic.rules.context.ValidationContext
import com.smartschedule.domain.logic.rules.result.ValidationResult
import kotlin.collections.iterator

class MaxShiftsPerWeekRule : BaseRulesHandler() {
    override fun validate(context: ValidationContext, result: ValidationResult) {
        val grouped = context.assignments.groupBy { it.employeeId }
        val maxByEmployee = context.employees.associateBy({ it.id }, { it.maxShiftsPerWeek })

        for ((empId, shifts) in grouped) {
            val maxAllowed = maxByEmployee[empId] ?: continue
            if (shifts.size > maxAllowed) {
                result.success = false
                result.errors += "העובד $empId שובץ ל-${shifts.size} משמרות (מקסימום מותר: $maxAllowed)"
            }
        }
    }
}