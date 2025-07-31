
package com.example.smartschedule.testutils.matchers

import com.example.smartschedule.domain.common.Result
import com.example.smartschedule.domain.errors.employee_error.EmployeeError
import com.example.smartschedule.domain.errors.user_error.UserError
import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject
import com.google.common.truth.Truth.assertAbout


// ==============================================================================
// SuccessResultMatcher - מטפל בבדיקת Result.Success
// ==============================================================================

class SuccessResultSubject<T> private constructor(
    metadata: FailureMetadata,
    private val actual: Result<T>?
) : Subject(metadata, actual) {

    fun hasValue(expected: T) {
        check("isSuccess").that(actual?.isSuccess).isTrue()
        check("data").that(actual?.getOrNull()).isEqualTo(expected)
    }

    fun isSuccess() {
        check("isSuccess").that(actual?.isSuccess).isTrue()
    }

    fun hasValueThat(predicate: (T) -> Boolean) {
        isSuccess()
        val data = actual?.getOrNull()
        check("data meets condition").that(data?.let(predicate)).isTrue()
    }

    companion object {
        fun <T> successResults(): Subject.Factory<SuccessResultSubject<T>, Result<T>> {
            return Subject.Factory { metadata, actual -> SuccessResultSubject(metadata, actual) }
        }
    }
}

// ==============================================================================
// ErrorResultMatcher - מטפל בבדיקת Result.Error
// ==============================================================================

class ErrorResultSubject<T> private constructor(
    metadata: FailureMetadata,
     val actual: Result<T>?
) : Subject(metadata, actual) {


    fun <E : Throwable> hasErrorOfType(expectedClass: Class<E>) {
        check("isError").that(actual?.isError).isTrue()
        val exception = actual?.exceptionOrNull()
        check("error type").that(exception).isInstanceOf(expectedClass)
    }


    fun hasMessageContaining(expectedMessage: String) {
        check("isError").that(actual?.isError).isTrue()
        val exception = actual?.exceptionOrNull()
        check("error message").that(exception?.message).contains(expectedMessage)
    }

    fun hasUserError(expectedErrorType: Class<out UserError>) {
        check("isError").that(actual?.isError).isTrue()
        val exception = actual?.exceptionOrNull()
        check("is UserError").that(exception).isInstanceOf(UserError::class.java)
        check("specific UserError type").that(exception).isInstanceOf(expectedErrorType)
    }

    fun hasEmployeeError(expectedErrorType: Class<out EmployeeError>) {
        check("isError").that(actual?.isError).isTrue()
        val exception = actual?.exceptionOrNull()
        check("is EmployeeError").that(exception).isInstanceOf(EmployeeError::class.java)
        check("specific EmployeeError type").that(exception).isInstanceOf(expectedErrorType)
    }

    companion object {
        fun <T> errorResults(): Subject.Factory<ErrorResultSubject<T>, Result<T>> {
            return Subject.Factory { metadata, actual -> ErrorResultSubject(metadata, actual) }
        }
    }
}

// ==============================================================================
// DatabaseErrorSimulator - מדמה כשלי מסד נתונים באופן עקבי
// ==============================================================================


object DatabaseErrorSimulator {
    fun getCommonDatabaseErrors(): List<Exception> {
        return listOf(
            android.database.sqlite.SQLiteException("Database is locked"),
            android.database.sqlite.SQLiteDatabaseCorruptException("Database corrupted"),
            java.util.concurrent.TimeoutException("Database operation timeout"),
            java.io.IOException("Disk full"),
            RuntimeException("Connection lost"),
            IllegalStateException("Database not initialized")
        )
    }


    fun getRandomDatabaseError(): Exception {
        val errors = getCommonDatabaseErrors()
        return errors.random()
    }


    fun createTimeoutError(timeoutSeconds: Int): Exception {
        return java.util.concurrent.TimeoutException(
            "Database operation timed out after $timeoutSeconds seconds"
        )
    }

    fun createCorruptionError(tableName: String): Exception {
        return android.database.sqlite.SQLiteDatabaseCorruptException(
            "Table '$tableName' is corrupted and cannot be read"
        )
    }
}


fun <T> assertThatResult(result: Result<T>) = assertAbout(SuccessResultSubject.successResults<T>()).that(result)

fun <T> assertThatError(result: Result<T>) = assertAbout(ErrorResultSubject.errorResults<T>()).that(result)

// ==============================================================================
// דוגמאות שימוש - איך הקוד הזה הופך את הבדיקות לקלות יותר:
// ==============================================================================

/*
// לפני - קוד ארוך ומסובך:
@Test
fun `test user registration`() = runTest {
    val result = userRepository.registerUser(user, "password")
    assertThat(result.isSuccess).isTrue()
    assertThat(result.getOrNull()).isEqualTo(user)
    assertThat(result.getOrNull()?.name).isEqualTo("עמית אברהמי")
}

// אחרי - קוד קצר וברור:
@Test
fun `test user registration`() = runTest {
    val result = userRepository.registerUser(user, "password")
    
    assertThatResult(result)
        .hasValue(user)
        .hasValueThat { it.name == "עמית אברהמי" }
}

// דוגמה לבדיקת שגיאות:
@Test
fun `test duplicate email error`() = runTest {
    val result = userRepository.registerUser(user, "password")
    
    assertThatError(result)
        .hasUserError(UserError.DuplicateEmail::class.java)
        .hasMessageContaining("already exists")
}

// דוגמה לשימוש בסימולטור שגיאות:
@Test
fun `test random database failures`() = runTest {
    repeat(10) { // בודק 10 פעמים עם שגיאות אקראיות
        val randomError = DatabaseErrorSimulator.getRandomDatabaseError()
        whenever(userDao.insertUser(any())).thenThrow(randomError)
        
        val result = userRepository.registerUser(user, "password")
        assertThatError(result).hasErrorOfType<UserError.DatabaseError>()
    }
}
*/