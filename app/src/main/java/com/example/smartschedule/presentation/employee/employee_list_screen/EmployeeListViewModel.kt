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

    /**
     * Loads the list of employees from the repository.
     *
     * This function initiates an asynchronous operation to fetch all employees.
     * It updates the [EmployeeListState.isLoading] to `true` at the beginning and sets it to `false` upon completion or error.
     * - On success, it updates the [EmployeeListState.employees] and [_allEmployees] with the fetched list
     *   and clears any existing [EmployeeListState.errorMessage].
     * - On failure (either from the repository or an unexpected exception), it updates the
     *   [EmployeeListState.errorMessage] with a user-friendly message.
     *
     * The operation is performed within the [viewModelScope] to ensure it's lifecycle-aware.
     * It collects the `Result` from `employeeRepository.getAllEmployees()` and uses `fold`
     * to handle success and error cases.
     */// ✅ Load employees with Result handling
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




    /**
     * Refreshes the list of employees from the repository.
     *
     * This function initiates a refresh operation to fetch the latest employee data.
     * It updates the UI state to indicate that a refresh is in progress.
     *
     * On success:
     * - Updates the internal `_allEmployees` flow with the new list.
     * - Updates the UI state with the fetched employees, sets `isRefreshing` to false, and clears any error messages.
     * - Ensures a minimum loading duration for a better user experience by delaying if the refresh completes too quickly.
     *
     * On failure (either from the repository or an unexpected exception):
     * - Logs the error.
     * - Updates the UI state by setting `isRefreshing` to false and displaying a user-friendly error message.
     */
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

    /**
     * Deletes an employee from the repository.
     *
     * This function launches a coroutine in the viewModelScope to perform the delete operation
     * asynchronously. It uses the `employeeRepository.deleteEmployee` method, which returns
     * a `Result` object.
     *
     * - On success, it logs a debug message. The UI is expected to update automatically
     *   due to the reactive nature of the underlying data Flow.
     * - On error, it logs an error message and updates the `_state` with a user-friendly
     *   error message, which can then be displayed in the UI.
     *
     * @param employee The [Employee] object to be deleted.
     */// ✅ Delete employee with Result Pattern
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

    /**
     * Sets up a reactive search functionality.
     *
     * This function observes changes in the `_allEmployees` list and the `_searchQuery`.
     * It combines these two flows and, after a debounce period of 300ms for the search query,
     * filters the `_allEmployees` list based on the current query.
     *
     * The filtering logic checks if the query (case-insensitive) is contained within
     * the employee's name, email, employee number, or the display name of their type.
     *
     * If the query is blank, all employees are shown.
     * The resulting filtered list of employees is then used to update the `filteredEmployees`
     * property of the `_state`.
     *
     * This function is typically called during the ViewModel's initialization.
     */
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