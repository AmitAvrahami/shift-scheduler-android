package com.smartschedule.domain.use_cases.shift

import com.smartschedule.domain.models.Employee
import com.smartschedule.domain.models.Shift
import com.smartschedule.domain.models.ShiftAssignment
import com.smartschedule.domain.repositories.ShiftAssignmentRepository
import com.smartschedule.domain.use_cases.common.safeCall
import com.smartschedule.utils.Resource
import java.time.LocalDateTime
import javax.inject.Inject

class AssignEmployeeToShiftUseCase @Inject constructor(
    private val repository: ShiftAssignmentRepository
) {
    suspend operator fun invoke(employee: Employee, shift: Shift): Resource<Unit> = safeCall(
        validate = {
            employee.id?.let { require(it > 0) { "מזהה עובד לא תקין" } }
            require(shift.id > 0) { "מזהה משמרת לא תקין" }
            require(employee.isActive) { "לא ניתן לשבץ עובד שאינו פעיל" }
            require(shift.requiredHeadcount > 0) { "לא ניתן לשבץ למשמרת עם תקן אפסי" }
        },
        block = {
            val assignment = ShiftAssignment(
                id = 0L, // יווצר אוטומטית ע״י Room
                shiftId = shift.id,
                employeeId = employee.id!!,
                assignedAt = LocalDateTime.now(),
                assignedBy = "מערכת",
                shiftType = shift.shiftType
            )
            repository.insertShiftAssignment(assignment)
        }
    )
}
