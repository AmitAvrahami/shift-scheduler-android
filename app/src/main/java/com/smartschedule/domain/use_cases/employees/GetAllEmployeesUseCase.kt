package com.smartschedule.domain.use_cases.employees

import com.smartschedule.domain.models.Employee
import com.smartschedule.domain.repositories.EmployeeRepository
import com.smartschedule.utils.Resource
import javax.inject.Inject

class GetAllEmployeesUseCase @Inject constructor(
    private val repository: EmployeeRepository
) {
    suspend operator fun invoke(): Resource<List<Employee>> {
        return try {
            val employees = repository.getAllEmployees()
            if (employees.isEmpty()) {
                Resource.Error("לא נמצאו עובדים במערכת")
            } else {
                Resource.Success(employees)
            }
        } catch (e: Exception) {
            Resource.Error("שגיאה בעת טעינת העובדים: ${e.localizedMessage}")
        }
    }
}
