package com.smartschedule.domain.logic.rules.soft

import com.smartschedule.domain.logic.rules.base.BaseRulesHandler
import com.smartschedule.domain.logic.rules.context.ValidationContext
import com.smartschedule.domain.logic.rules.result.ValidationResult
import com.smartschedule.domain.models.ShiftType
import kotlin.collections.iterator

class LimitNightShiftsRule(
    private val maxNightsPerWeek: Int = 2
) : BaseRulesHandler() {
    override fun validate(context: ValidationContext, result: ValidationResult) {
        val grouped = context.assignments.groupBy { it.employeeId }

        for ((empId, shifts) in grouped) {
            val nights = shifts.count { it.shiftType == ShiftType.NIGHT }
            if (nights > maxNightsPerWeek) {
                result.success = false
                result.errors += "העובד $empId שובץ ל-$nights לילות (מקסימום $maxNightsPerWeek)"
            }
        }
    }
}