package com.example.smartschedule.domain.common



import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteException
import com.example.smartschedule.domain.errors.user_error.UserError

/**
 * Executes a user-related database operation and wraps the result in a [Result] object.
 * Maps database exceptions to UserError types.
 */
suspend inline fun <T> safeUserDbOperation(
    operation: suspend () -> T
): Result<T> {
    return try {
        Result.Success(operation())
    } catch (e: UserError) {
        // Already a UserError, pass it through
        Result.Error(e)
    } catch (e: SQLiteException) {
        Result.Error(UserError.DatabaseCorrupted)
    } catch (e: SQLiteDatabaseCorruptException) {
        Result.Error(UserError.DatabaseCorrupted)
    } catch (e: Exception) {
        Result.Error(UserError.DatabaseError(e))
    }
}

/**
 * Extension function to convert UserError to user-friendly messages
 */
fun UserError.toUserFriendlyMessage(): String {
    return when (this) {
        is UserError.DatabaseCorrupted ->
            "מסד הנתונים פגום, נסה לאתחל את האפליקציה"

        is UserError.UserNotFound ->
            "משתמש לא נמצא במערכת"

        is UserError.InvalidCredentials ->
            "שם משתמש או סיסמה שגויים"

        is UserError.UserNotLoggedIn ->
            "משתמש לא מחובר למערכת"

        is UserError.DuplicateEmail ->
            "כתובת האימייל ${this.email} כבר קיימת במערכת"

        is UserError.PasswordHashFailure ->
            "שגיאה בעיבוד הסיסמה"

        is UserError.ValidationError ->
            "${this.field}: ${this.reason}"

        is UserError.DatabaseError ->
            "שגיאת מסד נתונים, נסה שוב"

        is UserError.NetworkUnavailable ->
            "אין חיבור לאינטרנט"

        is UserError.ServerError ->
            "שגיאת שרת (${this.code}), נסה שוב מאוחר יותר"
    }
}