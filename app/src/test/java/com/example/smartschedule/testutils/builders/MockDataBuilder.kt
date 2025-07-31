// File: app/src/test/java/com/example/smartschedule/testutils/builders/MockDataBuilder.kt

package com.example.smartschedule.testutils.builders

import com.example.smartschedule.data.database.dao.UserDao
import com.example.smartschedule.data.database.dao.EmployeeDao
import com.example.smartschedule.data.database.dao.ShiftDao
import com.example.smartschedule.data.database.entities.UserEntity
import com.example.smartschedule.data.database.entities.EmployeeEntity
import com.example.smartschedule.data.database.entities.ShiftEntity
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.*

object MockDataBuilder {

    // ==============================================================================
    // UserDao Builders - בנאי מוקים של UserDao
    // ==============================================================================


     fun userDaoReturning(user: UserEntity?): UserDao = mock {
        onBlocking { getUserByEmail(any()) } doReturn user
         onBlocking { getUserById(any()) } doReturn user
         onBlocking { insertUser(any()) } doReturn Unit // Unit זה "void" בקוטלין
         onBlocking { isEmailExists(any()) } doReturn if (user != null) 1 else 0
    }


    fun userDaoWithSuccessfulLogin(user: UserEntity, passwordHash: String): UserDao = mock {
        onBlocking { login(user.email, passwordHash) } doReturn user
        onBlocking { getUserByEmail(user.email) } doReturn user
        onBlocking { isEmailExists(user.email) } doReturn 1
    }


    fun userDaoWithFailedLogin(): UserDao = mock {
        onBlocking { login(any(), any()) } doReturn null
        onBlocking { getUserByEmail(any()) } doReturn null
        onBlocking { isEmailExists(any()) } doReturn 0
    }


    fun userDaoWithDatabaseError(error: Exception = RuntimeException("Database error")): UserDao = mock {
        wheneverBlocking{mock.insertUser(any())}.thenThrow(error)
        wheneverBlocking{mock.getUserByEmail(any())}.thenThrow(error)
        wheneverBlocking{mock.login(any(), any())}.thenThrow(error)
    }


    fun userDaoWithUsersList(users: List<UserEntity>): UserDao = mock {
        on { getAllUsers() } doReturn flowOf(users)

        users.forEach { user ->
            onBlocking { getUserByEmail(user.email) } doReturn user
            onBlocking { getUserById(user.id) } doReturn user
        }
    }

    // ==============================================================================
    // EmployeeDao Builders - בנאי מוקים של EmployeeDao
    // ==============================================================================

    fun employeeDaoReturning(employee: EmployeeEntity?): EmployeeDao = mock {
        onBlocking { getEmployeeById(any()) } doReturn employee
        onBlocking { getEmployeeByEmployeeNumber(any()) } doReturn employee
        onBlocking { insertEmployee(any()) } doReturn Unit
        onBlocking { deleteEmployee(any()) } doReturn Unit
    }

    fun employeeDaoWithEmployeesList(employees: List<EmployeeEntity>): EmployeeDao = mock {
        on { getAllEmployees() } doReturn flowOf(employees)

        employees.forEach { employee ->
            onBlocking { getEmployeeById(employee.id) } doReturn employee
            onBlocking { getEmployeeByEmployeeNumber(employee.employeeNumber) } doReturn employee
        }
    }


    fun employeeDaoWithDuplicateEmployeeNumber(duplicateNumber: String): EmployeeDao = mock {
        wheneverBlocking {  mock.getEmployeeByEmployeeNumber(duplicateNumber)}
            .thenReturn(EmployeeEntity("existing", "Existing", "email", duplicateNumber))

        onBlocking { insertEmployee(any()) } doReturn Unit
    }

    // ==============================================================================
    // ShiftDao Builders - בנאי מוקים של ShiftDao
    // ==============================================================================

    fun shiftDaoWithShiftsList(shifts: List<ShiftEntity>): ShiftDao = mock {
        on { getAllShifts() } doReturn flowOf(shifts)

        shifts.forEach { shift ->
            onBlocking { getShiftById(shift.id) } doReturn shift
        }

        onBlocking { insertShift(any()) } doReturn Unit
        onBlocking { deleteShift(any()) } doReturn Unit
    }

    fun shiftDaoWithEmployeeShifts(employeeId: String, shifts: List<ShiftEntity>): ShiftDao = mock {
        on { getShiftsByEmployee(employeeId) } doReturn flowOf(shifts)
        on { getAllShifts() } doReturn flowOf(shifts)
        onBlocking { insertShift(any()) } doReturn Unit
    }

    // ==============================================================================
    // Complex Scenario Builders - בנאי תרחישים מורכבים
    // ==============================================================================

    fun createWorkingSystemDaos(
        users: List<UserEntity> = emptyList(),
        employees: List<EmployeeEntity> = emptyList(),
        shifts: List<ShiftEntity> = emptyList()
    ): Triple<UserDao, EmployeeDao, ShiftDao> {

        val userDao = userDaoWithUsersList(users)
        val employeeDao = employeeDaoWithEmployeesList(employees)
        val shiftDao = shiftDaoWithShiftsList(shifts)

        return Triple(userDao, employeeDao, shiftDao)
    }

    fun createFailingSystemDaos(error: Exception = RuntimeException("System failure")): Triple<UserDao, EmployeeDao, ShiftDao> {

        val userDao = userDaoWithDatabaseError(error)
        val employeeDao: EmployeeDao = mock {
            whenever(mock.getAllEmployees()).thenThrow(error)
            wheneverBlocking{mock.insertEmployee(any())}.thenThrow(error)
        }
        val shiftDao: ShiftDao = mock {
            whenever(mock.getAllShifts()).thenThrow(error)
            wheneverBlocking { mock.insertShift(any())}.thenThrow(error)
        }

        return Triple(userDao, employeeDao, shiftDao)
    }

    // ==============================================================================
    // Helper Functions - פונקציות עזר לתרחישים נפוצים
    // ==============================================================================

    fun createSlowDao(delayMs: Long = 1000): UserDao = mock {
        onBlocking { insertUser(any()) } doAnswer {
            Thread.sleep(delayMs)
            Unit
        }

        onBlocking { getUserByEmail(any()) } doAnswer {
            Thread.sleep(delayMs)
            null
        }
    }

    fun createCountingDao(): Pair<UserDao, () -> Int> {
        var callCount = 0

        val dao: UserDao = mock {
            onBlocking { insertUser(any()) } doAnswer {
                callCount++
                Unit
            }
        }

        val getCallCount = { callCount }
        return Pair(dao, getCallCount)
    }
}

// ==============================================================================
// Extension Functions - פונקציות נוחות לשימוש מהיר
// ==============================================================================

/**
 * Extension Function שהופכת כל DAO למוק שזורק שגיאות
 */
//fun <T> T.withDatabaseError(error: Exception): T where T : Any {
//    // Reflection - גישה למטא-נתונים של האובייקט בזמן ריצה
//    // זה נושא מתקדם, אבל הרעיון הוא שאנחנו "מתחפשים" לאובייקט המקורי
//    return mock<T> {
//        // כאן נוכל להוסיף הגדרות כלליות לכל DAO
//    }
//}

// ==============================================================================
// דוגמאות שימוש מעשיות:
// ==============================================================================

/*
class UserRepositoryImplTest : BaseRepositoryTest() {
    
    private lateinit var repository: UserRepositoryImpl
    
    override fun setupMocks() {
        // דוגמה פשוטה - יוצר מוק עם משתמש אחד
        val userEntity = TestDataFactory.createUserEntity()
        val userDao = MockDataBuilder.userDaoReturning(userEntity)
        repository = UserRepositoryImpl(userDao)
    }
    
    @Test
    fun `when user exists, should return user`() = testAsync {
        logTestInfo("User Lookup", "Testing existing user retrieval")
        
        val result = repository.getUserByEmail("amit@test.com")
        assertSuccess(result)
    }
}

// דוגמה מתקדמת יותר:
class IntegrationTest : BaseRepositoryTest() {
    
    override fun setupMocks() {
        // יוצר מערכת שלמה עם נתונים
        val users = listOf(TestDataFactory.createUserEntity())
        val employees = listOf(TestDataFactory.createEmployeeEntity())
        val shifts = listOf(TestDataFactory.createShiftEntity())
        
        val (userDao, employeeDao, shiftDao) = MockDataBuilder.createWorkingSystemDaos(
            users = users,
            employees = employees,
            shifts = shifts
        )
        
        // כאן נוכל לאתחל את כל המערכת עם ה-DAOs האלה
    }
}

// דוגמה לבדיקת חוסן:
class ChaosTest : BaseRepositoryTest() {
    
    @Test
    fun `when everything fails, system should handle gracefully`() = testAsync {
        val error = RuntimeException("Complete system failure")
        val (userDao, employeeDao, shiftDao) = MockDataBuilder.createFailingSystemDaos(error)
        
        val repository = UserRepositoryImpl(userDao)
        val result = repository.registerUser(user, "password")
        
        // בודקים שהמערכת מטפלת בשגיאה ולא קורסת
        assertError<UserError.DatabaseError>(result)
    }
}
*/