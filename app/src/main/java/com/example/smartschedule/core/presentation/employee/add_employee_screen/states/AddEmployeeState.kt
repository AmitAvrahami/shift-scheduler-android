package com.example.smartschedule.core.presentation.employee.add_employee_screen.states

import com.example.smartschedule.core.domain.models.Employee
import com.example.smartschedule.core.domain.models.UserStatus
import com.example.smartschedule.core.domain.models.UserType
import com.example.smartschedule.core.presentation.common.UiState

@Deprecated(
    "Use NewAddEmployeeState instead, this is just for reference",
    ReplaceWith("NewAddEmployeeState"),
    DeprecationLevel.WARNING
)
data class AddEmployeeState(

    val name: String = "",
    val nameError: String? = null,

    val employeeNumber: String = "",
    val employeeNumberError: String? = null,

    val email: String = "",
    val emailError: String? = null,

    val password: String = "",
    val passwordError: String? = null,

    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,

    val maxShiftsPerWeek: String = "5",
    val maxShiftsPerWeekError: String? = null,

    val userType: UserType = UserType.EMPLOYEE,
    val isUserTypeDropdownExpanded: Boolean = false,

    val status: UserStatus = UserStatus.ACTIVE,
    val isStatusDropdownExpanded: Boolean = false,

    val isSubmitting: Boolean = false,
    val submissionError: String? = null,
    val submissionSuccess: Boolean = false
)
