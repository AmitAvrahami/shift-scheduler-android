package com.smartschedule.test.mock

import com.smartschedule.domain.models.Constraint
import com.smartschedule.domain.models.ConstraintType
import com.smartschedule.domain.models.Employee
import com.smartschedule.domain.models.RecurringConstraint
import com.smartschedule.domain.models.Shift
import com.smartschedule.domain.models.ShiftType
import com.smartschedule.domain.models.UserRole
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.collections.plusAssign

/**
 * יוצר נתוני דמה לשבוע עבודה — עובדים, אילוצים ומשמרות.
 */
object MockDataProvider {

    val employees = listOf(
        Employee(1, "עמית", isStudent = true, maxShiftsPerWeek = 5, userRole = UserRole.EMPLOYEE),
        Employee(2, "מלי", isStudent = false, maxShiftsPerWeek = 5, userRole = UserRole.MANAGER),
        Employee(3, "שחר", isStudent = true, maxShiftsPerWeek = 4, userRole = UserRole.EMPLOYEE),
        Employee(4, "מיטל", isStudent = false, maxShiftsPerWeek = 5, userRole = UserRole.MANAGER),
        Employee(5, "אופק", isStudent = true, maxShiftsPerWeek = 5, userRole = UserRole.EMPLOYEE),
        Employee(6, "שני", isStudent = false, maxShiftsPerWeek = 5, userRole = UserRole.EMPLOYEE),
        Employee(7, "מסי", isStudent = true, maxShiftsPerWeek = 3, userRole = UserRole.EMPLOYEE),
        Employee(8, "פולינה", isStudent = false, maxShiftsPerWeek = 4, userRole = UserRole.EMPLOYEE),
        Employee(9, "לאורה", isStudent = true, maxShiftsPerWeek = 5, userRole = UserRole.EMPLOYEE)
    )

    val recurringConstraints = listOf(
        // עמית – סגור כל יום ראשון ושישי
        RecurringConstraint(1, 1, DayOfWeek.SUNDAY, setOf(ShiftType.MORNING), "סגור ראשון"),
        RecurringConstraint(2, 1, DayOfWeek.SUNDAY, setOf(ShiftType.NOON), "סגור ראשון"),
        RecurringConstraint(3, 1, DayOfWeek.SUNDAY, setOf(ShiftType.NIGHT), "סגור ראשון"),
        RecurringConstraint(4, 1, DayOfWeek.FRIDAY, setOf(ShiftType.MORNING), "סגור שישי"),
        RecurringConstraint(5, 1, DayOfWeek.FRIDAY, setOf(ShiftType.NOON), "סגור שישי"),
        RecurringConstraint(6, 1, DayOfWeek.FRIDAY, setOf(ShiftType.NIGHT), "סגור שישי"),

        // מלי ומיטל – עובדות רק בקרים (אין אילוצים, פשוט נשמור בתור הגדרה לוגית בהמשך)
        // אופק – לא עובד לילות
        RecurringConstraint(7, 5, DayOfWeek.MONDAY, setOf(ShiftType.NIGHT), "לא עובד לילות"),
        RecurringConstraint(8, 5, DayOfWeek.WEDNESDAY, setOf(ShiftType.NIGHT), "לא עובד לילות"),

        // שני – לא זמינה בימי שלישי אחר הצהריים
        RecurringConstraint(9, 6, DayOfWeek.TUESDAY, setOf(ShiftType.NOON), "חוג ילדים"),

        // מסי – רק משמרות ערב (צוהריים/לילה)
        RecurringConstraint(10, 7, DayOfWeek.MONDAY, setOf(ShiftType.MORNING), "לא בקרים"),
        RecurringConstraint(11, 7, DayOfWeek.THURSDAY, setOf(ShiftType.MORNING), "לא בקרים"),

        // פולינה – לילות בלבד
        RecurringConstraint(12, 8, DayOfWeek.WEDNESDAY, setOf(ShiftType.MORNING), "רק לילות"),
        RecurringConstraint(13, 8, DayOfWeek.WEDNESDAY, setOf(ShiftType.NOON), "רק לילות"),

        // לאורה – לא זמינה בשבת
        RecurringConstraint(14, 9, DayOfWeek.SATURDAY, setOf(ShiftType.MORNING), "לא זמינה בשבת"),
        RecurringConstraint(15, 9, DayOfWeek.SATURDAY, setOf(ShiftType.NOON), "לא זמינה בשבת"),
        RecurringConstraint(16, 9, DayOfWeek.SATURDAY, setOf(ShiftType.NIGHT), "לא זמינה בשבת")
    )

    fun oneTimeConstraints(weekStart: LocalDate): List<Constraint> = listOf(
        Constraint(
            1,
            3,
            weekStart.plusDays(2),
            weekStart.plusDays(2),
            ConstraintType.DAY_OFF,
            null,
            "מבחן באוניברסיטה"
        ),
        Constraint(
            2,
            5,
            weekStart.plusDays(4),
            weekStart.plusDays(4),
            ConstraintType.DAY_OFF,
            null,
            "טיפול שיניים"
        ),
        Constraint(
            3,
            6,
            weekStart.plusDays(1),
            weekStart.plusDays(1),
            ConstraintType.DAY_OFF,
            null,
            "אירוע משפחתי"
        )
    )

    fun shiftsForWeek(weekStart: LocalDate): List<Shift> {
        val shifts = mutableListOf<Shift>()
        var idCounter = 100L
        for (i in 0..6) {
            val day = weekStart.plusDays(i.toLong())
            val dayOfWeek = day.dayOfWeek

            when (dayOfWeek) {
                DayOfWeek.FRIDAY -> {
                    shifts += Shift(idCounter++, 1L, day, ShiftType.MORNING, 2)
                    shifts += Shift(idCounter++, 1L, day, ShiftType.NOON, 1)
                }
                DayOfWeek.SATURDAY -> {
                    shifts += Shift(idCounter++, 1L, day, ShiftType.MORNING, 1)
                    shifts += Shift(idCounter++, 1L, day, ShiftType.NOON, 1)
                    shifts += Shift(idCounter++, 1L, day, ShiftType.NIGHT, 1)
                }
                else -> {
                    shifts += Shift(idCounter++, 1L, day, ShiftType.MORNING, 2)
                    shifts += Shift(idCounter++, 1L, day, ShiftType.NOON, 2)
                    shifts += Shift(idCounter++, 1L, day, ShiftType.NIGHT, 1)
                }
            }
        }
        return shifts
    }
}

fun main() {
    val weekStart = LocalDate.of(2025, 10, 19)
    val shifts = MockDataProvider.shiftsForWeek(weekStart)

    println("👥 עובדים: ${MockDataProvider.employees.size}")
    println("🧩 אילוצים קבועים: ${MockDataProvider.recurringConstraints.size}")
    println("📅 משמרות שנוצרו לשבוע:")
    shifts.groupBy { it.date.dayOfWeek }.forEach { (day, list) ->
        println(" - $day: ${list.size} משמרות (${list.joinToString { it.shiftType.name }})")
    }
}
