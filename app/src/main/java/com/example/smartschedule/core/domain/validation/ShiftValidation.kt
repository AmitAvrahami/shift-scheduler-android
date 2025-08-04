package com.example.smartschedule.core.domain.validation

    import java.time.Duration
    import java.time.LocalDateTime

object ShiftValidation {

        fun isValidShift(shift: com.example.smartschedule.core.domain.models.Shift): Boolean {
            return shift.startTime.isBefore(shift.endTime)
        }

        fun canEmployeeWork(employee: com.example.smartschedule.core.domain.models.Employee, shift: com.example.smartschedule.core.domain.models.Shift): Boolean {
            return shift.assignedEmployeeId == null || employee.id == shift.assignedEmployeeId
        }

        fun getShiftDurationHours(shift: com.example.smartschedule.core.domain.models.Shift): Long {
            return Duration.between(shift.startTime, shift.endTime).toHours()
        }

        fun isStandardShift(shift: com.example.smartschedule.core.domain.models.Shift): Boolean {
            return getShiftDurationHours(shift) == 8L // TODO: Make a constant for this value
        }


        fun validateShiftTimes(startTime: LocalDateTime, endTime: LocalDateTime): String? {
            return when {
                startTime.isAfter(endTime) -> "שעת סיום חייבת להיות אחרי שעת התחלה"
                startTime.isEqual(endTime) -> "שעת התחלה ושעת הסיום לא יכולות להיות זהות"
                else -> null
            }
        }


        fun validateShiftDuration(startTime: LocalDateTime, endTime: LocalDateTime): String? {
            val duration = Duration.between(startTime, endTime)
            val hours = duration.toHours()

            return when {
                hours > 12 -> "משמרת לא יכולה להיות יותר מ-12 שעות"
                hours < 1 -> "משמרת חייבת להיות לפחות שעה אחת"
                else -> null  // הכל בסדר
            }
        }

    }