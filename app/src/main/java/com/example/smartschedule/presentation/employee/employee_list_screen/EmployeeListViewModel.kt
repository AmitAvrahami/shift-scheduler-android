package com.example.smartschedule.presentation.employee.employee_list_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartschedule.domain.models.Employee
import com.example.smartschedule.domain.repository.EmployeeRepository
import com.example.smartschedule.domain.usecase.AddEmployeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeListViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val addEmployeeUseCase: AddEmployeeUseCase
): ViewModel()  {

    private val _state = MutableStateFlow(EmployeeListState())
    val state: StateFlow<EmployeeListState> = _state.asStateFlow()

    // Internal flows for reactive search
    private val _allEmployees = MutableStateFlow<List<Employee>>(emptyList())
    private val _searchQuery = MutableStateFlow("")

    init {
        loadEmployees()
        setupSearch()
    }

    @OptIn(FlowPreview::class)
    private fun setupSearch() {
        viewModelScope.launch {
            combine(
                _allEmployees,
                _searchQuery.debounce(300)
            ) { employees, query ->
                if (query.isBlank()) {
                    employees
                } else {
                    employees.filter { employee ->
                        employee.name.contains(query, ignoreCase = true) ||
                                employee.email.contains(query, ignoreCase = true) ||
                                employee.employeeNumber.contains(query, ignoreCase = true) ||
                                employee.type.displayName.contains(query, ignoreCase = true)
                    }
                }
            }.collect { filteredEmployees ->
                _state.value = _state.value.copy(
                    filteredEmployees = filteredEmployees
                )
            }
        }
    }
    fun loadEmployees() {
        Log.d("DEBUG", "🚀 loadEmployees() נקרא")

        viewModelScope.launch {
            Log.d("DEBUG", "🔄 נכנס ל-viewModelScope")

            _state.value = _state.value.copy(
                isLoading = true,
                errorMessage = null
            )

            Log.d("DEBUG", "✅ isLoading הוגדר ל-true")

            try {
                Log.d("DEBUG", "📡 קורא ל-employeeRepository.getAllEmployees()")

                val employees = employeeRepository.getAllEmployees().first()

                Log.d("DEBUG", "📦 קיבל ${employees.size} עובדים")

                _allEmployees.value = employees
                _state.value = _state.value.copy(
                    employees = employees,
                    isLoading = false,
                    errorMessage = null
                )

                Log.d("DEBUG", "✅ isLoading הוגדר ל-false")

            } catch (e: Exception) {
                Log.e("DEBUG", "❌ Exception: ${e.message}", e)
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "שגיאה בטעינת עובדים: ${e.message}"
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        _state.value = _state.value.copy(searchQuery = query)
    }

    fun clearSearch() {
        updateSearchQuery("")
    }



    fun refreshEmployees() {
        Log.d("REFRESH_DEBUG", "🔄 refreshEmployees() נקרא")

        _state.value = _state.value.copy(isRefreshing = true)
        Log.d("REFRESH_DEBUG", "✅ isRefreshing הוגדר ל-true")

        viewModelScope.launch {
            Log.d("REFRESH_DEBUG", "🚀 נכנס ל-viewModelScope של refresh")

            try {
                Log.d("REFRESH_DEBUG", "📡 קורא ל-employeeRepository.getAllEmployees().first()")

                val employees = employeeRepository.getAllEmployees().first()

                Log.d("REFRESH_DEBUG", "📦 Refresh קיבל ${employees.size} עובדים")

                _allEmployees.value = employees
                Log.d("REFRESH_DEBUG", "🔄 _allEmployees עודכן")

                _state.value = _state.value.copy(
                    employees = employees,
                    isRefreshing = false,
                    errorMessage = null
                )

                Log.d("REFRESH_DEBUG", "✅ isRefreshing הוגדר ל-false, refresh הושלם")

            } catch (e: Exception) {
                Log.e("REFRESH_DEBUG", "❌ Exception ב-refresh: ${e.message}", e)

                _state.value = _state.value.copy(
                    isRefreshing = false,
                    errorMessage = "שגיאה ברענון רשימת עובדים"
                )

                Log.d("REFRESH_DEBUG", "🔧 isRefreshing הוגדר ל-false עקב שגיאה")
            }
        }
    }

    fun deleteEmployee(employee: Employee) {
        viewModelScope.launch {
            try {
                employeeRepository.deleteEmployee(employee)
                Log.d("EmployeeViewModel", "עובד נמחק: ${employee.name}")
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = "שגיאה במחיקת עובד: ${e.message}"
                )
                Log.e("EmployeeViewModel", "שגיאה במחיקת עובד", e)
            }
        }
    }

    fun onLoadEmployeesSuccess(employees: List<Employee>) {
        _state.value = _state.value.copy(
            employees = employees
        )
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }



}