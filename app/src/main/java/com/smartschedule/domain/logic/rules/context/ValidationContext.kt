package com.smartschedule.domain.logic.rules.context

import com.smartschedule.domain.models.Employee
import com.smartschedule.domain.models.ShiftAssignment

data class ValidationContext(
    val assignments: List<ShiftAssignment>,
    val employees: List<Employee>
)