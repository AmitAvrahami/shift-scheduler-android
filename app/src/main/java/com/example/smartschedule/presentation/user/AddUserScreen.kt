package com.example.smartschedule.presentation.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartschedule.domain.models.Employee
import com.example.smartschedule.domain.models.User
import com.example.smartschedule.domain.models.UserType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(
    modifier: Modifier = Modifier,
    currentUser: User,
    onSaveClick: (User, String) -> Unit = { _, _ -> },
    onBackClick: () -> Unit = {}
) {
    //UI States
    var selectedUserType by remember { mutableStateOf(UserType.EMPLOYEE) }
    var expanded by remember { mutableStateOf(false) }

    //User Data
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var employeeNumber by remember { mutableStateOf("") }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "הוספת משתמש חדש",
            style = MaterialTheme.typography.titleMedium
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            OutlinedTextField(
                value = selectedUserType.displayName,
                onValueChange = { },
                readOnly = true,
                label = { Text("תפקיד") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                if (currentUser.type.canManageEmployees()) {
                    DropdownMenuItem(
                        text = { Text(UserType.EMPLOYEE.displayName) },
                        onClick = {
                            selectedUserType = UserType.EMPLOYEE
                            expanded = false
                        }
                    )
                }

                if (currentUser.type.canManageEmployees()) {
                    DropdownMenuItem(
                        text = { Text(UserType.MANAGER.displayName) },
                        onClick = {
                            selectedUserType = UserType.MANAGER
                            expanded = false
                        }
                    )
                }

                if (currentUser.type.canCreateManagers()) {
                    DropdownMenuItem(
                        text = { Text(UserType.ADMIN.displayName) },
                        onClick = {
                            selectedUserType = UserType.ADMIN
                            expanded = false
                        }
                    )
                }
            }
        }

        Text("נבחר: ${selectedUserType.displayName}")

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("שם מלא") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("כתובת אימייל") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("סיסמה") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        if (selectedUserType == UserType.EMPLOYEE || selectedUserType == UserType.MANAGER) {
            OutlinedTextField(
                value = employeeNumber,
                onValueChange = { employeeNumber = it },
                label = { Text("מספר עובד") },
                placeholder = { Text("12345") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("ביטול")
            }

            Button(
                onClick = {
                    val newUser = if (selectedUserType == UserType.ADMIN) {
                        User(
                            id = "",
                            name = name,
                            email = email,
                            type = selectedUserType
                        )
                    } else {
                        Employee(
                            id = "",
                            name = name,
                            email = email,
                            employeeNumber = employeeNumber,
                            type = selectedUserType
                        )
                    }
                    onSaveClick(newUser, password)
                },
                modifier = Modifier.weight(1f),
                enabled = isFormValid(
                    name = name,
                    email = email,
                    password = password,
                    employeeNumber = employeeNumber,
                    userType = selectedUserType
                )
            ) {
                Text("שמור")
            }
        }
    }
}

private fun isFormValid(
    name: String,
    email: String,
    password: String,
    employeeNumber: String,
    userType: UserType
): Boolean {
    if (name.isBlank() || email.isBlank() || password.isBlank()) {
        return false
    }
    if (userType == UserType.EMPLOYEE || userType == UserType.MANAGER) {
        return employeeNumber.isNotBlank()
    }
    return true
}


@Preview(showBackground = true)
@Composable
fun AddUserScreenPreview() {
    val currentUser = User(
        id = "1",
        name = "שאול ברנע",
        email = "employe@example.com",
        type = UserType.EMPLOYEE,
    )
    AddUserScreen(
        currentUser = currentUser
    )
}