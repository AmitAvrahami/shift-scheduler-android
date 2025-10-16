package com.smartschedule.presentation.shifts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartschedule.domain.use_cases.employee.GetAvailableEmployeesForShiftUseCase
import com.smartschedule.domain.use_cases.shift.DeleteShiftUseCase
import com.smartschedule.domain.use_cases.shift.GetShiftsForDayUseCase
import com.smartschedule.domain.use_cases.shift.UnassignEmployeeFromShiftUseCase
import com.smartschedule.domain.use_cases.shift.UpdateShiftUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShiftViewModel @Inject constructor(
    private val getShiftsForDayUseCase: GetShiftsForDayUseCase,
    private val getAvailableEmployeesForShiftUseCase: GetAvailableEmployeesForShiftUseCase,
    private val unassignEmployeeFromShiftUseCase: UnassignEmployeeFromShiftUseCase,
    private val updateShiftUseCase: UpdateShiftUseCase,
    private val deleteShiftUseCase: DeleteShiftUseCase
) : ViewModel() {

    fun getShiftsForDay() = viewModelScope.launch {}
    fun getAvailableEmployeesForShift() = viewModelScope.launch {}
    fun unassignEmployeeFromShift() = viewModelScope.launch {}
    fun updateShift() = viewModelScope.launch {}
    fun deleteShift() = viewModelScope.launch {}


    /*
    שליפה לפי יום (GetShiftsForDayUseCase)

בדיקת זמינות עובדים (GetAvailableEmployeesForShiftUseCase)

הוספה / עריכה של משמרת (UpdateShiftUseCase)

מחיקה של משמרת (DeleteShiftUseCase)

סנכרון בין סידור לשבוע הנוכחי
     */
}