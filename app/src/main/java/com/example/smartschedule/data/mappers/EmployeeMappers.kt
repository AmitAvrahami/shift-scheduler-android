package com.example.smartschedule.data.mappers

import com.example.smartschedule.data.database.entities.EmployeeEntity
import com.example.smartschedule.domain.models.Employee
import com.example.smartschedule.domain.models.UserStatus
import com.example.smartschedule.domain.models.UserType


fun Employee.toEntity() : EmployeeEntity{
    return EmployeeEntity(
        id = this.id,
        name = this.name,
        email = this.email,
        employeeNumber = this.employeeNumber,
        maxShiftPerWeek = this.maxShiftPerWeek,
        userType = this.type.name,
        status = this.status.name,
        createdDate = this.createdDate
    )
}


fun EmployeeEntity.toDomain() : Employee{
    return Employee(
        id = this.id,
        name = this.name,
        email = this.email,
        employeeNumber = this.employeeNumber,
        maxShiftPerWeek = this.maxShiftPerWeek,
        type = UserType.valueOf(this.userType),
        status = UserStatus.valueOf(this.status),
        createdDate = this.createdDate

    )
}