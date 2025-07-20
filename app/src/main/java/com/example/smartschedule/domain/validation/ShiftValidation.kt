    package com.example.smartschedule.domain.validation

    import com.example.smartschedule.domain.models.Employee
    import com.example.smartschedule.domain.models.Shift
    import java.time.Duration
    import java.time.LocalDateTime

    object ShiftValidation {

        fun isValidShift(shift: Shift): Boolean {
            return shift.startTime.isBefore(shift.endTime)
        }

        fun canEmployeeWork(employee: Employee, shift: Shift): Boolean {
            return shift.assignedEmployeeId == null || employee.id == shift.assignedEmployeeId
        }

        fun getShiftDurationHours(shift: Shift): Long {
            return Duration.between(shift.startTime, shift.endTime).toHours()
        }

        fun isStandardShift(shift: Shift): Boolean {
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