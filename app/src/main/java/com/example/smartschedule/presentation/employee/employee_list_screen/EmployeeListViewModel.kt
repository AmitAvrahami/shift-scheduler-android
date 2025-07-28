package com.example.smartschedule.presentation.employee.employee_list_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartschedule.domain.common.fold
import com.example.smartschedule.domain.errors.employee_error.EmployeeError
import com.example.smartschedule.domain.models.Employee
import com.example.smartschedule.domain.repository.EmployeeRepository
import com.example.smartschedule.domain.usecase.AddEmployeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
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

    // ✅ Load employees with Result handling
    fun loadEmployees() {
        Log.d("EmployeeViewModel", "🚀 loadEmployees() called")

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                employeeRepository.getAllEmployees()
                    .collect { result ->
                        result.fold(
                            onSuccess = { employees ->
                                Log.d("EmployeeViewModel", "✅ Loaded ${employees.size} employees")
                                _allEmployees.value = employees
                                _state.value = _state.value.copy(
                                    employees = employees,
                                    isLoading = false,
                                    errorMessage = null
                                )
                            },
                            onError = { error ->
                                Log.e("EmployeeViewModel", "❌ Failed to load employees", error)
                                _state.value = _state.value.copy(
                                    isLoading = false,
                                    errorMessage = error.toUserFriendlyMessage()
                                )
                            }
                        )
                    }
            } catch (e: Exception) {
                Log.e("EmployeeViewModel", "❌ Exception in loadEmployees", e)
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "שגיאה בטעינת עובדים: ${e.message}"
                )
            }
        }
    }




    fun refreshEmployees() {
        Log.d("EmployeeViewModel", "🔄 refreshEmployees() called")

        val startTime = System.currentTimeMillis()
        _state.value = _state.value.copy(isRefreshing = true, errorMessage = null)

        viewModelScope.launch {
            try {
                employeeRepository.refreshEmployees().fold(
                    onSuccess = { employees ->
                        Log.d("EmployeeViewModel", "✅ Refresh completed - ${employees.size} employees")

                        // Ensure minimum loading duration for UX
                        val elapsed = System.currentTimeMillis() - startTime
                        if (elapsed < MIN_LOADING_DURATION) {
                            delay(MIN_LOADING_DURATION - elapsed)
                        }

                        _allEmployees.value = employees
                        _state.value = _state.value.copy(
                            employees = employees,
                            isRefreshing = false,
                            errorMessage = null
                        )
                    },
                    onError = { error ->
                        Log.e("EmployeeViewModel", "❌ Refresh failed", error)
                        _state.value = _state.value.copy(
                            isRefreshing = false,
                            errorMessage = error.toUserFriendlyMessage()
                        )
                    }
                )
            } catch (e: Exception) {
                Log.e("EmployeeViewModel", "❌ Exception in refreshEmployees", e)
                _state.value = _state.value.copy(
                    isRefreshing = false,
                    errorMessage = "שגיאה ברענון רשימת עובדים"
                )
            }
        }
    }


    companion object {
        private const val MIN_LOADING_DURATION = 500L
    }

    // ✅ Delete employee with Result Pattern
    fun deleteEmployee(employee: Employee) {
        viewModelScope.launch {
            employeeRepository.deleteEmployee(employee).fold(
                onSuccess = {
                    Log.d("EmployeeViewModel", "✅ Employee deleted: ${employee.name}")
                    // Success - the Flow will automatically update the UI
                },
                onError = { error ->
                    Log.e("EmployeeViewModel", "❌ Failed to delete employee", error)
                    _state.value = _state.value.copy(
                        errorMessage = error.toUserFriendlyMessage()
                    )
                }
            )
        }
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

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        _state.value = _state.value.copy(searchQuery = query)
    }

    fun clearSearch() {
        updateSearchQuery("")
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }


// File: presentation/common/ErrorExtensions.kt

    fun Throwable.toUserFriendlyMessage(): String {
        return when (this) {
            is EmployeeError.DatabaseCorrupted ->
                "מסד הנתונים פגום, נסה לאתחל את האפליקציה"

            is EmployeeError.EmployeeNotFound ->
                "העובד לא נמצא במערכת"

            is EmployeeError.DuplicateEmployeeNumber ->
                "מספר עובד ${this.employeeNumber} כבר קיים במערכת"

            is EmployeeError.ValidationError ->
                "${this.field}: ${this.reason}"

            is EmployeeError.DatabaseError ->
                "שגיאת מסד נתונים, נסה שוב"

            is EmployeeError.NetworkUnavailable ->
                "אין חיבור לאינטרנט"

            is EmployeeError.ServerError ->
                "שגיאת שרת (${this.code}), נסה שוב מאוחר יותר"

            else ->
                "שגיאה לא צפויה: ${this.message}"
        }
    }
}