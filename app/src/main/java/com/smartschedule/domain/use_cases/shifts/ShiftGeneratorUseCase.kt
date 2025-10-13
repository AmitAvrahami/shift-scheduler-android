package com.smartschedule.domain.use_cases.shifts

import com.smartschedule.domain.models.Shift
import com.smartschedule.domain.models.ShiftTemplate
import java.time.LocalDate

class ShiftGeneratorUseCase {


    operator fun invoke(
        shiftTemplates: List<ShiftTemplate>,
        startDate: LocalDate,
        workScheduleId: Long = 0L
    ): List<Shift> {

        val shifts = mutableListOf<Shift>()
        val endDate = startDate.plusDays(6) //TODO : make this constant value
        var currentDate = startDate

        while (!currentDate.isAfter(endDate)) {
            val shiftsForDay = generateShiftsForDay(shiftTemplates, currentDate, workScheduleId)
            shifts.addAll(shiftsForDay)
            currentDate = currentDate.plusDays(1)
        }

        return shifts
    }

    private fun generateShiftsForDay(
        shiftTemplates: List<ShiftTemplate>,
        currentDate: LocalDate,
        workScheduleId: Long
    ): List<Shift> {
        val dayOfWeek = currentDate.dayOfWeek

        val templatesForDay = shiftTemplates.filter { it.dayOfWeek == dayOfWeek }

        return templatesForDay.map { template ->
            Shift(
                id = 0L,
                workScheduleId = workScheduleId,
                date = currentDate,
                shiftType = template.shiftType,
                requiredHeadcount = template.requiredHeadcount,
                notes = null
            )
        }
    }
}
