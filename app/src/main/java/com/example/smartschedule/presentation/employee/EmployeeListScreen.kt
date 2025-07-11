package com.example.smartschedule.presentation.employee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartschedule.domain.models.Employee

@Composable
fun EmployeeListScreen(modifier: Modifier = Modifier) {
    val employees = listOf(
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
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        items(employees) { employee ->
            EmployeeCard(employee = employee)
        }
    }
}


@Preview(
    showBackground = true,
)
@Composable
fun EmployeeListScreenPreview() {
    EmployeeListScreen()
}