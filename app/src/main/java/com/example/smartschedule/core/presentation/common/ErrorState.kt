package com.example.smartschedule.core.presentation.common

import android.database.sqlite.SQLiteException
import java.net.UnknownHostException

sealed class ErrorState(val message: String) {
    object NetworkError : ErrorState("בעיית חיבור לאינטרנט")
    object DatabaseError : ErrorState("שגיאה בהעלה/שמירת הנתונים")
    data class ValidationError(val field: String, val reason: String) : ErrorState("$field: $reason")
    data class UnknownError(val exception: String) : ErrorState("שגיאה לא צפויה: $exception")

    companion object {
        fun fromThrowable(throwable: Throwable): ErrorState = when (throwable) {
            is IllegalArgumentException -> ValidationError("validation", throwable.message ?: "")
            is UnknownHostException -> NetworkError
            is SQLiteException -> DatabaseError
            else -> UnknownError(throwable.message ?: "Unknown error")
        }
}
    }
