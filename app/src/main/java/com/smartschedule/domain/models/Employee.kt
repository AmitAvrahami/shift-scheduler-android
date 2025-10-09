package com.smartschedule.domain.models

import com.smartschedule.utils.Constants

data class Employee(
    val id : Long? = null,
    val name : String,
    val isStudent : Boolean,
    val maxShiftsPerWeek : Int = 5,
    val isActive : Boolean = true,
    val canWorkFriday: Boolean = false,
    val canWorkSaturday: Boolean = false,
    val userRole : UserRole,
    val notes : String? = null,
){
    init {
        require(name.isNotBlank()) { "Employee.name must not be blank" }
        require(maxShiftsPerWeek in Constants.MIN_SHIFT_PER_WEEK..Constants.MAX_SHIFT_PER_WEEK) {
            "maxShiftsPerWeek must be within [${Constants.MIN_SHIFT_PER_WEEK}..${Constants.MAX_SHIFT_PER_WEEK}]"
        }
    }
}

