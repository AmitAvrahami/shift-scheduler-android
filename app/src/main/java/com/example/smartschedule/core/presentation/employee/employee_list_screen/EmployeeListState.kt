package com.example.smartschedule.core.presentation.employee.employee_list_screen

import com.example.smartschedule.core.domain.models.Employee

data class EmployeeListState(
    val employees: List<Employee> = emptyList(),
    val filteredEmployees: List<Employee> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false
)