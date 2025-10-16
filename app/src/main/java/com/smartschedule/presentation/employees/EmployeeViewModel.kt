package com.smartschedule.presentation.employees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartschedule.domain.use_cases.employee.AddEmployeeUseCase
import com.smartschedule.domain.use_cases.employee.DeactivateEmployeeUseCase
import com.smartschedule.domain.use_cases.employee.GetAllEmployeesUseCase
import com.smartschedule.domain.use_cases.employee.UpdateEmployeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val getAllEmployeesUseCase: GetAllEmployeesUseCase,
    private val addEmployeeUseCase: AddEmployeeUseCase,
    private val updateEmployeeUseCase: UpdateEmployeeUseCase,
    private val deactivateEmployeeUseCase: DeactivateEmployeeUseCase,
) : ViewModel() {

    fun getAllEmployees() = viewModelScope.launch {}
    fun addEmployee() = viewModelScope.launch {}
    fun updateEmployee() = viewModelScope.launch {}
    fun deactivateEmployee() = viewModelScope.launch {}
    fun searchEmployee() = viewModelScope.launch {}
    fun manageTagsAndNotes() = viewModelScope.launch {}

    /*
    שליפת רשימת עובדים (GetAllEmployeesUseCase)


הוספה (AddEmployeeUseCase)

עדכון (UpdateEmployeeUseCase)

השבתה / הפעלה מחדש (DeactivateEmployeeUseCase)

חיפוש לפי שם / תפקיד

ניהול תגיות או הערות על עובדים
     */
}