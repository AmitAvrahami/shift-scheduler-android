package com.example.smartschedule

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartschedule.domain.models.Employee
import com.example.smartschedule.domain.models.Schedule
import com.example.smartschedule.domain.models.Shift
import com.example.smartschedule.domain.models.ShiftType
import com.example.smartschedule.domain.validation.ShiftValidation
import com.example.smartschedule.presentation.employee.EmployeeListScreen
import com.example.smartschedule.ui.theme.SmartScheduleTheme
import java.time.LocalDate
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        testDataModels()
        setContent {
            SmartScheduleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EmployeeListScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

private fun testDataModels() {
    val employee = Employee(
        id = "emp1",
        name = "עמית אברהמי",
        email = "amit@example.com",
        employeeNumber = "12345"
    )

    val shift = Shift(
        id = "shift1",
        startTime = LocalDateTime.of(2024, 1, 15, 6, 45),
        endTime = LocalDateTime.of(2024, 1, 15, 14, 45),
        shiftType = ShiftType.MORNING,
        assignedEmployeeId = employee.id
    )

    val schedule = Schedule(
        id = "schedule1",
        weekStartDate = LocalDate.of(2024, 1, 15),
        shifts = listOf(shift)
    )

    Log.d("ShiftTest", "המשמרת תקינה: ${ShiftValidation.isValidShift(shift)}")
    Log.d("ShiftTest", "העובד יכול לעבוד: ${ShiftValidation.canEmployeeWork(employee, shift)}")
    Log.d("ShiftTest", "אורך המשמרת: ${ShiftValidation.getShiftDurationHours(shift)} שעות")
    Log.d("ShiftTest", "משמרת סטנדרטית: ${ShiftValidation.isStandardShift(shift)}")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmartScheduleTheme {
        Greeting("Android")
    }
}