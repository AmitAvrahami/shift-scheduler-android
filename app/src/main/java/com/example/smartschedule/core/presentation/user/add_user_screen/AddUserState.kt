package com.example.smartschedule.core.presentation.user.add_user_screen

import com.example.smartschedule.core.domain.models.UserStatus
import com.example.smartschedule.core.domain.models.UserType
data class AddUserState(
    // Basic Info
    val name: String = "",
    val nameError: String? = null,

    val email: String = "",
    val emailError: String? = null,

    val password: String = "",
    val passwordError: String? = null,

    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,

    // Employee specific fields
    val employeeNumber: String = "",
    val employeeNumberError: String? = null,

    val maxShiftsPerWeek: String = "5",
    val maxShiftsPerWeekError: String? = null,

    // User type and status
    val userType: UserType = UserType.EMPLOYEE,
    val userStatus: UserStatus = UserStatus.ACTIVE,

    // UI States
    val isUserTypeDropdownExpanded: Boolean = false,
    val isStatusDropdownExpanded: Boolean = false,

    // Submission states
    val isSubmitting: Boolean = false,
    val isValidating: Boolean = false,
    val submissionError: String? = null,
    val submissionSuccess: Boolean = false,

    // Form validation
    val isFormValid: Boolean = false,
    val hasAttemptedSubmit: Boolean = false
) {
    // Helper computed properties
    val showEmployeeFields: Boolean = userType == UserType.EMPLOYEE || userType == UserType.MANAGER
    val hasAnyError: Boolean = listOf(
        nameError, emailError, passwordError, confirmPasswordError,
        employeeNumberError, maxShiftsPerWeekError, submissionError
    ).any { it != null }
}
