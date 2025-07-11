package com.example.smartschedule.presentation.employee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartschedule.domain.models.Employee
import compose.icons.TablerIcons
import compose.icons.tablericons.Id
import compose.icons.tablericons.Mail
import compose.icons.tablericons.User

@Composable
fun EmployeeCard(employee: Employee){
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Icon(
                    imageVector = TablerIcons.User,
                    contentDescription = "User Icon",
                )
                Text(text = employee.name)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Icon(
                    imageVector = TablerIcons.Id,
                    contentDescription = "ID Icon",
                )
                Text(
                    text = employee.employeeNumber,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Icon(
                    imageVector = TablerIcons.Mail,
                    contentDescription = "Mail Icon",
                )
                Text(
                    text = employee.email
                )
            }
        }

    }
}


@Preview(
    showBackground = true,
    widthDp = 200,
    heightDp = 200,
)

@Composable
fun EmployeeCardPreview() {
    val sampleEmployee = Employee(
        id = "1",
        name = "עמית אברהמי",
        email = "amit@example.com",
        employeeNumber = "12345"
    )

    EmployeeCard(employee = sampleEmployee)
}
