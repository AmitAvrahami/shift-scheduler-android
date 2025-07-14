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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartschedule.data.database.AppDataBase
import com.example.smartschedule.data.repository.EmployeeRepository
import com.example.smartschedule.data.repository.EmployeeRepositoryImpl
import com.example.smartschedule.data.repository.ShiftRepository
import com.example.smartschedule.data.repository.ShiftRepositoryImpl
import com.example.smartschedule.domain.models.Employee
import com.example.smartschedule.domain.models.Schedule
import com.example.smartschedule.domain.models.Shift
import com.example.smartschedule.domain.models.ShiftType
import com.example.smartschedule.domain.validation.ShiftValidation
import com.example.smartschedule.presentation.employee.AddEmployeeScreen
import com.example.smartschedule.presentation.employee.EditEmployeeScreen
import com.example.smartschedule.presentation.employee.EmployeeListScreen
import com.example.smartschedule.presentation.navigation.Routes
import com.example.smartschedule.presentation.shift.AddShiftScreen
import com.example.smartschedule.presentation.shift.ShiftListScreen
import com.example.smartschedule.ui.theme.SmartScheduleTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {

    private lateinit var dataBase : AppDataBase
    private lateinit var employeeRepository : EmployeeRepository
    private lateinit var shiftRepository: ShiftRepository


    private fun generateEmployeeId(): String {
        return "emp_${System.currentTimeMillis()}"
    }
    private fun generateShiftId(): String {
        return "shift_${System.currentTimeMillis()}"
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBase = DatabaseModule.getDatabase(this)
        employeeRepository = EmployeeRepositoryImpl(dataBase.employeeDao())
        shiftRepository = ShiftRepositoryImpl(dataBase.shiftDao())

        enableEdgeToEdge()
        testDataModels()
        setContent {
            SmartScheduleTheme {
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()

                val employees by employeeRepository.getAllEmployees().collectAsState(initial = emptyList())
                val shifts by shiftRepository.getAllShifts().collectAsState(initial = emptyList())
                var selectedEmployeeForEdit by remember { mutableStateOf<Employee?>(null) }


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
                                employees = employees,
                                onViewShiftsClick = {
                                    navController.navigate(Routes.SHIFT_LIST)
                                },
                                onEditEmployeeClick = { selectedEmployee ->
                                    selectedEmployeeForEdit = selectedEmployee
                                    navController.navigate(Routes.EDIT_EMPLOYEE)
                                }
                            )
                        }
                        composable(Routes.ADD_EMPLOYEE){
                            AddEmployeeScreen(
                                onSaveClick = { newEmployee ->
                                    coroutineScope.launch {  // ← חדש!
                                        val employeeWithId = newEmployee.copy(
                                            id = generateEmployeeId()
                                        )
                                        employeeRepository.insertEmployee(employeeWithId)
                                        Log.d("AddEmployee", "נוצר עובד: ${newEmployee.name}")
                                    }
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
                                shifts = shifts,
                                onAddShiftClick = {
                                    navController.navigate(Routes.ADD_SHIFT)
                                }
                            )
                        }
                        composable(Routes.ADD_SHIFT) {
                            AddShiftScreen(
                                modifier = Modifier.padding(innerPadding),
                                onSaveClick = {
                                    coroutineScope.launch {
                                        val newShift = Shift(
                                            id = generateShiftId(),
                                            startTime = LocalDateTime.of(2024, 1, 15, 6, 45), // זמני - נשפר בהמשך
                                            endTime = LocalDateTime.of(2024, 1, 15, 14, 45),
                                            shiftType = ShiftType.MORNING,
                                            assignedEmployeeId = null
                                        )
                                        shiftRepository.insertShift(newShift)
                                        Log.d("AddShift", "נוצרה משמרת חדשה")
                                    }
                                    navController.popBackStack()
                                },
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(Routes.EDIT_EMPLOYEE){
                            selectedEmployeeForEdit?.let { employee ->
                                EditEmployeeScreen(
                                    employee = employee,
                                    onSaveClick = { updatedEmployee ->
                                        coroutineScope.launch {
                                            employeeRepository.insertEmployee(updatedEmployee)
                                            Log.d("EditEmployee", "עובד עודכן: ${updatedEmployee.name}")
                                        }
                                        selectedEmployeeForEdit = null
                                        navController.popBackStack()
                                    },
                                    onBackClick = {
                                        selectedEmployeeForEdit = null
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