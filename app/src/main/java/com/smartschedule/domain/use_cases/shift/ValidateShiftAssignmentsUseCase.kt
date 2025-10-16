package com.smartschedule.domain.use_cases.shift

import com.smartschedule.domain.models.ShiftAssignment
import com.smartschedule.utils.Resource
import javax.inject.Inject

class ValidateShiftAssignmentsUseCase @Inject constructor() {

    suspend operator fun invoke(assignments: List<ShiftAssignment>): Resource<Unit> {
        return try {
            require(assignments.isNotEmpty()) { "רשימת השיבוצים ריקה" }

            val duplicates = assignments.groupBy { it.employeeId to it.shiftId }
                .filter { it.value.size > 1 }

            require(duplicates.isEmpty()) {
            }

            val byDay = assignments.groupBy { it.employeeId to it.shiftId }

            assignments.forEach { a ->
                require(a.employeeId > 0) { "מזהה עובד לא תקין בשיבוץ ${a.id}" }
                require(a.shiftId > 0) { "מזהה משמרת לא תקין בשיבוץ ${a.id}" }
                require(a.assignedBy.isNotBlank()) { "שם משבץ חסר בשיבוץ ${a.id}" }
            }

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "שגיאה באימות השיבוצים")
        }
    }
}
