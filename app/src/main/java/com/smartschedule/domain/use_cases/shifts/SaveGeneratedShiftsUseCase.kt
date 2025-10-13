package com.smartschedule.domain.use_cases.shifts

import com.smartschedule.domain.models.Shift
import com.smartschedule.domain.repositories.ShiftRepository
import com.smartschedule.utils.Resource
import javax.inject.Inject


class SaveGeneratedShiftsUseCase @Inject constructor(
    private val shiftRepository: ShiftRepository
){

    suspend operator fun invoke(shifts: List<Shift>): Resource<Unit> {
        return try {
            if (shifts.isEmpty()){
                Resource.Error("לא נוצרו משמרות לשמירה")
            } else{
                shiftRepository.insertShifts(shifts)
                Resource.Success(Unit)
            }
        }catch (e: Exception){
            Resource.Error("שגיאה בעת שמירת המשמרות: ${e.localizedMessage ?: "שגיאה לא צפויה"}")
        }
    }
}