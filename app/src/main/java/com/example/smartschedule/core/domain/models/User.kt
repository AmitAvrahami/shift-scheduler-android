package com.example.smartschedule.core.domain.models

import java.time.LocalDateTime

open class User(
    open val id: String,
    open val name: String,
    open val email: String,
    open val type : UserType,
    open val status: UserStatus = UserStatus.ACTIVE,
    open val createdDate: LocalDateTime = LocalDateTime.now()
){
    /**
     * בודק אם המשתמש יכול להתחבר למערכת
     * מבוסס על הסטטוס שלו
     */
    fun canLogin(): Boolean = status.canLogin()

    /**
     * בודק אם המשתמש מופיע ברשימות ובסידורים
     */
    fun isVisible(): Boolean = status.isVisible()

    /**
     * מחזיר את הזמן שעבר מאז יצירת המשתמש
     * שימושי לדוחות ולבדיקות
     */
    fun getAccountAge(): Long {
        return java.time.Duration.between(createdDate, LocalDateTime.now()).toDays()
    }
}