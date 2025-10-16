package com.smartschedule.domain.logic.rules.hard

import com.smartschedule.domain.logic.rules.base.BaseRulesHandler
import com.smartschedule.domain.logic.rules.context.ValidationContext
import com.smartschedule.domain.logic.rules.result.ValidationResult
import kotlin.collections.iterator

class NoDoublePostingRule : BaseRulesHandler() {
    override fun validate(context: ValidationContext, result: ValidationResult) {
        val grouped = context.assignments.groupBy { it.employeeId }
        for ((empId, shifts) in grouped) {
            val daily = shifts.groupBy { it.assignedAt }
            daily.forEach { (date, list) ->
                if (list.size > 1) {
                    result.success = false
                    result.errors += "העובד $empId שובץ פעמיים ביום $date"
                }
            }
        }

    }
}