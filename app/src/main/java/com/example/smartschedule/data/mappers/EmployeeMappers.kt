package com.example.smartschedule.data.mappers

import com.example.smartschedule.data.database.entities.EmployeeEntity
import com.example.smartschedule.domain.models.Employee


fun Employee.toEntity() : EmployeeEntity{
    return EmployeeEntity(
        id = this.id,
        name = this.name,
        email = this.email,
        employeeNumber = this.employeeNumber,
        maxShiftPerWeek = this.maxShiftPerWeek,
    )
}


fun EmployeeEntity.toDomain() : Employee{
    return Employee(
        id = this.id,
        name = this.name,
        email = this.email,
        employeeNumber = this.employeeNumber,
        maxShiftPerWeek = this.maxShiftPerWeek,
    )
}