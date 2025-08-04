package com.example.smartschedule.core.domain.common



import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteException

/**
 * Executes a user-related database operation and wraps the result in a [Result] object.
 * Maps database exceptions to UserError types.
 */
suspend inline fun <T> safeUserDbOperation(
    operation: suspend () -> T
): Result<T> {
    return try {
        Result.Success(operation())
    } catch (e: com.example.smartschedule.core.domain.errors.user_error.UserError) {
        // Already a UserError, pass it through
        Result.Error(e)
    } catch (e: SQLiteException) {
        Result.Error(_root_ide_package_.com.example.smartschedule.core.domain.errors.user_error.UserError.DatabaseCorrupted)
    } catch (e: SQLiteDatabaseCorruptException) {
        Result.Error(_root_ide_package_.com.example.smartschedule.core.domain.errors.user_error.UserError.DatabaseCorrupted)
    } catch (e: Exception) {
        Result.Error(_root_ide_package_.com.example.smartschedule.core.domain.errors.user_error.UserError.DatabaseError(e))
    }
}

/**
 * Extension function to convert UserError to user-friendly messages
 */
fun com.example.smartschedule.core.domain.errors.user_error.UserError.toUserFriendlyMessage(): String {
    return when (this) {
        is com.example.smartschedule.core.domain.errors.user_error.UserError.DatabaseCorrupted ->
            "מסד הנתונים פגום, נסה לאתחל את האפליקציה"

        is com.example.smartschedule.core.domain.errors.user_error.UserError.UserNotFound ->
            "משתמש לא נמצא במערכת"

        is com.example.smartschedule.core.domain.errors.user_error.UserError.InvalidCredentials ->
            "שם משתמש או סיסמה שגויים"

        is com.example.smartschedule.core.domain.errors.user_error.UserError.UserNotLoggedIn ->
            "משתמש לא מחובר למערכת"

        is com.example.smartschedule.core.domain.errors.user_error.UserError.DuplicateEmail ->
            "כתובת האימייל ${this.email} כבר קיימת במערכת"

        is com.example.smartschedule.core.domain.errors.user_error.UserError.PasswordHashFailure ->
            "שגיאה בעיבוד הסיסמה"

        is com.example.smartschedule.core.domain.errors.user_error.UserError.ValidationError ->
            "${this.field}: ${this.reason}"

        is com.example.smartschedule.core.domain.errors.user_error.UserError.DatabaseError ->
            "שגיאת מסד נתונים, נסה שוב"

        is com.example.smartschedule.core.domain.errors.user_error.UserError.NetworkUnavailable ->
            "אין חיבור לאינטרנט"

        is com.example.smartschedule.core.domain.errors.user_error.UserError.ServerError ->
            "שגיאת שרת (${this.code}), נסה שוב מאוחר יותר"
    }
}