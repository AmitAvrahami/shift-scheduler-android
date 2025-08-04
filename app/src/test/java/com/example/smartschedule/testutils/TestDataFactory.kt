package com.example.smartschedule.testutils

import com.example.smartschedule.core.domain.models.Employee
import com.example.smartschedule.core.domain.models.Shift
import com.example.smartschedule.core.domain.models.ShiftType
import com.example.smartschedule.core.domain.models.UserStatus
import com.example.smartschedule.core.domain.models.UserType
import com.example.smartschedule.domain.models.*
import java.time.LocalDateTime
import java.util.UUID

object TestDataFactory {
    // 🎯 User Factories
    fun createUser(
        id: String = generateId(),
        name: String = "עמית אברהמי",
        email: String = "amit@test.com",
        type: UserType = UserType.EMPLOYEE,
        status: UserStatus = UserStatus.ACTIVE
    ): com.example.smartschedule.core.domain.models.User {
        return _root_ide_package_.com.example.smartschedule.core.domain.models.User(
            id = id,
            name = name,
            email = email,
            type = type,
            status = status
        )
    }

    fun createEmployee(
        id: String = generateId(),
        name: String = "שרה כהן",
        email: String = "sara@test.com",
        employeeNumber: String = "EMP001",
        maxShiftPerWeek: Int = 5,
        type: UserType = UserType.EMPLOYEE,
        status: UserStatus = UserStatus.ACTIVE
    ): Employee {
        return Employee(
            id = id,
            name = name,
            email = email,
            employeeNumber = employeeNumber,
            maxShiftPerWeek = maxShiftPerWeek,
            type = type,
            status = status
        )
    }

    // 🎯 Shift Factories
    fun createShift(
        id: String = generateId(),
        startTime: LocalDateTime = LocalDateTime.of(2025, 1, 28, 8, 0),
        endTime: LocalDateTime = LocalDateTime.of(2025, 1, 28, 16, 0),
        shiftType: ShiftType = ShiftType.MORNING,
        assignedEmployeeId: String? = null
    ): Shift {
        return Shift(
            id = id,
            startTime = startTime,
            endTime = endTime,
            shiftType = shiftType,
            assignedEmployeeId = assignedEmployeeId
        )
    }

    // 🎯 Convenience Methods - שיטות נוחות
    fun createMorningShift(): Shift = createShift(
        startTime = LocalDateTime.of(2025, 1, 28, 6, 45),
        endTime = LocalDateTime.of(2025, 1, 28, 14, 45),
        shiftType = ShiftType.MORNING
    )

    fun createAfternoonShift(): Shift = createShift(
        startTime = LocalDateTime.of(2025, 1, 28, 14, 45),
        endTime = LocalDateTime.of(2025, 1, 28, 22, 45),
        shiftType = ShiftType.AFTERNOON
    )

    fun createNightShift(): Shift = createShift(
        startTime = LocalDateTime.of(2025, 1, 28, 22, 45),
        endTime = LocalDateTime.of(2025, 1, 29, 6, 45),
        shiftType = ShiftType.NIGHT
    )

    fun createAdminUser(): com.example.smartschedule.core.domain.models.User = createUser(
        name = "מנהל ראשי",
        email = "admin@test.com",
        type = UserType.ADMIN
    )

    fun createManagerUser(): com.example.smartschedule.core.domain.models.User = createUser(
        name = "מנהל משמרת",
        email = "manager@test.com",
        type = UserType.MANAGER
    )

    // 🔧 Helper Methods
    private fun generateId(): String = "test-${UUID.randomUUID()}"
}