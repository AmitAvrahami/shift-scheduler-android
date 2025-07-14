package com.example.smartschedule.presentation.employee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartschedule.domain.models.Employee

@Composable
fun EditEmployeeScreen(
    employee: Employee,
    onSaveClick: (Employee) -> Unit = { },
    onBackClick: () -> Unit = { }
){
    var name by remember { mutableStateOf(employee.name) }
    var email by remember { mutableStateOf(employee.email) }
    var employeeNumber by remember { mutableStateOf(employee.employeeNumber) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "עריכת עובד",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = name,
            onValueChange = {name = it},
            label = {Text("שם מלא")},
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = employeeNumber,
            onValueChange = {employeeNumber = it},
            label = {Text("מספר עובד")},
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = {Text("כתובת מייל")},
            modifier = Modifier.fillMaxWidth()
        )

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
                    val updatedEmployee = employee.copy(
                        name = name,
                        email = email,
                        employeeNumber = employeeNumber
                    )
                    onSaveClick(updatedEmployee)
                },
                modifier = Modifier.weight(1f),
                enabled = name.isNotBlank() && email.isNotBlank() && employeeNumber.isNotBlank()
            ) {
                Text("שמור שינויים")

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EditEmployeeScreenPreview() {
    val sampleEmployee = Employee(
        id = "1",
        name = "עמית אברהמי",
        email = "amit@example.com",
        employeeNumber = "12345"
    )

    EditEmployeeScreen(employee = sampleEmployee)
}