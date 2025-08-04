package com.example.smartschedule.core

import com.example.smartschedule.core.presentation.employee.add_employee_screen.AddEmployeeScreen
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartschedule.core.domain.models.*
import com.example.smartschedule.core.domain.validation.ShiftValidation
import com.example.smartschedule.core.presentation.auth.LoginScreen
import com.example.smartschedule.core.presentation.dashboard.ManagerDashboard
import com.example.smartschedule.core.presentation.employee.edit_employee_screen.EditEmployeeScreen
import com.example.smartschedule.core.presentation.employee.employee_list_screen.EmployeeListScreen
import com.example.smartschedule.core.presentation.employee.viewmodel.AddEmployeeViewModel
import com.example.smartschedule.core.presentation.navigation.Routes
import com.example.smartschedule.core.presentation.shift.AddShiftScreen
import com.example.smartschedule.core.presentation.shift.EditShiftScreen
import com.example.smartschedule.core.presentation.shift.ShiftListScreen
import com.example.smartschedule.core.presentation.user.UserListScreen
import com.example.smartschedule.core.presentation.user.add_user_screen.AddUserScreen
import com.example.smartschedule.core.presentation.user.add_user_screen.AddUserViewModel
import com.example.smartschedule.core.presentation.viewmodel.LoginViewModel
import com.example.smartschedule.ui.theme.SmartScheduleTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private fun generateEmployeeId(): String {
        return "emp_${System.currentTimeMillis()}"
    }
    private fun generateUserId(): String {
        return "user_${System.currentTimeMillis()}"
    }

    private fun generateShiftId(): String {
        return "shift_${System.currentTimeMillis()}"
    }

    // TODO: Move to ViewModel later
    private suspend fun validateEmployeeNumber(
        employeeNumber: String,
        employeeCount: Int
    ): String? {
        return when{
            employeeNumber.isBlank() -> null
            employeeNumber.length < 3 -> "מספר עובד חייב להיות לפחות 3 ספרות"
            // TODO: Real validation with repository
            employeeNumber == "12345" -> "מספר עובד כבר קיים במערכת"
            else -> null
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        testDataModels()
        setContent {
            SmartScheduleTheme {
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()

                var currentUser by remember { mutableStateOf<User?>(null) }

                //View Models
                val loginViewModel: LoginViewModel = hiltViewModel()
                val addUserViewModel: AddUserViewModel = hiltViewModel()
                val addEmployeeViewModel: AddEmployeeViewModel = hiltViewModel()

                // TODO: These will be replaced with ViewModels later
                var employees by remember { mutableStateOf(listOf<Employee>()) }
                var shifts by remember { mutableStateOf(listOf<Shift>()) }

                var selectedEmployeeForEdit by remember { mutableStateOf<Employee?>(null) }

                //deleteEmployee vars
                var employeeToDelete by remember { mutableStateOf<Employee?>(null) }
                var showDeleteConfirmationDialog by remember { mutableStateOf(false) }

                //validation Employee vars
                var currentEmployeeNumber by remember { mutableStateOf("") }

                //Shift Edit Vars
                var selectedShiftForEdit by remember { mutableStateOf<Shift?>(null) }

                //Delete Shift Vars
                var shiftToDelete by remember { mutableStateOf<Shift?>(null) }
                var showDeleteShiftConfirmationDialog by remember { mutableStateOf(false) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost (
                        navController = navController,
                        startDestination = Routes.LOGIN
                    ){
                        composable(Routes.LOGIN){
                            LoginScreen(
                                modifier = Modifier.padding(innerPadding),
                               onLoginSuccess = { user ->
                                   Log.d("MainActivity", "🎉 התחברות הצליחה - משתמש: ${user.name}")
                                   currentUser = user
                                   when (user.type) {
                                       UserType.EMPLOYEE -> navController.navigate(Routes.EMPLOYEE_LIST)
                                       UserType.MANAGER -> navController.navigate(Routes.MANAGER_DASHBOARD)
                                       UserType.ADMIN -> navController.navigate(Routes.MANAGER_DASHBOARD)
                                   }
                               }
                            )
                        }

                        composable(Routes.MANAGER_DASHBOARD){
                            currentUser?.let { user ->
                                ManagerDashboard(
                                    user = user,
                                    modifier = Modifier.padding(innerPadding),
                                    onViewEmployeesClick = {
                                        navController.navigate(Routes.EMPLOYEE_LIST)
                                    },
                                    onViewShiftsClick = {
                                        navController.navigate(Routes.SHIFT_LIST)
                                    },
                                    onViewUsersClick = {
                                        navController.navigate(Routes.USER_LIST)
                                    }
                                )
                            }
                        }

                        composable(Routes.EMPLOYEE_LIST) {
                            currentUser?.let { user ->
                                EmployeeListScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    user = user,
                                    onAddEmployeeClick = {
                                        navController.navigate(Routes.ADD_EMPLOYEE)
                                    },
                                    onViewShiftsClick = {
                                        navController.navigate(Routes.SHIFT_LIST)
                                    },
                                    onEditEmployeeClick = { selectedEmployee ->
                                        selectedEmployeeForEdit = selectedEmployee
                                        navController.navigate(Routes.EDIT_EMPLOYEE)
                                    },
                                    onDeleteEmployeeClick = { selectedEmployee ->
                                        employeeToDelete = selectedEmployee
                                        showDeleteConfirmationDialog = true
                                    },
                                )
                            }
                        }

                        composable(Routes.ADD_EMPLOYEE){
                            AddEmployeeScreen(
                                onNavigateBack = { finish() },
                                onEmployeeAdded = { employee ->
                                    Log.d("MainActivity",employee.name)
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(Routes.SHIFT_LIST) {
                            currentUser?.let { user ->
                                ShiftListScreen(
                                    user = user,
                                    modifier = Modifier.padding(innerPadding),
                                    shifts = shifts,
                                    onAddShiftClick = {
                                        navController.navigate(Routes.ADD_SHIFT)
                                    },
                                    onEditShiftClick = { selectedShift ->
                                        selectedShiftForEdit = selectedShift
                                        navController.navigate(Routes.EDIT_SHIFT)
                                    },
                                    onDeleteShiftClick = { selectedShift ->
                                        shiftToDelete = selectedShift
                                        showDeleteShiftConfirmationDialog = true
                                    }
                                )
                            }
                        }

                        composable(Routes.ADD_SHIFT) {
                            AddShiftScreen(
                                modifier = Modifier.padding(innerPadding),
                                onSaveClick = {
                                    // TODO: Replace with ViewModel call
                                    val newShift = Shift(
                                        id = generateShiftId(),
                                        startTime = LocalDateTime.of(2024, 1, 15, 6, 45), // זמני - נשפר בהמשך
                                        endTime = LocalDateTime.of(2024, 1, 15, 14, 45),
                                        shiftType = ShiftType.MORNING,
                                        assignedEmployeeId = null
                                    )
                                    shifts = shifts + newShift
                                    Log.d("AddShift", "נוצרה משמרת חדשה")

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
                                        // TODO: Replace with ViewModel call
                                        employees = employees.map {
                                            if (it.id == updatedEmployee.id) updatedEmployee else it
                                        }
                                        Log.d("EditEmployee", "עובד עודכן: ${updatedEmployee.name}")

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

                        composable(Routes.EDIT_SHIFT){
                            selectedShiftForEdit?.let { shift ->
                                EditShiftScreen(
                                    shift = shift,
                                    onSaveClick = { updatedShift ->
                                        // TODO: Replace with ViewModel call
                                        shifts = shifts.map {
                                            if (it.id == updatedShift.id) updatedShift else it
                                        }
                                        Log.d("EditShift", "משמרת עודכנה: ${updatedShift.shiftType.displayName}")

                                        selectedShiftForEdit = null
                                        navController.popBackStack()
                                    },
                                    onBackClick = {
                                        selectedShiftForEdit = null
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }

                        composable(Routes.USER_LIST) {
                            currentUser?.let { user ->
                                UserListScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    currentUser = user,
                                    onAddUserClick = {
                                        navController.navigate(Routes.ADD_USER)
                                    }
                                )
                            }
                        }

                        composable(Routes.ADD_USER){
                            currentUser?.let { user ->
                                AddUserScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    currentUser = user,
                                    onSaveClick = { newUser ->
                                        coroutineScope.launch {
                                            try {
                                                when (newUser) {
                                                    is Employee, -> {
                                                        Log.d("MainActivity","המשתמש הוא עובד")
//                                                        val employeeWithId = newUser.copy(
//                                                            id = generateEmployeeId()
//                                                        )
//                                                        userViewModel.addUser(employeeWithId,password)
//                                                        employeeViewModel.addEmployee(employeeWithId)
//                                                        Log.d("AddUser", "נוצר עובד/מנהל: ${employeeWithId.name}")
                                                    }

                                                    else -> {
                                                        Log.d("MainActivity","המשתמש הוא אדמין")
//                                                        val userWithId = User(
//                                                            id = generateUserId(),
//                                                            name = newUser.name,
//                                                            email = newUser.email,
//                                                            type = newUser.type,
//                                                            status = newUser.status,
//                                                            createdDate = newUser.createdDate
//                                                        )
//                                                        userViewModel.addUser(userWithId)
//                                                        Log.d("AddUser", "נוצר אדמין: ${userWithId.name}")
                                                        Log.d("AddUser", "נוצר אדמין: ")


                                                    }
                                                }
                                                navController.popBackStack()
                                            } catch (e: Exception) {
                                                Log.e("AddUser", "שגיאה ביצירת משתמש: ${e.message}")
                                            }
                                        }
                                    },
                                    onBackClick = {
                                        navController.popBackStack()
                                    }
                                )
                            }

                        }
                    }

                    // Delete Employee Dialog
                    if (showDeleteConfirmationDialog && employeeToDelete != null) {
                        AlertDialog(
                            onDismissRequest = {
                                showDeleteConfirmationDialog = false
                                employeeToDelete = null
                            },
                            title = {
                                Text("מחיקת עובד")
                            },
                            text = {
                                Text("האם אתה בטוח שברצונך למחוק את ${employeeToDelete!!.name}?")
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        employeeToDelete?.let { employee ->
                                            // TODO: Replace with ViewModel call
                                            employees = employees.filter { it.id != employee.id }
                                            Log.d("DeleteEmployee", "עובד נמחק: ${employee.name}")
                                        }
                                        showDeleteConfirmationDialog = false
                                        employeeToDelete = null
                                    }
                                ) {
                                    Text("מחק")
                                }
                            },
                            dismissButton = {
                                OutlinedButton(
                                    onClick = {
                                        showDeleteConfirmationDialog = false
                                        employeeToDelete = null
                                    }
                                ) {
                                    Text("בטל")
                                }
                            }
                        )
                    }

                    // Delete Shift Dialog
                    if(showDeleteShiftConfirmationDialog && shiftToDelete != null){
                        AlertDialog(
                            onDismissRequest = {
                                showDeleteShiftConfirmationDialog = false
                                shiftToDelete = null
                            },
                            title = {
                                Text("מחיקת משמרת")
                            },
                            text = {
                                Text("האם אתה בטוח שברצונך למחוק את ${shiftToDelete!!.shiftType.displayName}?")
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        shiftToDelete?.let { shift ->
                                            // TODO: Replace with ViewModel call
                                            shifts = shifts.filter { it.id != shift.id }
                                            Log.d("DeleteShift", "משמרת נמחקה: ${shift.shiftType.displayName}")
                                        }
                                        showDeleteShiftConfirmationDialog = false
                                        shiftToDelete = null
                                    }
                                ) {
                                    Text("מחק")
                                }
                            },
                            dismissButton = {
                                OutlinedButton(
                                    onClick = {
                                        showDeleteShiftConfirmationDialog = false
                                        shiftToDelete = null
                                    }
                                ) {
                                    Text("בטל")
                                }
                            }
                        )
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

    Schedule(
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