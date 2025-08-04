package com.example.smartschedule.core.presentation.employee.employee_list_screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartschedule.core.domain.models.User
import com.example.smartschedule.core.presentation.employee.components.EmployeeCard
import com.example.smartschedule.core.presentation.employee.components.EmployeeSearchBar
import com.example.smartschedule.core.presentation.employee.components.EmptyEmployeeListState
import com.example.smartschedule.core.presentation.employee.components.EmptySearchState
import com.example.smartschedule.core.domain.models.Employee
import com.example.smartschedule.core.domain.models.UserStatus
import com.example.smartschedule.core.domain.models.UserType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListScreen(
    modifier: Modifier = Modifier,
    user: User,
    onAddEmployeeClick: () -> Unit = {},
    onViewShiftsClick: () -> Unit = {},
    onEditEmployeeClick: (Employee) -> Unit = {},
    onDeleteEmployeeClick: (Employee) -> Unit = {},
    viewModel: EmployeeListViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(state.value.isRefreshing) {
        Log.d("EmployeeListScreen", "isRefreshing: ${state.value.isRefreshing}")
    }




    LaunchedEffect(state.value.errorMessage) {
        state.value.errorMessage?.let { error ->
            snackBarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = {
            if (user.type.canManageEmployees()) {
                FloatingActionButton(
                    onClick = onAddEmployeeClick,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "הוסף עובד"
                    )
                }
            }
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.value.isRefreshing,
            onRefresh = viewModel::refreshEmployees,
            modifier = modifier.padding(innerPadding),
            state = pullToRefreshState,
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = state.value.isRefreshing,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = pullToRefreshState
                )
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // View Shifts Button
                Button(
                    onClick = onViewShiftsClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("צפייה במשמרות")
                }

                // Search Bar
                EmployeeSearchBar(
                    searchQuery = state.value.searchQuery,
                    onSearchQueryChange = viewModel::updateSearchQuery,
                    onClearSearch = viewModel::clearSearch
                )

                // Content
                when {
                    state.value.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    state.value.filteredEmployees.isEmpty() && state.value.searchQuery.isNotEmpty() -> {
                        EmptySearchState(searchQuery = state.value.searchQuery)
                    }

                    state.value.employees.isEmpty() -> {
                        EmptyEmployeeListState()
                    }

                    else -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(state.value.filteredEmployees){ employee ->
                                EmployeeCard(
                                    employee = employee,
                                    onEditClick = { onEditEmployeeClick(employee) } ,
                                    onDeleteClick = { onDeleteEmployeeClick(employee) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun sampleEmployees(): List<Employee> {

    return listOf(
        Employee(
            id = "1",
            name = "עמית אברהמי",
            email = "amit@example.com",
            employeeNumber = "12345"
        )
    )

}
private fun sampleUser(): User {
    return User(
        id = "sampleUid",
        name = "נחום ברנע",
        email = "user@example.com",
        type = UserType.MANAGER,
        status = UserStatus.ACTIVE
    )
}


@Preview(
    showBackground = true,
)
@Composable
fun EmployeeListScreenPreview() {

    EmployeeListScreen(
//        viewModel = null,
        onAddEmployeeClick = {},
        onViewShiftsClick = {},
        onEditEmployeeClick = {},
        onDeleteEmployeeClick = {},
        user = sampleUser()
        )
}