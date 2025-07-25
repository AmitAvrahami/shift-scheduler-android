package com.example.smartschedule.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartschedule.domain.models.Employee
import com.example.smartschedule.domain.repository.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository
): ViewModel()  {

    private val _employees = MutableStateFlow<List<Employee>>(emptyList())
    val employees : StateFlow<List<Employee>> = _employees.asStateFlow()

    // UI States
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadEmployees()
    }


    fun loadEmployees() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                employeeRepository.getAllEmployees().collect { allEmployees ->
                    _employees.value = allEmployees
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = "שגיאה בטעינת עובדים: ${e.message}"
                _isLoading.value = false
            }
        }
    }


    fun addEmployee(employee: Employee) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                employeeRepository.insertEmployee(employee)
                Log.d("EmployeeViewModel", "עובד נוסף: ${employee.name}")
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "שגיאה ביצירת עובד: ${e.message}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

}