package com.example.smartschedule.presentation.employee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartschedule.domain.models.Employee
import com.example.smartschedule.domain.models.UserType
import com.example.smartschedule.domain.models.UserStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployeeScreen(
    modifier: Modifier = Modifier,
    onSaveClick: (Employee, String) -> Unit = { _, _ -> }, // הוספנו פרמטר סיסמה
    onBackClick: () -> Unit = {},
    employeeNumberError: String? = null,
    onEmployeeNumberChanged: (String) -> Unit = {},
    passwordError: String? = null // validation לסיסמה
) {
    // שדות בסיסיים
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var employeeNumber by remember { mutableStateOf("") }
    var maxShiftsPerWeek by remember { mutableStateOf("5") } // ברירת מחדל

    // שדות סיסמה
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // UserType Dropdown
    var userTypeExpanded by remember { mutableStateOf(false) }
    var selectedUserType by remember { mutableStateOf(UserType.EMPLOYEE) }

    // Status Dropdown
    var statusExpanded by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf(UserStatus.ACTIVE) }

    // Validation
    val passwordsMatch = password == confirmPassword
    val isFormValid = name.isNotBlank() &&
            email.isNotBlank() &&
            employeeNumber.isNotBlank() &&
            password.isNotBlank() &&
            passwordsMatch &&
            maxShiftsPerWeek.toIntOrNull() != null &&
            employeeNumberError == null &&
            passwordError == null

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

        // שם מלא
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("שם מלא") },
            modifier = Modifier.fillMaxWidth()
        )

        // מספר עובד
        OutlinedTextField(
            value = employeeNumber,
            onValueChange = { newNumber ->
                employeeNumber = newNumber
                onEmployeeNumberChanged(newNumber)
            },
            label = { Text("מספר עובד") },
            isError = employeeNumberError != null,
            supportingText = employeeNumberError?.let {
                { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // אימייל
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("כתובת מייל") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        // סיסמה
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("סיסמה") },
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            supportingText = passwordError?.let {
                { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // אישור סיסמה
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("אישור סיסמה") },
            visualTransformation = PasswordVisualTransformation(),
            isError = !passwordsMatch && confirmPassword.isNotBlank(),
            supportingText = if (!passwordsMatch && confirmPassword.isNotBlank()) {
                { Text("הסיסמאות לא תואמות", color = MaterialTheme.colorScheme.error) }
            } else null,
            modifier = Modifier.fillMaxWidth()
        )

        // כמות משמרות מקסימלית
        OutlinedTextField(
            value = maxShiftsPerWeek,
            onValueChange = { newValue ->
                // רק מספרים
                if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                    maxShiftsPerWeek = newValue
                }
            },
            label = { Text("כמות משמרות מקסימלית בשבוע") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // תפקיד
        ExposedDropdownMenuBox(
            expanded = userTypeExpanded,
            onExpandedChange = { userTypeExpanded = it }
        ) {
            OutlinedTextField(
                value = selectedUserType.displayName,
                onValueChange = {},
                readOnly = true,
                label = { Text("תפקיד") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = userTypeExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = userTypeExpanded,
                onDismissRequest = { userTypeExpanded = false }
            ) {
                UserType.values().forEach { userType ->
                    DropdownMenuItem(
                        text = { Text(userType.displayName) },
                        onClick = {
                            selectedUserType = userType
                            userTypeExpanded = false
                        }
                    )
                }
            }
        }

        // סטטוס
        ExposedDropdownMenuBox(
            expanded = statusExpanded,
            onExpandedChange = { statusExpanded = it }
        ) {
            OutlinedTextField(
                value = selectedStatus.displayName,
                onValueChange = {},
                readOnly = true,
                label = { Text("סטטוס") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = statusExpanded,
                onDismissRequest = { statusExpanded = false }
            ) {
                UserStatus.values().forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status.displayName) },
                        onClick = {
                            selectedStatus = status
                            statusExpanded = false
                        }
                    )
                }
            }
        }

        // כפתורי פעולה
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
                    val newEmployee = Employee(
                        id = "", // יטופל ב-ViewModel
                        name = name,
                        email = email,
                        employeeNumber = employeeNumber,
                        maxShiftPerWeek = maxShiftsPerWeek.toIntOrNull() ?: 5,
                        type = selectedUserType,
                        status = selectedStatus
                    )
                    onSaveClick(newEmployee, password)
                },
                modifier = Modifier.weight(1f),
                enabled = isFormValid
            ) {
                Text("שמור")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddEmployeeScreenPreview() {
    AddEmployeeScreen(
        employeeNumberError = "מספר עובד כבר קיים במערכת",
        passwordError = "סיסמה חייבת להיות לפחות 6 תווים"
    )
}