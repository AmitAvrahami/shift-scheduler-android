package com.example.smartschedule.presentation.employee.add_employee_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartschedule.domain.models.Employee
import com.example.smartschedule.domain.models.UserType
import com.example.smartschedule.domain.models.UserStatus
import com.example.smartschedule.domain.validation.EmployeeValidation
import com.example.smartschedule.domain.validation.ValidationResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployeeScreen(
    modifier: Modifier = Modifier,
    onSaveSuccess: () -> Unit = {},
    onBackClick: () -> Unit = {},
    viewModel: AddEmployeeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    // Handle submission success
    LaunchedEffect(state.submissionSuccess) {
        if (state.submissionSuccess) {
            onSaveSuccess()
            viewModel.clearSubmissionSuccess()
        }
    }

    // Handle submission error
    LaunchedEffect(state.submissionError) {
        state.submissionError?.let { error ->
            snackBarHostState.showSnackbar(error)
            viewModel.clearSubmissionError()
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "הוספת עובד חדש",
            style = MaterialTheme.typography.titleMedium
        )

        // Name Field
        ValidatedOutlinedTextField(
            value = state.name,
            onValueChange = viewModel::updateName,
            label = "שם מלא",
            errorMessage = state.nameError,
            modifier = Modifier.fillMaxWidth()
        )

        // Employee Number Field
        ValidatedOutlinedTextField(
            value = state.employeeNumber,
            onValueChange = viewModel::updateEmployeeNumber,
            label = "מספר עובד",
            errorMessage = state.employeeNumberError,
            modifier = Modifier.fillMaxWidth()
        )

        // Email Field
        ValidatedOutlinedTextField(
            value = state.email,
            onValueChange = viewModel::updateEmail,
            label = "כתובת מייל",
            errorMessage = state.emailError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        // Password Field
        ValidatedOutlinedTextField(
            value = state.password,
            onValueChange = viewModel::updatePassword,
            label = "סיסמה",
            errorMessage = state.passwordError,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        // Confirm Password Field
        ValidatedOutlinedTextField(
            value = state.confirmPassword,
            onValueChange = viewModel::updateConfirmPassword,
            label = "אישור סיסמה",
            errorMessage = state.confirmPasswordError,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        // Max Shifts Per Week Field
        ValidatedOutlinedTextField(
            value = state.maxShiftsPerWeek,
            onValueChange = viewModel::updateMaxShiftsPerWeek,
            label = "כמות משמרות מקסימלית בשבוע",
            errorMessage = state.maxShiftsPerWeekError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )


        // User Type Dropdown
        ExposedDropdownMenuBox(
            expanded = state.isUserTypeDropdownExpanded,
            onExpandedChange = viewModel::updateUserTypeDropdownExpanded
        ) {
            OutlinedTextField(
                value = state.userType.displayName,
                onValueChange = {},
                readOnly = true,
                label = { Text("תפקיד") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isUserTypeDropdownExpanded)
                },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = state.isUserTypeDropdownExpanded,
                onDismissRequest = { viewModel.updateUserTypeDropdownExpanded(false) }
            ) {
                UserType.entries.forEach { userType ->
                    DropdownMenuItem(
                        text = { Text(userType.displayName) },
                        onClick = { viewModel.updateUserType(userType) }
                    )
                }
            }
        }

        // Status Dropdown
        ExposedDropdownMenuBox(
            expanded = state.isStatusDropdownExpanded,
            onExpandedChange = viewModel::updateStatusDropdownExpanded
        ) {
            OutlinedTextField(
                value = state.status.displayName,
                onValueChange = {},
                readOnly = true,
                label = { Text("סטטוס") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isStatusDropdownExpanded)
                },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = state.isStatusDropdownExpanded,
                onDismissRequest = { viewModel.updateStatusDropdownExpanded(false) }
            ) {
                UserStatus.entries.forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status.displayName) },
                        onClick = { viewModel.updateStatus(status) }
                    )
                }
            }
        }

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.weight(1f),
                enabled = !state.isSubmitting
            ) {
                Text("ביטול")
            }

            Button(
                onClick = viewModel::addEmployee,
                modifier = Modifier.weight(1f),
                enabled = viewModel.isFormValid && !state.isSubmitting
            ) {
                if (state.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(4.dp)
                    )
                } else {
                    Text("שמור")
                }
            }
        }
        SnackbarHost(hostState = snackBarHostState)

    }
}


@Composable
private fun ValidatedOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String?,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = errorMessage != null,
        supportingText = errorMessage?.let {
            { Text(it, color = MaterialTheme.colorScheme.error) }
        },
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun AddEmployeeScreenPreview() {
    AddEmployeeScreen()
}