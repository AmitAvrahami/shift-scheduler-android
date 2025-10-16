package com.smartschedule.domain.use_cases.shift

import com.smartschedule.domain.models.Shift
import com.smartschedule.domain.models.ShiftType
import com.smartschedule.domain.repositories.ShiftRepository
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

class GenerateWeeklyShiftsUseCase @Inject constructor(
    private val repository: ShiftRepository
) {
    suspend operator fun invoke(weekStart: LocalDate): List<Shift> {
        require(weekStart.dayOfWeek == DayOfWeek.SUNDAY) {
            "תאריך תחילת השבוע חייב להיות יום ראשון"
        }

        val shifts = mutableListOf<Shift>()
        var idCounter = 0L

        for (i in 0..6) {
            val currentDate = weekStart.plusDays(i.toLong())

            when (currentDate.dayOfWeek) {
                DayOfWeek.FRIDAY -> {
                    shifts += createShift(idCounter++, currentDate, ShiftType.MORNING, 2)
                    shifts += createShift(idCounter++, currentDate, ShiftType.NOON, 1)
                }
                DayOfWeek.SATURDAY -> {
                    shifts += createShift(idCounter++, currentDate, ShiftType.MORNING, 1)
                    shifts += createShift(idCounter++, currentDate, ShiftType.NOON, 1)
                    shifts += createShift(idCounter++, currentDate, ShiftType.NIGHT, 1)
                }
                else -> {
                    shifts += createShift(idCounter++, currentDate, ShiftType.MORNING, 2)
                    shifts += createShift(idCounter++, currentDate, ShiftType.NOON, 2)
                    shifts += createShift(idCounter++, currentDate, ShiftType.NIGHT, 1)
                }
            }
        }

        shifts.forEach { repository.insertShift(it) }

        return shifts
    }

    private fun createShift(id: Long, date: LocalDate, type: ShiftType, count: Int): Shift {
        return Shift(
            id = id,
            workScheduleId = 0L,
            date = date,
            shiftType = type,
            requiredHeadcount = count
        )
    }
}
