package com.example.smartschedule.core.presentation.dashboard


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartschedule.core.domain.models.User
import com.example.smartschedule.core.domain.models.UserStatus
import com.example.smartschedule.core.domain.models.UserType

@Composable
fun ManagerDashboard(
    modifier: Modifier = Modifier,
    user: User,
    onViewEmployeesClick: () -> Unit = {},
    onViewShiftsClick: () -> Unit = {},
    onViewUsersClick : () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "לוח בקרה מנהל",
            style = MaterialTheme.typography.headlineMedium
        )

        Button(
            onClick = onViewEmployeesClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ניהול עובדים")
        }

        Button(
            onClick = onViewShiftsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ניהול משמרות")
        }


        if (user.type.canCreateManagers()){
            Button(
                onClick = onViewUsersClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ניהול משתמשים")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ManagerDashboardPreview() {
    val sampleUser = User(
        id = "manager123",
        name = "רון שרוני",
        type = UserType.ADMIN,
        email = "manager@example.com",
        status = UserStatus.ACTIVE
    )
    ManagerDashboard(user = sampleUser)
}