package com.example.smartschedule.domain.models

enum class UserStatus(val displayName : String) {
    ACTIVE("פעיל"),
    INACTIVE("לא פעיל"),
    PENDING("ממתין לאישור");

    /**
     * בודק אם המשתמש יכול להתחבר למערכת
     * רק משתמשים פעילים יכולים להתחבר
     */
    fun canLogin(): Boolean = this == ACTIVE

    /**
     * בודק אם המשתמש מופיע ברשימות ובסידורים
     * משתמשים לא פעילים לא מופיעים בשיבוץ
     */
    fun isVisible(): Boolean = this == ACTIVE || this == PENDING
}