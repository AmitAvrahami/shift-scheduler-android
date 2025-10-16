package com.smartschedule.domain.use_cases.shift

import com.smartschedule.domain.models.ShiftAssignment
import com.smartschedule.domain.repositories.ShiftAssignmentRepository
import com.smartschedule.domain.use_cases.common.safeCall
import com.smartschedule.utils.Resource
import javax.inject.Inject

class UnassignEmployeeFromShiftUseCase @Inject constructor(
    private val repository: ShiftAssignmentRepository
) {
    suspend operator fun invoke(shiftAssignment: ShiftAssignment): Resource<Unit> = safeCall(
        validate = {
            require(shiftAssignment.id > 0) { "מזהה השיבוץ חייב להיות גדול מ־0" }
            require(shiftAssignment.employeeId > 0) { "מזהה העובד לא תקין" }
            require(shiftAssignment.shiftId > 0) { "מזהה המשמרת לא תקין" }
        },
        block = {
            repository.deleteShiftAssignment(shiftAssignment)
        }
    )
}
