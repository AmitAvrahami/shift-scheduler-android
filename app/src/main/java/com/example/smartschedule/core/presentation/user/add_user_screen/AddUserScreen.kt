package com.example.smartschedule.core.presentation.user.add_user_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartschedule.core.domain.models.User
import com.example.smartschedule.core.domain.models.UserStatus
import com.example.smartschedule.core.domain.models.UserType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(
    modifier: Modifier = Modifier,
    currentUser: User,
    onSaveClick: (User) -> Unit = {},
    onBackClick: () -> Unit = {},
    viewModel: AddUserViewModel? = hiltViewModel()
) {
    val state by (viewModel?.state ?: return).collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle submission success
    LaunchedEffect(state.submissionSuccess) {
        if (state.submissionSuccess) {
            onBackClick()
        }
    }

    // Handle submission errors
    LaunchedEffect(state.submissionError) {
        state.submissionError?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearSubmissionError()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text = "הוספת משתמש חדש",
            style = MaterialTheme.typography.titleLarge
        )

        // User Type Dropdown
        ExposedDropdownMenuBox(
            expanded = state.isUserTypeDropdownExpanded,
            onExpandedChange = { viewModel.toggleUserTypeDropdown() }
        ) {
            OutlinedTextField(
                value = state.userType.displayName,
                onValueChange = { },
                readOnly = true,
                label = { Text("תפקיד") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isUserTypeDropdownExpanded)
                },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = state.isUserTypeDropdownExpanded,
                onDismissRequest = { viewModel.toggleUserTypeDropdown() }
            ) {
                if (currentUser.type.canManageEmployees()) {
                    DropdownMenuItem(
                        text = { Text(UserType.EMPLOYEE.displayName) },
                        onClick = { viewModel.updateUserType(UserType.EMPLOYEE) }
                    )

                    DropdownMenuItem(
                        text = { Text(UserType.MANAGER.displayName) },
                        onClick = { viewModel.updateUserType(UserType.MANAGER) }
                    )
                }

                if (currentUser.type.canCreateManagers()) {
                    DropdownMenuItem(
                        text = { Text(UserType.ADMIN.displayName) },
                        onClick = { viewModel.updateUserType(UserType.ADMIN) }
                    )
                }
            }
        }

        // Status Dropdown
        ExposedDropdownMenuBox(
            expanded = state.isStatusDropdownExpanded,
            onExpandedChange = { viewModel.toggleStatusDropdown() }
        ) {
            OutlinedTextField(
                value = state.userStatus.displayName,
                onValueChange = { },
                readOnly = true,
                label = { Text("סטטוס") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isStatusDropdownExpanded)
                },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = state.isStatusDropdownExpanded,
                onDismissRequest = { viewModel.toggleStatusDropdown() }
            ) {
                UserStatus.values().forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status.displayName) },
                        onClick = { viewModel.updateUserStatus(status) }
                    )
                }
            }
        }

        // Name Field
        OutlinedTextField(
            value = state.name,
            onValueChange = viewModel::updateName,
            label = { Text("שם מלא") },
            isError = state.nameError != null,
            supportingText = state.nameError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
            modifier = Modifier.fillMaxWidth()
        )

        // Email Field
        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::updateEmail,
            label = { Text("כתובת אימייל") },
            isError = state.emailError != null,
            supportingText = state.emailError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            trailingIcon = {
                if (state.isValidating) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Password Field
        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::updatePassword,
            label = { Text("סיסמה") },
            isError = state.passwordError != null,
            supportingText = state.passwordError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        // Confirm Password Field
        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = viewModel::updateConfirmPassword,
            label = { Text("אישור סיסמה") },
            isError = state.confirmPasswordError != null,
            supportingText = state.confirmPasswordError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        // Employee-specific fields
        if (state.showEmployeeFields) {
            OutlinedTextField(
                value = state.employeeNumber,
                onValueChange = viewModel::updateEmployeeNumber,
                label = { Text("מספר עובד") },
                isError = state.employeeNumberError != null,
                supportingText = state.employeeNumberError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.maxShiftsPerWeek,
                onValueChange = viewModel::updateMaxShiftsPerWeek,
                label = { Text("כמות משמרות מקסימלית בשבוע") },
                isError = state.maxShiftsPerWeekError != null,
                supportingText = state.maxShiftsPerWeekError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                onClick = {
                    viewModel.saveUser { user ->
                        onSaveClick(user)
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = state.isFormValid && !state.isSubmitting
            ) {
                if (state.isSubmitting) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                        Text("שומר...")
                    }
                } else {
                    Text("שמור")
                }
            }
        }

        // Snackbar for errors
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Preview functions
@Preview(showBackground = true)
@Composable
fun AddUserScreenPreview() {
    val currentUser = User(
        id = "1",
        name = "עמית אברהמי",
        email = "admin@example.com",
        type = UserType.ADMIN
    )

    AddUserScreen(
        currentUser = currentUser,
        viewModel = null
    )
}