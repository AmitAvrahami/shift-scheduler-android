package com.smartschedule.domain.use_cases.employees


import com.smartschedule.domain.models.Employee
import com.smartschedule.domain.repositories.EmployeeRepository
import com.smartschedule.utils.Resource
import javax.inject.Inject

class AddEmployeeUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {

    suspend operator fun invoke(employee: Employee): Resource<Unit> {
        return try {
            if (employee.name.isBlank()) { //TODO : add validation
                Resource.Error("שם העובד לא יכול להיות ריק")
            } else {
                employeeRepository.insertEmployee(employee)
                Resource.Success(Unit)
            }
        } catch (e: Exception) {
            Resource.Error("שגיאה בעת הוספת עובד: ${e.localizedMessage ?: "שגיאה לא צפויה"}")
        }
    }
}
