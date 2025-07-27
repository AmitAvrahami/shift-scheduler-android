package com.example.smartschedule.domain.usecase

import com.example.smartschedule.domain.models.Employee
import com.example.smartschedule.domain.repository.EmployeeRepository
import com.example.smartschedule.domain.repository.UserRepository
import javax.inject.Inject


class AddEmployeeUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(employee: Employee, password: String) : Boolean {
        val userRegisteredSuccessfully = userRepository.registerUser(employee,password)
        if (userRegisteredSuccessfully) {
            employeeRepository.insertEmployee(employee)
            return true
        }
        return false
    }
}