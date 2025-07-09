package com.example.smartschedule.domain.models

data class Employee(
    val id: String,
    val name: String,
    val email: String,
    val employeeNumber: String,
    val maxShiftPerWeek: Int = 5

)
