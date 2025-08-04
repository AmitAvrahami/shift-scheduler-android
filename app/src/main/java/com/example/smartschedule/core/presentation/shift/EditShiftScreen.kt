package com.example.smartschedule.core.presentation.shift

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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartschedule.core.domain.models.Shift
import com.example.smartschedule.core.domain.models.ShiftType
import com.example.smartschedule.core.domain.validation.ShiftValidation
import java.time.LocalDateTime
import java.time.LocalTime


private fun parseTimeString(timeString: String): LocalTime? {
    return try {
        val parts = timeString.split(":")
        if (parts.size == 2){
            val hours = parts[0].toInt()
            val minutes = parts[1].toInt()
            if(hours in 0..23 && minutes in 0..59){
                LocalTime.of(hours,minutes)
            }else null
        } else null
    } catch (e: NumberFormatException) {
        null
    }
}

private fun createDateTime(originalDateTime: LocalDateTime, time: LocalTime): LocalDateTime {
    return originalDateTime.withHour(time.hour).withMinute(time.minute)
}

private fun validateTimes(startTimeStr: String, endTimeStr: String, originalShift: Shift): String? {
    val startTime = parseTimeString(startTimeStr)
    val endTime = parseTimeString(endTimeStr)

    if (startTime == null) return "פורמט שעת התחלה לא תקין (דוגמה: 06:45)"
    if (endTime == null) return "פורמט שעת סיום לא תקין (דוגמה: 14:45)"

    val startDateTime = createDateTime(originalShift.startTime, startTime)
    val endDateTime = createDateTime(originalShift.endTime, endTime)

    ShiftValidation.validateShiftTimes(startDateTime, endDateTime)?.let { return it }
    ShiftValidation.validateShiftDuration(startDateTime, endDateTime)?.let { return it }

    return null // הכל בסדר
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditShiftScreen(
    shift: Shift,
    onSaveClick: (Shift) -> Unit = {},
    onBackClick: () -> Unit = {}
){

    //shift type variables
    var expanded by remember { mutableStateOf(false) }
    var selectedShiftType by remember { mutableStateOf(shift.shiftType) }


    //employee variables
    var employeeExpanded by remember { mutableStateOf(false) }
    var selectedEmployee by remember { mutableStateOf(shift.assignedEmployeeId) }

    //Validation variables
    var timeValidationError by remember { mutableStateOf<String?>(null) }
    var startTime by remember {
        mutableStateOf(
            "${shift.startTime.hour.toString().padStart(2, '0')}:${shift.startTime.minute.toString().padStart(2, '0')}"
        )
    }
    var endTime by remember {
        mutableStateOf(
            "${shift.endTime.hour.toString().padStart(2, '0')}:${shift.endTime.minute.toString().padStart(2, '0')}"
        )
    }


        Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Text(
            text = "עריכת משמרת",
            style = MaterialTheme.typography.titleMedium
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedShiftType.displayName,
                onValueChange = {},
                readOnly = true,
                label = { Text("סוג משמרת") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ){
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
        ExposedDropdownMenuBox(
            expanded = employeeExpanded,
            onExpandedChange = { employeeExpanded = it }
        ) {
            OutlinedTextField(
                value = selectedEmployee ?: "לא משובץ",
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
                    text = { Text("ללא שיבוץ") },
                    onClick = {
                        selectedEmployee = null
                        employeeExpanded = false
                    }
                )
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
            }
        }
            OutlinedTextField(
                value = startTime,
                onValueChange = { newTime ->
                    startTime = newTime
                    timeValidationError = validateTimes(newTime, endTime, shift)
                },
                label = { Text("שעת התחלה") },
                placeholder = { Text("06:45") },
                isError = timeValidationError != null,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = endTime,
                onValueChange = { newTime ->
                    endTime = newTime
                    timeValidationError = validateTimes(startTime, newTime, shift)

                },
                label = { Text("שעת סיום") },
                placeholder = { Text("14:45") },
                isError = timeValidationError != null,
                supportingText = timeValidationError?.let {
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                },
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
                    val startTimeParsed = parseTimeString(startTime)
                    val endTimeParsed = parseTimeString(endTime)
                    if (startTimeParsed != null && endTimeParsed != null) {
                        val updatedStartDateTime = createDateTime(shift.startTime, startTimeParsed)
                        val updatedEndDateTime = createDateTime(shift.endTime, endTimeParsed)

                        val updatedShift = shift.copy(
                            shiftType = selectedShiftType,
                            assignedEmployeeId = selectedEmployee,
                            startTime = updatedStartDateTime,
                            endTime = updatedEndDateTime
                        )
                        onSaveClick(updatedShift)
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = timeValidationError == null &&
                        startTime.isNotBlank() &&
                        endTime.isNotBlank()
            ){
                Text("שמור שינויים")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EditShiftScreenPreview() {
    val sampleShift = Shift(
        id = "1",
        startTime = LocalDateTime.of(2024, 1, 15, 6, 45),
        endTime = LocalDateTime.of(2024, 1, 15, 14, 45),
        shiftType = ShiftType.MORNING,
        assignedEmployeeId = "emp1"
    )

    EditShiftScreen(shift = sampleShift)
}
