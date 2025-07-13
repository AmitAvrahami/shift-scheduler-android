package com.example.smartschedule.presentation.shift

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartschedule.domain.models.ShiftType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShiftScreen(
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
){
    var selectedShiftType by remember { mutableStateOf(ShiftType.MORNING) }
    var expanded by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf("15/01/2025") }
    var startTime by remember { mutableStateOf("06:45") }
    var endDate by remember { mutableStateOf("15/01/2025") }
    var endTime by remember { mutableStateOf("14:45") }
    var selectedEmployee by remember { mutableStateOf<String?>(null) }
    var employeeExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "הוספת משמרת חדשה",
            style = MaterialTheme.typography.titleMedium
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedShiftType.displayName,
                onValueChange = { },
                readOnly = true,
                label = { Text("סוג משמרת") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(ShiftType.MORNING.displayName) },
                    onClick = {
                        selectedShiftType = ShiftType.MORNING
                        expanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text(ShiftType.AFTERNOON.displayName) },
                    onClick = {
                        selectedShiftType = ShiftType.AFTERNOON
                        expanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text(ShiftType.NIGHT.displayName) },
                    onClick = {
                        selectedShiftType = ShiftType.NIGHT
                        expanded = false
                    }
                )
            }

        }

        OutlinedTextField(
            value = startDate,
            onValueChange = { startDate = it },
            label = { Text("תאריך התחלה") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = startTime,
            onValueChange = { startTime = it },
            label = { Text("שעת התחלה") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = endDate,
            onValueChange = { endDate = it },
            label = { Text("תאריך סיום") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = endTime,
            onValueChange = { endTime = it },
            label = { Text("שעת סיום") },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = employeeExpanded,
            onExpandedChange = { employeeExpanded = it }
        ){
            OutlinedTextField(
                value = selectedEmployee ?: "לא נבחר",
                onValueChange = { },
                readOnly = true,
                label = { Text("עובד משובץ") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = employeeExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = employeeExpanded,
                onDismissRequest = { employeeExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("עמית אברהמי") },
                    onClick = {
                        selectedEmployee = "עמית אברהמי"
                        employeeExpanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("שרה כהן") },
                    onClick = {
                        selectedEmployee = "שרה כהן"
                        employeeExpanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("ללא שיבוץ") },
                    onClick = {
                        selectedEmployee = null
                        employeeExpanded = false
                    }
                )
            }
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
                onClick = onSaveClick,
                modifier = Modifier.weight(1f),
                enabled = startDate.isNotBlank() && startTime.isNotBlank() &&
                        endDate.isNotBlank() && endTime.isNotBlank()
            ) {
                Text("שמור")
            }
        }
    }

}



@Preview(showBackground = true)
@Composable
fun AddShiftScreenPreview() {
    AddShiftScreen()
}