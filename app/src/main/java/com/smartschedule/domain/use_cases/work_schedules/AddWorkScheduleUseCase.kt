package com.smartschedule.domain.use_cases.work_schedules

import com.smartschedule.domain.models.WorkSchedule
import com.smartschedule.domain.repositories.WorkScheduleRepository
import com.smartschedule.utils.Resource
import javax.inject.Inject

class AddWorkScheduleUseCase @Inject constructor(
    private val repository: WorkScheduleRepository
) {

    suspend operator fun invoke(workSchedule: WorkSchedule): Resource<Unit> {
        return try {
            if (workSchedule.weekStartDate == null) {
                return Resource.Error("תאריך התחלה לשבוע לא יכול להיות ריק")
            }

            repository.insertWorkSchedule(workSchedule)

            Resource.Success(Unit)

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error("שגיאה בעת יצירת סידור עבודה: ${e.localizedMessage ?: "שגיאה לא צפויה"}")
        }
    }
}
