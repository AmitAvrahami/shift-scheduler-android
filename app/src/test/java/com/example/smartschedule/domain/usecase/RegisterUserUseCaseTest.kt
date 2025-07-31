package com.example.smartschedule.domain.usecase

import com.example.smartschedule.domain.common.Result
import com.example.smartschedule.domain.errors.user_error.UserError
import com.example.smartschedule.domain.models.UserType
import com.example.smartschedule.domain.repository.UserRepository
import com.example.smartschedule.domain.validation.ValidationResult
import com.example.smartschedule.testutils.TestDataFactory
import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class RegisterUserUseCaseTest {

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var registerUserUseCase: RegisterUserUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        registerUserUseCase = RegisterUserUseCase(userRepository)
    }

    @Test
    fun `when registering valid employee, should return success`() = runTest {
        val currentUser = TestDataFactory.createAdminUser()
        // Arrange
        val newUser = TestDataFactory.createUser(
            name = "עמית אברהמי",
            email = "amit@test.com",
            type = UserType.EMPLOYEE
        )
        val password = "Test123!"

        whenever(userRepository.registerUserWithResult(newUser, password))
            .thenReturn(Result.Success(newUser))

        // Act
        val result = registerUserUseCase.executeWithResult(currentUser, newUser, password)

        // Assert
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(newUser)
    }



    //Email validation: Invalid formats, empty strings, null values

    @Test //Invalid Format
    fun `when email format is invalid, should return validation error`() = runTest {
        // 🏗️ ARRANGE - כאן אנחנו בודקים כל מיני פורמטים לא תקינים
        val currentUser = TestDataFactory.createAdminUser()
        // Arrange
        val invalidEmails = listOf(
            "not-an-email",
            "@missing-domain.com",
            "missing-at-sign.com",
            "double@@domain.com",
            "spaces in@email.com",
            "missing.domain@",
            ""
        )

        val password = "ValidPass123!"

        // 🎬 ACT & ASSERT - כאן אנחנו רצים על כל אימייל לא תקין
        invalidEmails.forEach { invalidEmail ->
            val userWithInvalidEmail = TestDataFactory.createUser(email = invalidEmail)

            val result = registerUserUseCase.executeWithResult(currentUser,userWithInvalidEmail, password)

            // בודקים שכל אימייל לא תקין מחזיר שגיאה
            assertWithMessage("Expected error for email: $invalidEmail")
                .that((result.isError)).isTrue()
        }
    }
    @Test
    fun `when email validation fails, should return validation error`() = runTest {
        // Arrange
        val currentUser = TestDataFactory.createAdminUser()
        val userWithBadEmail = TestDataFactory.createUser(email = "invalid-email")
        val password = "ValidPass123!"

        // Act
        val result = registerUserUseCase.executeWithResult(currentUser,userWithBadEmail, password)

        // Assert
        assertThat(result.isError).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("פורמט האימייל אינו תקין")
    }
    @Test //Empty Email
    fun `when empty email provided, should return validation error`() = runTest {
        // 🏗️ ARRANGE
        val currentUser = TestDataFactory.createAdminUser()
        val userWithEmptyEmail = TestDataFactory.createUser(
            name = "עמית תקין",
            email = "" // אימייל ריק
        )
        val password = "ValidPass123!"

        // 🎬 ACT
        val result = registerUserUseCase.executeWithResult( currentUser,userWithEmptyEmail, password)

        // 🔍 ASSERT
        assertThat(result.isError).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("אימייל הוא שדה חובה")
    }
    @Test //Duplicate Email
    fun `when duplicate email provided, should return DuplicateEmail error`() = runTest {
        // 🏗️ ARRANGE - הכנת התרחיש
        val currentUser = TestDataFactory.createAdminUser()
        val existingUser = TestDataFactory.createUser(
            email = "amit@existing.com"
        )
        val password = "ValidPass123!"

        whenever(userRepository.registerUserWithResult(any(), any()))
            .thenReturn(Result.Error(UserError.DuplicateEmail(existingUser.email)))

        // 🎬 ACT
        val result = registerUserUseCase.executeWithResult(currentUser,existingUser, password)

        // 🔍 ASSERT
        assertThat(result.isError).isTrue()

        // כאן אנחנו בודקים שקיבלנו בדיוק את סוג השגיאה שציפינו לה
        val error = result.exceptionOrNull()
        assertThat(error).isInstanceOf(UserError.DuplicateEmail::class.java)

        // ועוד יותר ספציפי - שהשגיאה מכילה את האימייל הבעייתי
        val duplicateError = error as UserError.DuplicateEmail
        assertThat(duplicateError.email).isEqualTo(existingUser.email)
    }

    //Password validation: Weak passwords, empty passwords, special characters

    @Test//All
    fun `when password is weak, should return validation error`() = runTest {
        val currentUser = TestDataFactory.createAdminUser()
        // 🏗️ ARRANGE
        val user = TestDataFactory.createUser()
        val weakPasswords = listOf(
            "123",          // קצר מדי
            "password",     // רק אותיות קטנות
            "PASSWORD",     // רק אותיות גדולות
            "12345678",     // רק מספרים
            "Pass1",        // חסר תו מיוחד
            "Pass@",        // קצר מדי עם תו מיוחד
            ""              // ריק
        )

        // 🎬 ACT & ASSERT
        weakPasswords.forEach { weakPassword ->
            val result = registerUserUseCase.executeWithResult(currentUser,user, weakPassword)

                assertWithMessage("Expected error for weak password: '$weakPassword'")
                    .that(result.isError).isTrue()

            // בודקים שזה באמת שגיאת validation ולא משהו אחר
            assertThat(result.exceptionOrNull())
                .isInstanceOf(IllegalArgumentException::class.java)
        }
    }
    @Test //Weak Password
    fun `when password validation fails, should return validation error`() = runTest {
        // Arrange
        val currentUser = TestDataFactory.createAdminUser()
        val user = TestDataFactory.createUser()
        val weakPassword = "123"

        // Act
        val result = registerUserUseCase.executeWithResult(currentUser,user, weakPassword)

        // Assert
        assertThat(result.isError).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("סיסמה חייבת לכלול")
    }
    @Test //Empty Password
    fun `when empty password provided, should return validation error`() = runTest {
        // 🏗️ ARRANGE
        val currentUser = TestDataFactory.createAdminUser()
        val user = TestDataFactory.createUser()
        val emptyPassword = "" // סיסמה ריקה

        // 🎬 ACT
        val result = registerUserUseCase.executeWithResult(currentUser,user, emptyPassword)

        // 🔍 ASSERT
        assertThat(result.isError).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("סיסמה לא יכולה להיות ריקה")
    }
    @Test //Special Characters
    fun `when password hashing fails, should return PasswordHashFailure error`() = runTest {
        // 🏗️ ARRANGE
        val currentUser = TestDataFactory.createAdminUser()
        val user = TestDataFactory.createUser()
        val problematicPassword = "SomePassword123!"

        val hashingException = RuntimeException("Encryption algorithm failed")
        whenever(userRepository.registerUserWithResult(any(), any()))
            .thenReturn(Result.Error(UserError.PasswordHashFailure(hashingException)))

        // 🎬 ACT
        val result = registerUserUseCase.executeWithResult(currentUser,user, problematicPassword)

        // 🔍 ASSERT
        assertThat(result.isError).isTrue()

        val error = result.exceptionOrNull()
        assertThat(error).isInstanceOf(UserError.PasswordHashFailure::class.java)

        // בודקים שהשגיאה המקורית נשמרה בתוך השגיאה שלנו
        val hashFailure = error as UserError.PasswordHashFailure
        assertThat(hashFailure.originalError).isEqualTo(hashingException)
    }

    //Name validation: Empty names, special characters, length limits

    @Test //Empty Name
    fun `when empty name provided, should return validation error`() = runTest {
        // 🏗️ ARRANGE
        val currentUser = TestDataFactory.createAdminUser()
        val userWithEmptyName = TestDataFactory.createUser(
            name = "", // שם ריק - זה אמור לעצור את הרישום
            email = "valid@test.com"
        )
        val password = "ValidPass123!"

        // 🎬 ACT
        val result = registerUserUseCase.executeWithResult(currentUser,userWithEmptyName, password)

        // 🔍 ASSERT
        assertThat(result.isError).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(result.exceptionOrNull()?.message).contains("השדה שם לא יכול להיות ריק")
    }
    @Test //length limits
    fun `when name validation is to shourt, should return validation error`() = runTest {
        // Arrange
        val currentUser = TestDataFactory.createAdminUser()
        val userWithBadName = TestDataFactory.createUser(name = "ab") // שם קצר מדי
        val password = "ValidPass123!"

        // Act
        val result = registerUserUseCase.executeWithResult(currentUser,userWithBadName, password)

        // Assert
        assertThat(result.isError).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("שם יכול לכלול רק אותיות")
    }
    @Test //Special Characters
    fun `when name contains special characters, should return validation error`() = runTest {
        // 🏗️ ARRANGE
        val currentUser = TestDataFactory.createAdminUser()
        val userWithSpecialChars = TestDataFactory.createUser(name = "John Doe!")
        val password = "ValidPass123!"

        // 🎬 ACT
        val result = registerUserUseCase.executeWithResult(currentUser,userWithSpecialChars, password)

        // 🔍 ASSERT
        assertThat(result.isError).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)

    }

    //User type validation: Invalid enum values, permission constraints

    @Test //permission constraints - ADMIN creates MANAGER
    fun `when ADMIN creates MANAGER, should succeed`() = runTest {
        // 🏗️ ARRANGE
        val adminUser = TestDataFactory.createAdminUser() // המשתמש הנוכחי
        val newManager = TestDataFactory.createUser(
            type = UserType.MANAGER,
            name = "מנהל חדש",
            email = "manager@test.com"
        )
        val password = "SecurePass123!"

        whenever(userRepository.registerUserWithResult(any(), any()))
            .thenReturn(Result.Success(newManager))

        // 🎬 ACT - עכשיו עם 3 פרמטרים
        val result = registerUserUseCase.executeWithResult(adminUser, newManager, password)

        // 🔍 ASSERT
        assertThat(result.isSuccess).isTrue()
    }
    @Test //permission constraints - EMPLOYEE tries to create MANAGER
    fun `when EMPLOYEE tries to create MANAGER, should fail`() = runTest {
        // 🏗️ ARRANGE
        val employeeUser = TestDataFactory.createUser(type = UserType.EMPLOYEE) // עובד רגיל
        val newManager = TestDataFactory.createUser(
            type = UserType.MANAGER,
            name = "ניסיון ליצור מנהל",
            email = "attempt@test.com"
        )
        val password = "SecurePass123!"

        // 🎬 ACT
        val result = registerUserUseCase.executeWithResult(employeeUser, newManager, password)

        // 🔍 ASSERT
        assertThat(result.isError).isTrue()
        assertThat(result.exceptionOrNull()?.message)
            .contains("רק מנהל מערכת יכול ליצור משתמש מנהל")
    }
    @Test //permission constraints - MANAGER tries to create ADMIN
    fun `when MANAGER tries to create ADMIN, should fail`() = runTest {
        // 🏗️ ARRANGE
        val managerUser = TestDataFactory.createManagerUser() // מנהל רגיל
        val newAdmin = TestDataFactory.createUser(
            type = UserType.ADMIN,
            name = "ניסיון ליצור אדמין",
            email = "admin-attempt@test.com"
        )
        val password = "SecurePass123!"

        // 🎬 ACT
        val result = registerUserUseCase.executeWithResult(managerUser, newAdmin, password)

        // 🔍 ASSERT
        assertThat(result.isError).isTrue()
        assertThat(result.exceptionOrNull()?.message)
            .contains("רק מנהל מערכת יכול ליצור מנהל מערכת אחר")
    }
    @Test //permission constraints - EMPLOYEE tries to create EMPLOYEE
    fun `when anyone creates EMPLOYEE, should succeed if data is valid`() = runTest {
        // 🏗️ ARRANGE - בודקים שכל סוג משתמש יכול ליצור EMPLOYEE
        val userTypes = listOf(
            TestDataFactory.createUser(type = UserType.EMPLOYEE),
            TestDataFactory.createManagerUser(),
            TestDataFactory.createAdminUser()
        )

        val newEmployee = TestDataFactory.createUser(
            type = UserType.EMPLOYEE,
            name = "עובד חדש",
            email = "employee@test.com"
        )
        val password = "SecurePass123!"

        whenever(userRepository.registerUserWithResult(any(), any()))
            .thenReturn(Result.Success(newEmployee))

        // 🎬 ACT & ASSERT - כל סוג משתמש אמור להצליח ליצור EMPLOYEE
        userTypes.forEach { currentUser ->
            val result = registerUserUseCase.executeWithResult(currentUser, newEmployee, password)

                assertWithMessage("Failed for user type: ${currentUser.type}")
                    .that(result.isSuccess).isTrue()
        }
    }

    //Error Propagation

    @Test //Unexpected Exception
    fun `when repository throws unexpected exception, should handle gracefully`() = runTest {
        // 🏗️ ARRANGE - הכנת תרחיש כאוטי
        val currentUser = TestDataFactory.createAdminUser()
        val user = TestDataFactory.createUser()
        val password = "ValidPass123!"

        // כאן אנחנו מדמים מצב שהRepository זורק שגיאה שלא ציפינו לה
        // זה יכול להיות OutOfMemoryError, NetworkException, או כל דבר אחר
        val unexpectedException = RuntimeException("Something went terribly wrong!")
        whenever(userRepository.registerUserWithResult(any(), any()))
            .thenThrow(unexpectedException)

        // 🎬 ACT
        val result = registerUserUseCase.executeWithResult(currentUser,user, password)

        // 🔍 ASSERT - בודקים שהUse Case לא קורס אלא מטפל בשגיאה בצורה מסודרת
        assertThat(result.isError).isTrue()

        // כאן אנחנו בודקים שהשגיאה המקורית נשמרה
        // זה חשוב לdebug - אנחנו רוצים לדעת מה באמת השתבש
        val error = result.exceptionOrNull()
        assertThat(error).isNotNull()

        // ואנחנו יכולים לבדוק שהודעת השגיאה מועילה למפתח
        println("Caught error: ${error?.message}")
    }

    //Test database failure scenarios

    @Test //Database Timeout Failure
    fun `when database operation times out, should return appropriate error`() = runTest {
        // 🏗️ ARRANGE
        val currentUser = TestDataFactory.createAdminUser()
        val user = TestDataFactory.createUser()
        val password = "ValidPass123!"

        // מדמים timeout - זה קורה כשמסד הנתונים עמוס או הרשת איטית
        val timeoutException = java.util.concurrent.TimeoutException("Database operation timed out after 30 seconds")
        whenever(userRepository.registerUserWithResult(any(), any()))
            .thenReturn(Result.Error(UserError.DatabaseError(timeoutException)))

        // 🎬 ACT
        val result = registerUserUseCase.executeWithResult(currentUser,user, password)

        // 🔍 ASSERT
        assertThat(result.isError).isTrue()

        val error = result.exceptionOrNull()
        assertThat(error).isInstanceOf(UserError.DatabaseError::class.java)

        // בודקים שהשגיאה המקורית (הtimeout) נשמרה
        val dbError = error as UserError.DatabaseError
        assertThat(dbError.originalError).isInstanceOf(java.util.concurrent.TimeoutException::class.java)
    }
    @Test //Database Corrupted
    fun `when database is corrupted, should return DatabaseCorrupted error`() = runTest {
        // 🏗️ ARRANGE
        val currentUser = TestDataFactory.createAdminUser()
        val user = TestDataFactory.createUser()
        val password = "ValidPass123!"

        whenever(userRepository.registerUserWithResult(any(), any()))
            .thenReturn(Result.Error(UserError.DatabaseCorrupted))

        // 🎬 ACT
        val result = registerUserUseCase.executeWithResult(currentUser,user, password)

        // 🔍 ASSERT
        assertThat(result.isError).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(UserError.DatabaseCorrupted::class.java)
    }


    //Integration Test

    @Test //Complex Validation Flow
    fun `when registering user with complex validation flow, should validate in correct order`() = runTest {
        // 🏗️ ARRANGE - תרחיש מורכב שבודק את כל השלבים
        val currentUser = TestDataFactory.createAdminUser()
        val user = TestDataFactory.createUser(
            name = "עמית אברהמי",
            email = "amit@valid-domain.com",
            type = UserType.EMPLOYEE
        )
        val password = "ComplexPass123!@#"

        // מדמים הצלחה מהRepository רק אחרי שכל הvalidation עבר
        whenever(userRepository.registerUserWithResult(any(), any()))
            .thenReturn(Result.Success(user))

        // 🎬 ACT
        val result = registerUserUseCase.executeWithResult(currentUser,user, password)

        // 🔍 ASSERT - בודקים שהכל עבד בסדר הנכון
        assertThat(result.isSuccess).isTrue()

        // בודקים שהRepository נקרא בדיוק פעם אחת עם הפרמטרים הנכונים
        verify(userRepository, times(1)).registerUserWithResult(user, password)

        // בודקים שהתוצאה מכילה את המשתמש הנכון
        assertThat(result.getOrNull()).isEqualTo(user)
    }
    @Test //Retry after failure
    fun `when validation passes but repository fails, should not retry`() = runTest {
        // 🏗️ ARRANGE
        val currentUser = TestDataFactory.createAdminUser()
        val user = TestDataFactory.createUser()
        val password = "ValidPass123!"

        // מדמים מצב שהvalidation עובר אבל הRepository נכשל
        // זה תרחיש נפוץ - הנתונים תקינים אבל יש בעיה טכנית
        whenever(userRepository.registerUserWithResult(any(), any()))
            .thenReturn(Result.Error(UserError.NetworkUnavailable))

        // 🎬 ACT
        val result = registerUserUseCase.executeWithResult(currentUser,user, password)

        // 🔍 ASSERT
        assertThat(result.isError).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(UserError.NetworkUnavailable::class.java)

        // בודקים שלא היו ניסיונות חוזרים - ה-Use Case לא אמור לעשות retry
        verify(userRepository, times(1)).registerUserWithResult(any(), any())

        // זה חשוב כי retry זה אחריות של שכבה אחרת (Repository או Infrastructure)
    }

    //Complex Edge Cases

    @Test //Null Values
    fun `when user tries to register with null values, should handle gracefully`() = runTest {
        // 🏗️ ARRANGE - כאן אנחנו בודקים מה קורה עם null values
        // זה יכול לקרות אם יש בעיה בdeserialization או אם מישהו שולח נתונים מוזרים

        // אנחנו לא יכולים ליצור User עם null במקומות שהם non-null
        // אז אנחנו בודקים עם ערכים שמתנהגים כמו null
        val currentUser = TestDataFactory.createAdminUser()
        val userWithNullLikeValues = TestDataFactory.createUser(
            name = "null", // מחרוזת שנראית כמו null
            email = "null@null.com"
        )
        val password = "ValidPass123!"

        // 🎬 ACT
         whenever(userRepository.registerUserWithResult(userWithNullLikeValues, password))
            .thenReturn(Result.Success(userWithNullLikeValues))

        val result = registerUserUseCase.executeWithResult(currentUser,userWithNullLikeValues, password)

        // 🔍 ASSERT - המערכת אמורה לטפל בזה בצורה מסודרת
        // כאן אנחנו בעצם בודקים שה-validation לא מתבלבל מערכים מוזרים
        assertThat(result.isSuccess).isTrue() // "null" זה string תקין, לא null אמיתי
    }
    @Test //Long Values
    fun `when user tries to register with extremely long values, should validate properly`() = runTest {
        // 🏗️ ARRANGE - בודקים מה קורה עם ערכים ארוכים מאוד
        val currentUser = TestDataFactory.createAdminUser()
        val extremelyLongName = "א".repeat(10000) // שם של 10,000 תווים
        val extremelyLongEmail = "a".repeat(200) + "@" + "b".repeat(200) + ".com"

        val userWithLongValues = TestDataFactory.createUser(
            name = extremelyLongName,
            email = extremelyLongEmail
        )
        val password = "ValidPass123!"

        whenever(userRepository.registerUserWithResult(userWithLongValues,password))
            .thenReturn(Result.Success(userWithLongValues)) //TODO: Decide if to except or not

        // 🎬 ACT
        val result = registerUserUseCase.executeWithResult(currentUser,userWithLongValues, password)

        // 🔍 ASSERT
        // כאן התוצאה תלויה בlogic הvalidation שלך
        // אם יש הגבלת אורך - אמור להיות error
        // אם אין - אמור להצליח
        // זה מזכיר לנו לחשוב על הגבלות אורך!
        println("Result for extremely long values: ${result.isSuccess}")
    }
    @Test //Long Running Task
    fun `when registering user, should complete within reasonable time`() = runTest {
        // 🏗️ ARRANGE
        val currentUser = TestDataFactory.createAdminUser()
        val user = TestDataFactory.createUser()
        val password = "ValidPass123!"

        // מדמים Repository שמחזיר תשובה מהירה
        whenever(userRepository.registerUserWithResult(any(), any()))
            .thenReturn(Result.Success(user))

        // 🎬 ACT - מודדים זמן ביצוע
        val startTime = System.currentTimeMillis()
        val result = registerUserUseCase.executeWithResult(currentUser,user, password)
        val endTime = System.currentTimeMillis()

        val executionTime = endTime - startTime

        // 🔍 ASSERT
        assertThat(result.isSuccess).isTrue()

        // בודקים שהביצוע לקח פחות מ-100 מילישניות
        // זה סביר לlogic עסקי פשוט בלי פניות רשת אמיתיות
        assertThat(executionTime).isLessThan(100)

        println("Execution time: ${executionTime}ms")
    }
}