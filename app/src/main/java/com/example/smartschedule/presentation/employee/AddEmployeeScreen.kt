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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartschedule.data.repository.EmployeeRepository
import com.example.smartschedule.domain.models.Employee

@Composable
fun AddEmployeeScreen(
    onSaveClick:(Employee)-> Unit = {},
    onBackClick: () -> Unit = {},
    employeeNumberError : String? = null,
    onEmployeeNumberChanged : (String) -> Unit = {}
){
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var employeeNumber by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "הוספת עובד חדש",
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
            onValueChange = { newNumber ->
                employeeNumber = newNumber
                onEmployeeNumberChanged(newNumber)
            },
            label = {Text("מספר עובד")},
            isError = employeeNumberError != null,
            supportingText = employeeNumberError?.let {
                { Text(it, color = MaterialTheme.colorScheme.error) }
            },
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
            ){
                Text("ביטול")
            }
            Button(
                onClick = {
                    val newEmployee = Employee(
                        id = "", //TODO : handle this
                        name = name,
                        email = email,
                        employeeNumber = employeeNumber
                    )
                    onSaveClick(newEmployee)
                },
                modifier = Modifier.weight(1f),
                enabled = name.isNotBlank() &&
                        email.isNotBlank() &&
                        employeeNumber.isNotBlank() &&
                        employeeNumberError == null) {
                Text("שמור")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddEmployeeScreenPreview(){
    AddEmployeeScreen(
        employeeNumberError = "מספר עובד כבר קיים במערכת",
        onEmployeeNumberChanged = {}
    )
}