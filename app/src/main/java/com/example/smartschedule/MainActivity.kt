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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartschedule.domain.models.Employee
import com.example.smartschedule.domain.models.Schedule
import com.example.smartschedule.domain.models.Shift
import com.example.smartschedule.domain.models.ShiftType
import com.example.smartschedule.domain.validation.ShiftValidation
import com.example.smartschedule.presentation.employee.AddEmployeeScreen
import com.example.smartschedule.presentation.employee.EmployeeListScreen
import com.example.smartschedule.presentation.navigation.Routes
import com.example.smartschedule.presentation.shift.AddShiftScreen
import com.example.smartschedule.presentation.shift.ShiftListScreen
import com.example.smartschedule.ui.theme.SmartScheduleTheme
import java.time.LocalDate
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {

    private val employeesList = mutableStateListOf(
        Employee(
            id = "1",
            name = "עמית אברהמי",
            email = "amit@example.com",
            employeeNumber = "12345"
        ),
        Employee(
            id = "2",
            name = "שרה כהן",
            email = "sara@example.com",
            employeeNumber = "67890"
        ),
        Employee(
            id = "3",
            name = "דוד לוי",
            email = "david@example.com",
            employeeNumber = "11111"
        )
    )
    private val shiftsList = mutableStateListOf(
        Shift(
            id = "1",
            startTime = LocalDateTime.of(2024, 1, 15, 6, 45),
            endTime = LocalDateTime.of(2024, 1, 15, 14, 45),
            shiftType = ShiftType.MORNING,
            assignedEmployeeId = "emp1"
        ),
        Shift(
            id = "2",
            startTime = LocalDateTime.of(2024, 1, 15, 14, 45),
            endTime = LocalDateTime.of(2024, 1, 15, 22, 45),
            shiftType = ShiftType.AFTERNOON,
            assignedEmployeeId = null
        ),
        Shift(
            id = "3",
            startTime = LocalDateTime.of(2024, 1, 15, 22, 45),
            endTime = LocalDateTime.of(2024, 1, 16, 6, 45),
            shiftType = ShiftType.NIGHT,
            assignedEmployeeId = "emp2"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        testDataModels()
        setContent {
            SmartScheduleTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost (
                        navController = navController,
                        startDestination = Routes.EMPLOYEE_LIST
                    ){
                        composable(Routes.EMPLOYEE_LIST){
                            EmployeeListScreen(
                                modifier = Modifier.padding(innerPadding),
                                onAddEmployeeClick = {
                                    navController.navigate(Routes.ADD_EMPLOYEE)
                                },
                                employees = employeesList,
                                onViewShiftsClick = {
                                    navController.navigate(Routes.SHIFT_LIST)
                                }
                            )
                        }
                        composable(Routes.ADD_EMPLOYEE){
                            AddEmployeeScreen(
                                onSaveClick = { newEmployee ->
                                    val employeeWithId = newEmployee.copy(
                                        id = (employeesList.size + 1).toString()
                                    )
                                    employeesList.add(employeeWithId)
                                    Log.d("AddEmployee", "נוצר עובד: ${newEmployee.name}")
                                    navController.popBackStack()
                                },
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(Routes.SHIFT_LIST) {
                            ShiftListScreen(
                                modifier = Modifier.padding(innerPadding),
                                shifts = shiftsList,
                                onAddShiftClick = {
                                    navController.navigate(Routes.ADD_SHIFT)
                                }
                            )
                        }
                        composable(Routes.ADD_SHIFT) {
                            AddShiftScreen(
                                modifier = Modifier.padding(innerPadding),
                                onSaveClick = {
                                    val newShift = Shift(
                                        id = (shiftsList.size + 1).toString(),
                                        startTime = LocalDateTime.of(2024, 1, 15, 6, 45), // זמני - נשפר בהמשך
                                        endTime = LocalDateTime.of(2024, 1, 15, 14, 45),
                                        shiftType = ShiftType.MORNING,
                                        assignedEmployeeId = null
                                    )
                                    shiftsList.add(newShift)
                                    Log.d("Add Shift","נוצרה משמרת חדשה")
                                    navController.popBackStack()
                                },
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
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