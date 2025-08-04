package com.example.smartschedule.core.presentation.user.add_user_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartschedule.core.domain.models.Employee
import com.example.smartschedule.core.domain.models.User
import com.example.smartschedule.core.domain.models.UserStatus
import com.example.smartschedule.core.domain.models.UserType
import com.example.smartschedule.core.domain.repository.EmployeeRepository
import com.example.smartschedule.core.domain.repository.UserRepository
import com.example.smartschedule.core.domain.validation.EmployeeValidation
import com.example.smartschedule.core.domain.validation.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val employeeRepository: EmployeeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddUserState())
    val state: StateFlow<AddUserState> = _state.asStateFlow()



    fun updateName(name: String) {
        _state.value = _state.value.copy(
            name = name,
            nameError = validateName(name).takeIf { _state.value.hasAttemptedSubmit }
        )
        updateFormValidity()
    }

    fun updateEmail(email: String) {
        _state.value = _state.value.copy(
            email = email,
            emailError = validateEmail(email).takeIf { _state.value.hasAttemptedSubmit }
        )

//        // Check email uniqueness with debouncing
//        if (email.isNotBlank() && emailRegex.matches(email)) {
//            checkEmailUniqueness(email)
//        }

        updateFormValidity()
    }

    fun updatePassword(password: String) {
        val currentState = _state.value
        _state.value = currentState.copy(
            password = password,
            passwordError = validatePassword(password).takeIf { currentState.hasAttemptedSubmit },
            confirmPasswordError = validateConfirmPassword(password, currentState.confirmPassword)
                .takeIf { currentState.hasAttemptedSubmit }
        )
        updateFormValidity()
    }

    fun updateConfirmPassword(confirmPassword: String) {
        val currentState = _state.value
        _state.value = currentState.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = validateConfirmPassword(currentState.password, confirmPassword)
                .takeIf { currentState.hasAttemptedSubmit }
        )
        updateFormValidity()
    }

    fun updateEmployeeNumber(employeeNumber: String) {
        _state.value = _state.value.copy(
            employeeNumber = employeeNumber,
            employeeNumberError = validateEmployeeNumber(employeeNumber)
                .takeIf { _state.value.hasAttemptedSubmit }
        )

        // Check employee number uniqueness
        if (employeeNumber.isNotBlank()) {
            checkEmployeeNumberUniqueness(employeeNumber)
        }

        updateFormValidity()
    }

    fun updateMaxShiftsPerWeek(maxShifts: String) {
        _state.value = _state.value.copy(
            maxShiftsPerWeek = maxShifts,
            maxShiftsPerWeekError = validateMaxShifts(maxShifts)
                .takeIf { _state.value.hasAttemptedSubmit }
        )
        updateFormValidity()
    }

    fun updateUserType(userType: UserType) {
        _state.value = _state.value.copy(
            userType = userType,
            isUserTypeDropdownExpanded = false,
            // Reset employee fields if switching to ADMIN
            employeeNumber = if (userType == UserType.ADMIN) "" else _state.value.employeeNumber,
            maxShiftsPerWeek = if (userType == UserType.ADMIN) "0" else _state.value.maxShiftsPerWeek
        )
        updateFormValidity()
    }

    fun updateUserStatus(userStatus: UserStatus) {
        _state.value = _state.value.copy(
            userStatus = userStatus,
            isStatusDropdownExpanded = false
        )
    }

    fun toggleUserTypeDropdown() {
        _state.value = _state.value.copy(
            isUserTypeDropdownExpanded = !_state.value.isUserTypeDropdownExpanded
        )
    }

    fun toggleStatusDropdown() {
        _state.value = _state.value.copy(
            isStatusDropdownExpanded = !_state.value.isStatusDropdownExpanded
        )
    }

    // ============== VALIDATION FUNCTIONS ==============

    private fun validateName(name: String): String? {
        val result = EmployeeValidation.validateName(name)
        return when(result) {
            is ValidationResult.Success -> null
            is ValidationResult.Error -> result.message
        }
    }

    private fun validateEmail(email: String): String? {
        val result = EmployeeValidation.validateEmail(email)
        return when(result) {
            is ValidationResult.Success -> null
            is ValidationResult.Error -> result.message
        }
    }

    private fun validatePassword(password: String): String? {
        val result = EmployeeValidation.validatePassword(password)
        return when(result) {
            is ValidationResult.Success -> null
            is ValidationResult.Error -> result.message
        }
    }

    private fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        val result = EmployeeValidation.validateConfirmPassword(password, confirmPassword)
        return when(result) {
            is ValidationResult.Success -> null
            is ValidationResult.Error -> result.message
        }
    }

    private fun validateEmployeeNumber(employeeNumber: String): String? {
        if (_state.value.userType == UserType.ADMIN) return null
        val result = EmployeeValidation.validateEmployeeNumber(employeeNumber)
        return when(result) {
            is ValidationResult.Success -> null
            is ValidationResult.Error -> result.message
        }
    }

    private fun validateMaxShifts(maxShifts: String): String? {
        if (_state.value.userType == UserType.ADMIN) return null
        val intValue = maxShifts.toIntOrNull() ?: 0
        val result = EmployeeValidation.validateMaxShiftPerWeek(intValue)
        return when(result) {
            is ValidationResult.Success -> null
            is ValidationResult.Error -> result.message
        }
    }

    // ============== ASYNC VALIDATION ==============

    private fun checkEmailUniqueness(email: String) {
        //TODO: check email uniqueness
    }

    private fun checkEmployeeNumberUniqueness(employeeNumber: String) {
    //TODO: check employee number uniqueness
    }

    // ============== FORM VALIDATION ==============

    private fun updateFormValidity() {
        val currentState = _state.value
        val isValid = validateForm(currentState)
        _state.value = currentState.copy(isFormValid = isValid)
    }

    private fun validateForm(state: AddUserState): Boolean {
        val basicValidations = listOf(
            validateName(state.name),
            validateEmail(state.email),
            validatePassword(state.password),
            validateConfirmPassword(state.password, state.confirmPassword)
        ).all { it == null }

        val employeeValidations = if (state.showEmployeeFields) {
            listOf(
                validateEmployeeNumber(state.employeeNumber),
                validateMaxShifts(state.maxShiftsPerWeek)
            ).all { it == null }
        } else {
            true
        }

        val notBusy = !state.isValidating && !state.isSubmitting

        return basicValidations && employeeValidations && notBusy
    }


    fun submitForm(): Boolean {
        _state.value = _state.value.copy(hasAttemptedSubmit = true)

        val currentState = _state.value
        val allErrors = mapOf(
            "name" to validateName(currentState.name),
            "email" to validateEmail(currentState.email),
            "password" to validatePassword(currentState.password),
            "confirmPassword" to validateConfirmPassword(currentState.password, currentState.confirmPassword),
            "employeeNumber" to if (currentState.showEmployeeFields) validateEmployeeNumber(currentState.employeeNumber) else null,
            "maxShifts" to if (currentState.showEmployeeFields) validateMaxShifts(currentState.maxShiftsPerWeek) else null
        )

        _state.value = currentState.copy(
            nameError = allErrors["name"],
            emailError = allErrors["email"],
            passwordError = allErrors["password"],
            confirmPasswordError = allErrors["confirmPassword"],
            employeeNumberError = allErrors["employeeNumber"],
            maxShiftsPerWeekError = allErrors["maxShifts"]
        )

        updateFormValidity()

        return _state.value.isFormValid
    }

    fun saveUser(onSuccess: (User) -> Unit) {
        if (!submitForm()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isSubmitting = true,
                submissionError = null
            )

            try {
                val currentState = _state.value
                val newUser = createUserFromState(currentState)

                val success = userRepository.registerUser(newUser, currentState.password)

                if (success) {
                    if (newUser is Employee) {
                        employeeRepository.insertEmployee(newUser)
                    }

                    _state.value = currentState.copy(
                        isSubmitting = false,
                        submissionSuccess = true
                    )

                    onSuccess(newUser)
                } else {
                    _state.value = currentState.copy(
                        isSubmitting = false,
                        submissionError = "שגיאה ביצירת המשתמש. נסה שוב."
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSubmitting = false,
                    submissionError = "שגיאה ביצירת המשתמש: ${e.message}"
                )
            }
        }
    }

    private fun createUserFromState(state: AddUserState): User {
        val userId = generateUserId()

        return if (state.userType == UserType.ADMIN) {
            User(
                id = userId,
                name = state.name,
                email = state.email,
                type = state.userType,
                status = state.userStatus
            )
        } else {
            Employee(
                id = userId,
                name = state.name,
                email = state.email,
                employeeNumber = state.employeeNumber,
                maxShiftPerWeek = state.maxShiftsPerWeek.toIntOrNull() ?: 5,
                type = state.userType,
                status = state.userStatus
            )
        }
    }

    private fun generateUserId(): String {
        return "user_${UUID.randomUUID().toString()}"
    }


    fun clearSubmissionError() {
        _state.value = _state.value.copy(submissionError = null)
    }

    fun resetForm() {
        _state.value = AddUserState()
    }
}