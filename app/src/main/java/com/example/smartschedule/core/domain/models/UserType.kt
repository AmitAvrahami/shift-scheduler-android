package com.example.smartschedule.core.domain.models

enum class UserType(val displayName: String) {
    EMPLOYEE("עובד"),
    MANAGER("מנהל"),
    ADMIN("מנהל מערכת");

    // TODO: Consider refactoring to sealed class permissions system
    //       for more flexibility in future versions
    //       Discussion: 24/07/2025 - sealed class vs enum functions

    /**
     * בודק אם המשתמש יכול ליצור מנהלים חדשים
     * רק אדמין יכול ליצור מנהלים!
     */
    fun canCreateManagers(): Boolean = when (this) {
        EMPLOYEE, MANAGER -> false
        ADMIN -> true
    }

    /**
     * בודק אם המשתמש יכול ליצור ולערוך סידורי עבודה
     * זה הליבה של האפליקציה - רק מנהלים ומעלה!
     */
    fun canManageSchedules(): Boolean = when (this) {
        EMPLOYEE -> false
        MANAGER, ADMIN -> true
    }

    /**
     * בודק אם המשתמש יכול לאשר החלפות משמרות בין עובדים
     * עובד יכול לבקש החלפה, אבל רק מנהל יכול לאשר!
     */
    fun canApproveShiftSwaps(): Boolean = when (this) {
        EMPLOYEE -> false
        MANAGER, ADMIN -> true
    }

    /**
     * בודק אם המשתמש יכול לצפות בדוחות עובדים
     * מנהל רואה דוחות של העובדים שלו, אדמין רואה הכל
     */
    fun canViewReports(): Boolean = when (this) {
        EMPLOYEE -> false
        MANAGER, ADMIN -> true
    }

    /**
     * בודק אם המשתמש יכול לנהל עובדים (עריכה, מחיקה, הוספה)
     * זה כולל עריכת פרטים אישיים, העברת עובדים בין מחלקות וכו'
     */
    fun canManageEmployees(): Boolean = when (this) {
        EMPLOYEE -> false
        MANAGER, ADMIN -> true
    }

    /**
     * בודק אם המשתמש יכול להגדיר חוקים ואילוצים במערכת
     * דברים כמו: מקסימום שעות בשבוע, זמן מנוחה בין משמרות וכו'
     * זה רגיש מאוד - רק אדמין!
     */
    fun canManageSystemRules(): Boolean = when (this) {
        EMPLOYEE, MANAGER -> false
        ADMIN -> true
    }

    /**
     * בודק אם המשתמש יכול לצפות בסידורי עבודה
     * כולם יכולים לצפות - אבל כל אחד ברמה שלו:
     * עובד רואה רק את הסידור שלו, מנהל רואה את כולם
     */
    fun canViewSchedules(): Boolean = when (this) {
        EMPLOYEE, MANAGER, ADMIN -> true
    }
}