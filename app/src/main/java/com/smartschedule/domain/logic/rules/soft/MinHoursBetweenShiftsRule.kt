package com.smartschedule.domain.logic.rules.soft

import com.smartschedule.domain.logic.rules.base.BaseRulesHandler
import com.smartschedule.domain.logic.rules.context.ValidationContext
import com.smartschedule.domain.logic.rules.result.ValidationResult
import com.smartschedule.utils.Constants
import java.time.Duration
import kotlin.collections.iterator

class MinHoursBetweenShiftsRule(
    private val minHours: Int = Constants.MIN_HOURS_BETWEEN_SHIFTS
) : BaseRulesHandler() {
    override fun validate(context: ValidationContext, result: ValidationResult) {
        val grouped = context.assignments.groupBy { it.employeeId }
        for ((empId, shifts) in grouped) {
            val sorted = shifts.sortedBy { it.assignedAt }
            for (i in 1 until sorted.size) {
                val diff = Duration.between(sorted[i - 1].assignedAt, sorted[i].assignedAt).toHours()
                if (diff < minHours) {
                    result.success = false
                    result.errors += "העובד $empId נח רק $diff שעות בין משמרות (נדרש $minHours שעות לפחות)"
                }
            }
        }
    }
}