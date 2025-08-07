package com.example.smartschedule.auth.domain.models.user

enum class Permissions {
    // 🔐 ניהול הרשאות ותפקדים
    DEFINE_ROLES,
    DEFINE_PERMISSIONS,

    // 👥 ניהול עובדים
    EMPLOYEE_MANAGEMENT,

    // 🕒 ניהול משמרות ולוחות זמנים
    SHIFT_MANAGEMENT,
    SCHEDULE_MANAGEMENT,
    DEFINE_SHIFT_TYPES,
    PUBLISH_SCHEDULE_NOTIFICATION,
    AUTO_ASSIGN_SHIFTS,
    SHIFT_SWAP_MANAGEMENT,

    // ⚙️ חוקים והגדרות
    RULE_CONFIGURATION,
    BUSINESS_RULES_DEFINITION,

    // 📊 דוחות ונתונים
    REPORT_GENERATION,
    VIEW_PERSONAL_REPORT,

    // 🔎 תצוגות משתמש
    VIEW_PERSONAL_SCHEDULE,
    VIEW_SCHEDULE_CONFLICTS,
    VIEW_SYSTEM_ACTIVITY_LOG,

    // 🔔 אינטראקציה של משתמשים
    RECEIVE_NOTIFICATIONS,
    ENTERING_CONSTRAINT,
    SHIFT_REQUEST_SWAP,
    SUBMIT_SHIFT_CHANGE_REQUEST;

    object PermissionGroups {

        val RolePermissions = setOf(
            DEFINE_ROLES,
            DEFINE_PERMISSIONS
        )

        val EmployeePermissions = setOf(
            EMPLOYEE_MANAGEMENT
        )

        val ShiftPermissions = setOf(
            SHIFT_MANAGEMENT,
            SCHEDULE_MANAGEMENT,
            DEFINE_SHIFT_TYPES,
            PUBLISH_SCHEDULE_NOTIFICATION,
            AUTO_ASSIGN_SHIFTS,
            SHIFT_SWAP_MANAGEMENT
        )

        val RulePermissions = setOf(
            RULE_CONFIGURATION,
            BUSINESS_RULES_DEFINITION
        )

        val ReportPermissions = setOf(
            REPORT_GENERATION,
            VIEW_PERSONAL_REPORT
        )

        val ViewPermissions = setOf(
            VIEW_PERSONAL_SCHEDULE,
            VIEW_SYSTEM_ACTIVITY_LOG,
            VIEW_SCHEDULE_CONFLICTS
        )

        val UserInteractionPermissions = setOf(
            RECEIVE_NOTIFICATIONS,
            ENTERING_CONSTRAINT,
            SHIFT_REQUEST_SWAP,
            SUBMIT_SHIFT_CHANGE_REQUEST
        )

    }


}