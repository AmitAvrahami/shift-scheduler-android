package com.example.smartschedule.presentation.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartschedule.domain.models.User
import com.example.smartschedule.domain.models.UserType
import com.example.smartschedule.presentation.viewmodel.LoginViewModel
import com.example.smartschedule.presentation.viewmodel.UserViewModel


@Composable
fun UserListScreen(
    modifier: Modifier = Modifier,
    currentUser: User,
    onAddUserClick: () -> Unit = {},
    viewModel: UserViewModel? = hiltViewModel()
) {
    val users = viewModel?.users?.collectAsState()?.value ?: sampleUsers
    val isLoading = viewModel?.isLoading?.collectAsState()?.value ?: false
    val errorMessage = viewModel?.errorMessage?.collectAsState()?.value


    Scaffold(
        floatingActionButton = {
            if (currentUser.type.canManageEmployees()){
                FloatingActionButton(
                    onClick = onAddUserClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add User"
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "רשימת משתמשים",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator()
            }

            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(users) { user ->
                    Text(user.name)
                }
            }

        }
    }
}

private val sampleUsers = listOf(
    User(id = "1", name = "עמית אברהמי", email = "amit@admin.com", type = UserType.ADMIN)
)

@Preview(showBackground = true)
@Composable
fun UserListScreenPreview() {
    UserListScreen(
        viewModel = null,
        currentUser = sampleUsers[0]
    ) // בלי ViewModel = fake data
}