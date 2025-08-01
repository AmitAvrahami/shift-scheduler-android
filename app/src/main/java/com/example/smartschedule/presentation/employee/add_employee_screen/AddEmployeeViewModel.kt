package com.example.smartschedule.presentation.employee.add_employee_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartschedule.domain.models.Employee
import com.example.smartschedule.domain.models.UserStatus
import com.example.smartschedule.domain.models.UserType
import com.example.smartschedule.domain.repository.EmployeeRepository
import com.example.smartschedule.domain.usecase.AddEmployeeUseCase
import com.example.smartschedule.domain.validation.EmployeeValidation
import com.example.smartschedule.domain.validation.ValidationResult
import com.example.smartschedule.presentation.common.ErrorState
import com.example.smartschedule.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.smartschedule.domain.common.fold
import com.example.smartschedule.domain.common.toUserFriendlyMessage
import com.example.smartschedule.domain.errors.employee_error.EmployeeError
import com.example.smartschedule.domain.errors.user_error.UserError
@HiltViewModel
class AddEmployeeViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val addEmployeeUseCase: AddEmployeeUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<UiState<Employee>>(UiState.Loading)
    val uiState: StateFlow<UiState<Employee>> = _uiState.asStateFlow()

    private val _state = MutableStateFlow<AddEmployeeState>(AddEmployeeState())
    val state: StateFlow<AddEmployeeState> = _state.asStateFlow()

    val isFormValid: Boolean
        get() = with(state.value) {
            name.isNotBlank() &&
                    employeeNumber.isNotBlank() &&
                    email.isNotBlank() &&
                    password.isNotBlank() &&
                    confirmPassword.isNotBlank() &&
                    maxShiftsPerWeek.isNotBlank() &&
                    nameError == null &&
                    employeeNumberError == null &&
                    emailError == null &&
                    passwordError == null &&
                    confirmPasswordError == null &&
                    maxShiftsPerWeekError == null &&
                    !isSubmitting
        }

    fun addEmployee() {
        if (!isFormValid) return

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isSubmitting = true,
                submissionError = null
            )

            try {
                val currentState = state.value
                val employee = Employee(
                    id = "", // Will be generated by use case
                    name = currentState.name,
                    email = currentState.email,
                    employeeNumber = currentState.employeeNumber,
                    maxShiftPerWeek = currentState.maxShiftsPerWeek.toIntOrNull() ?: 5,
                    type = currentState.userType,
                    status = currentState.status
                )

                val isSuccess = addEmployeeUseCase(employee, currentState.password)

                if (isSuccess) {
                    _state.value = _state.value.copy(
                        isSubmitting = false,
                        submissionSuccess = true
                    )
                    _uiState.value = UiState.Success(employee)
                    Log.d("AddEmployeeViewModel", "עובד נוסף בהצלחה: ${employee.name}")
                } else {
                    _state.value = _state.value.copy(
                        isSubmitting = false,
                        submissionError = "שגיאה ביצירת העובד"
                    )
                    _uiState.value = UiState.Error(ErrorState.DatabaseError)
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSubmitting = false,
                    submissionError = "שגיאה לא צפויה: ${e.message}"
                )
                _uiState.value = UiState.Error(ErrorState.UnknownError(e.message ?: ""))
                Log.e("AddEmployeeViewModel", "שגיאה בהוספת עובד", e)
            }
        }
    }

    fun addEmployeeWithResult() {
        if (!isFormValid) return

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isSubmitting = true,
                submissionError = null
            )

            try {
                val currentState = state.value
                val employee = Employee(
                    id = "", // Will be generated by repository
                    name = currentState.name,
                    email = currentState.email,
                    employeeNumber = currentState.employeeNumber,
                    maxShiftPerWeek = currentState.maxShiftsPerWeek.toIntOrNull() ?: 5,
                    type = currentState.userType,
                    status = currentState.status
                )

                // Use the new Result-based function
                addEmployeeUseCase.executeWithResult(employee, currentState.password).fold(
                    onSuccess = { insertedEmployee ->
                        _state.value = _state.value.copy(
                            isSubmitting = false,
                            submissionSuccess = true
                        )
                        _uiState.value = UiState.Success(insertedEmployee)
                        Log.d("AddEmployeeViewModel", "עובד נוסף בהצלחה עם Result: ${insertedEmployee.name}")
                    },
                    onError = { error ->
                        val userFriendlyMessage = when (error) {
                            is UserError -> error.toUserFriendlyMessage()
                            else -> "שגיאה לא צפויה: ${error.message}"
                        }

                        _state.value = _state.value.copy(
                            isSubmitting = false,
                            submissionError = userFriendlyMessage
                        )
                        _uiState.value = UiState.Error(ErrorState.UnknownError(error.message ?: ""))
                        Log.e("AddEmployeeViewModel", "שגיאה בהוספת עובד עם Result", error)
                    }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSubmitting = false,
                    submissionError = "שגיאה לא צפויה: ${e.message}"
                )
                _uiState.value = UiState.Error(ErrorState.UnknownError(e.message ?: ""))
                Log.e("AddEmployeeViewModel", "Exception בהוספת עובד עם Result", e)
            }
        }
    }


    fun clearSubmissionSuccess() {
        _state.value = _state.value.copy(submissionSuccess = false)
    }

    fun clearSubmissionError() {
        _state.value = _state.value.copy(submissionError = null)
    }



    fun updateName(value: String) {
        _state.value = _state.value.copy(
            name = value,
            nameError = when (val result = EmployeeValidation.validateName(value)) {
                is ValidationResult.Error -> result.message
                is ValidationResult.Success -> null
            }
        )
    }

    fun updateEmployeeNumber(value: String) {
        _state.value = _state.value.copy(
            employeeNumber = value,
            employeeNumberError = when (val result = EmployeeValidation.validateEmployeeNumber(value)) {
                is ValidationResult.Error -> result.message
                is ValidationResult.Success -> null
            }
        )
    }

    fun updateEmail(value: String) {
        _state.value = _state.value.copy(
            email = value,
            emailError = when (val result = EmployeeValidation.validateEmail(value)) {
                is ValidationResult.Error -> result.message
                is ValidationResult.Success -> null
            }
        )
    }

    fun updatePassword(value: String) {
        _state.value = _state.value.copy(
            password = value,
            passwordError = when (val result = EmployeeValidation.validatePassword(value)) {
                is ValidationResult.Error -> result.message
                is ValidationResult.Success -> null
            }
        )

        if (state.value.confirmPassword.isNotEmpty()) {
            updateConfirmPassword(state.value.confirmPassword)
        }
    }
    fun updateConfirmPassword(value: String) {
        _state.value = _state.value.copy(
            confirmPassword = value,
            confirmPasswordError = when {
                value.isBlank() -> "אישור סיסמה נדרש"
                value != state.value.password -> "הסיסמאות לא תואמות"
                else -> null
            }
        )
    }

    fun updateMaxShiftsPerWeek(value: String) {
            val intValue = value.toIntOrNull() ?: 0
            _state.value = _state.value.copy(
                maxShiftsPerWeek = value,
                maxShiftsPerWeekError = when (val result = EmployeeValidation.validateMaxShiftPerWeek(intValue)) {
                    is ValidationResult.Error -> result.message
                    is ValidationResult.Success -> null
                }
            )
    }

    fun updateUserType(userType: UserType) {
        _state.value = _state.value.copy(
            userType = userType,
            isUserTypeDropdownExpanded = false
        )
    }

    fun updateUserTypeDropdownExpanded(expanded: Boolean) {
        _state.value = _state.value.copy(isUserTypeDropdownExpanded = expanded)
    }

    fun updateStatus(status: UserStatus) {
        _state.value = _state.value.copy(
            status = status,
            isStatusDropdownExpanded = false
        )
    }

    fun updateStatusDropdownExpanded(expanded: Boolean) {
        _state.value = _state.value.copy(isStatusDropdownExpanded = expanded)
    }




}