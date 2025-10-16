package com.smartschedule.test

import com.smartschedule.domain.models.*
import com.smartschedule.domain.repositories.*
import com.smartschedule.domain.use_cases.schedule_context_use_case.BuildEmployeeContextUseCase
import kotlinx.coroutines.runBlocking
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

// -------------------- MOCK REPOSITORIES -------------------- //

class FakeEmployeeRepository : EmployeeRepository {
    override suspend fun getAllEmployees(): List<Employee> = listOf(
        Employee(1, "עמית", isStudent = true, maxShiftsPerWeek = 5, userRole = UserRole.EMPLOYEE),
        Employee(2, "מלי", isStudent = false, maxShiftsPerWeek = 5, userRole = UserRole.MANAGER),
        Employee(3, "מיטל", isStudent = false, maxShiftsPerWeek = 5, userRole = UserRole.MANAGER)
    )

    override suspend fun getEmployeeById(id: Long) = getAllEmployees().find { it.id == id }
    override suspend fun insertEmployee(employee: Employee) {}
    override suspend fun updateEmployee(employee: Employee) {
    }

    override suspend fun deleteEmployee(employee: Employee) {}
}

class FakeConstraintRepository : ConstraintRepository {
    override suspend fun getAllConstraints(): List<Constraint> {
        return emptyList()
    }

    override suspend fun getConstraintById(id: Long): Constraint? {
        return null
    }

    override suspend fun insertConstraint(constraint: Constraint) {

    }

    override suspend fun updateConstraint(constraint: Constraint) {

    }

    override suspend fun deleteConstraint(constraint: Constraint) {

    }

    override suspend fun getForEmployee(employeeId: Long, start: LocalDate, end: LocalDate): List<Constraint> =
        if (employeeId == 1L)
            listOf(
                Constraint(
                    id = 1L,
                    employeeId = 1L,
                    dateStart = start.plusDays(2),
                    dateEnd = start.plusDays(2),
                    constraintType = ConstraintType.DAY_OFF,
                    shiftType = null,
                    reason = "אימון כדורסל"
                )
            )
        else emptyList()
}

class FakeRecurringConstraintRepository : RecurringConstraintRepository {
    override suspend fun getAllRecurringConstraints(): List<RecurringConstraint> {
        return emptyList()
    }

    override suspend fun getRecurringConstraintById(id: Long): RecurringConstraint? {
        return null
    }

    override suspend fun insertRecurringConstraint(recurringConstraint: RecurringConstraint) {
    }

    override suspend fun updateRecurringConstraint(recurringConstraint: RecurringConstraint) {
    }

    override suspend fun deleteRecurringConstraint(recurringConstraint: RecurringConstraint) {
    }

    override suspend fun getForEmployee(employeeId: Long): List<RecurringConstraint> =
        if (employeeId == 1L)
            listOf(
                RecurringConstraint(
                    id = 1L,
                    employeeId = 1L,
                    dayOfWeek = DayOfWeek.WEDNESDAY,
                    shiftTypes = setOf(ShiftType.NOON),
                    reason = "אימון קבוע"
                )
            )
        else emptyList()
}

class FakeShiftAssignmentRepository : ShiftAssignmentRepository {
    override suspend fun getAllShiftAssignments(): List<ShiftAssignment> {
        return emptyList()
    }

    override suspend fun getShiftAssignmentById(id: Long): ShiftAssignment? {
        return null
    }

    override suspend fun insertShiftAssignment(shiftAssignment: ShiftAssignment) {
    }

    override suspend fun updateShiftAssignment(shiftAssignment: ShiftAssignment) {
    }

    override suspend fun deleteShiftAssignment(shiftAssignment: ShiftAssignment) {
    }

    override suspend fun getForEmployee(employeeId: Long, weekStart: LocalDate, weekEnd: LocalDate): List<ShiftAssignment> =
        if (employeeId == 1L)
            listOf(
                ShiftAssignment(
                    id = 1,
                    shiftId = 101,
                    employeeId = 1,
                    assignedAt = LocalDateTime.now().minusDays(1),
                    assignedBy = "מערכת",
                    shiftType = ShiftType.NOON
                )
            )
        else emptyList()
}

class FakeShiftRepository : ShiftRepository {
    override suspend fun getAllShifts(): List<Shift> {
        return emptyList()
    }

    override suspend fun getShiftById(id: Long): Shift? {
        return null
    }

    override suspend fun insertShift(shift: Shift) {
    }

    override suspend fun insertShifts(shifts: List<Shift>) {
    }

    override suspend fun updateShift(shift: Shift) {
    }

    override suspend fun deleteShift(shift: Shift) {
    }

    override suspend fun getAvailableShifts(
        weekStart: LocalDate,
        weekEnd: LocalDate
    ): List<Shift> {
        TODO("Not yet implemented")
    }

    override suspend fun getShiftsBetween(start: LocalDate, end: LocalDate): List<Shift> {
        val days = (0..6).map { start.plusDays(it.toLong()) }
        val shifts = mutableListOf<Shift>()
        var idCounter = 100L
        for (day in days) {
            shifts += Shift(idCounter++, 1L, day, ShiftType.MORNING, 2)
            shifts += Shift(idCounter++, 1L, day, ShiftType.NOON, 2)
            shifts += Shift(idCounter++, 1L, day, ShiftType.NIGHT, 1)
        }
        return shifts
    }
}

// -------------------- MAIN TEST -------------------- //

fun main() = runBlocking {
    val weekStart = LocalDate.of(2025, 10, 19)
    val weekEnd = weekStart.plusDays(6)

    val useCase = BuildEmployeeContextUseCase(
        employeeRepo = FakeEmployeeRepository(),
        constraintRepo = FakeConstraintRepository(),
        recurringRepo = FakeRecurringConstraintRepository(),
        assignmentRepo = FakeShiftAssignmentRepository(),
        shiftRepo = FakeShiftRepository()
    )

    val contexts = useCase.buildForWeek(weekStart, weekEnd)

    println("======= תוצאות בדיקה =======")
    contexts.forEach { ctx ->
        println("\n👤 עובד: ${ctx.employee.name}")
        println("• אילוצים: ${ctx.constraints.size}")
        println("• אילוצים קבועים: ${ctx.recurringConstraints.size}")
        println("• משמרות שובצו: ${ctx.assignedShifts.size}")
        println("• משמרות זמינות לשבוע: ${ctx.availableShifts.size}")
        println("• יכול לעבוד ביום רביעי צהריים? ${
            ctx.canWork(
                ctx.availableShifts.firstOrNull {
                    it.date.dayOfWeek == DayOfWeek.WEDNESDAY && it.shiftType == ShiftType.NOON
                } ?: return@forEach
            )
        }")
    }
}
