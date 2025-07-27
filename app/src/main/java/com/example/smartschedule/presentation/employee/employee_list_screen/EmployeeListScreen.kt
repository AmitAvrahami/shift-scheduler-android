package com.example.smartschedule.presentation.employee.employee_list_screen

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.* // ktlint-disable no-wildcard-imports
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartschedule.domain.models.Employee
import com.example.smartschedule.domain.models.User
import com.example.smartschedule.domain.models.UserStatus
import com.example.smartschedule.domain.models.UserType
import com.example.smartschedule.presentation.employee.components.EmployeeCard
import com.example.smartschedule.presentation.employee.components.EmployeeSearchBar
import com.example.smartschedule.presentation.employee.components.EmptyEmployeeListState
import com.example.smartschedule.presentation.employee.components.EmptySearchState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListScreen(
    modifier: Modifier = Modifier,
    user: User,
    onAddEmployeeClick: () -> Unit = {},
    onViewShiftsClick: () -> Unit = {},
    onEditEmployeeClick: (Employee) -> Unit = {},
    onDeleteEmployeeClick: (Employee) -> Unit = {},
    viewModel: EmployeeListViewModel? = hiltViewModel()
) {

    val state = viewModel?.state?.collectAsState()?.value ?: EmployeeListState(
        employees = sampleEmployees(),
        isLoading = false,
        errorMessage = null
    )
    val snackBarHostState = remember { SnackbarHostState() }
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(state.isRefreshing) {
        android.util.Log.d("PullToRefresh", "state.isRefreshing changed: ${state.isRefreshing}")
    }

    LaunchedEffect(state.isLoading) {
        android.util.Log.d("PullToRefresh", "pullToRefreshState.isRefreshing changed: ${state.isLoading}")
    }





    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { error ->
            snackBarHostState.showSnackbar(error)
            viewModel?.clearError()
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
        viewModel?.let {
            PullToRefreshBox(
                state = pullToRefreshState,
                isRefreshing = state.isRefreshing,
                onRefresh = it::refreshEmployees,
                modifier = modifier.padding(innerPadding)
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
                        searchQuery = state.searchQuery,
                        onSearchQueryChange = viewModel::updateSearchQuery,
                        onClearSearch = viewModel::clearSearch
                    )

                    // Content
                    when {
                        state.isLoading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        state.filteredEmployees.isEmpty() && state.searchQuery.isNotEmpty() -> {
                            EmptySearchState(searchQuery = state.searchQuery)
                        }

                        state.employees.isEmpty() -> {
                            EmptyEmployeeListState()
                        }

                        else -> {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(state.filteredEmployees) { employee ->
                                    EmployeeCard(
                                        employee = employee,
                                        onEditClick = onEditEmployeeClick,
                                        onDeleteClick = {
                                            viewModel.deleteEmployee(employee)
                                            onDeleteEmployeeClick(employee)
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
        viewModel = null,
        onAddEmployeeClick = {},
        onViewShiftsClick = {},
        onEditEmployeeClick = {},
        onDeleteEmployeeClick = {},
        user = sampleUser()
        )
}