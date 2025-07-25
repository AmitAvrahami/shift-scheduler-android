package com.example.smartschedule.presentation.employee

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartschedule.domain.models.Employee
import com.example.smartschedule.domain.models.User
import com.example.smartschedule.domain.models.UserStatus
import com.example.smartschedule.domain.models.UserType

@Composable
fun EmployeeListScreen(
    modifier: Modifier = Modifier,
    user: User,
    employees: List<Employee>,
    onAddEmployeeClick: () -> Unit = {},
    onViewShiftsClick: () -> Unit = {},
    onEditEmployeeClick: (Employee) -> Unit = {},
    onDeleteEmployeeClick: (Employee) -> Unit = {}
) {
    Scaffold(
        floatingActionButton = {
            if (user.type.canManageEmployees()){
            FloatingActionButton(
                onClick = onAddEmployeeClick,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Employee"
                )
            }
                }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onViewShiftsClick,
                modifier = Modifier.padding(16.dp)
            ) {
                Text("צפייה במשמרות")
            }

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(employees) { employee ->
                    EmployeeCard(
                        employee = employee,
                        onEditClick = onEditEmployeeClick,
                        onDeleteClick = onDeleteEmployeeClick
                    )
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
)
@Composable
fun EmployeeListScreenPreview() {
    val sampleEmployees = listOf(
        Employee(
            id = "1",
            name = "עמית אברהמי",
            email = "amit@example.com",
            employeeNumber = "12345"
        )
    )

    val sampleUser = User(
        id = "sampleUid",
        name = "נחום ברנע",
        email = "user@example.com",
        type = UserType.MANAGER,
        status = UserStatus.ACTIVE
    )



    EmployeeListScreen(
        employees = sampleEmployees,
        onDeleteEmployeeClick = {},
        user = sampleUser
        )
}