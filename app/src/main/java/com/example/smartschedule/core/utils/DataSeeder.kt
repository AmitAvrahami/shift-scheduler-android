package com.example.smartschedule.core.utils

import com.example.smartschedule.domain.models.User
import com.example.smartschedule.domain.models.UserType
import com.example.smartschedule.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSeeder @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend fun seedInitialData() {
        try {
            // בדוק אם כבר יש users
            val existingUsers = userRepository.getAllUsers()

            // אם אין users - צור dummy users
            if (isEmpty(existingUsers)) {
                createDummyUsers()
            }
        } catch (e: Exception) {
            // Log error but don't crash
            println("🚨 Error seeding data: ${e.message}")
        }
    }

    private suspend fun createDummyUsers() {
        val dummyUsers = listOf(
            // עמית - מנהל (זה אתה!)
            User(
                id = "1",
                name = "עמית אברהמי",
                email = "amit@example.com",
                type = UserType.MANAGER
            ),

            // עובד רגיל
            User(
                id = 2.toString(),
                name = "שרה כהן",
                email = "sara@example.com",
                type = UserType.EMPLOYEE
            ),

            // עוד עובד
            User(
                id = 3.toString(),
                name = "דני לוי",
                email = "danny@example.com",
                type = UserType.EMPLOYEE
            ),

            // אדמין
            User(
                id = 4.toString(),
                name = "אדמין מערכת",
                email = "admin@example.com",
                type = UserType.ADMIN
            )
        )

        // רשום את כל המשתמשים עם הסיסמה "123456"
        dummyUsers.forEach { user ->
            userRepository.registerUser(user, "123456")
            println("✅ נוצר משתמש: ${user.name} (${user.email})")
        }
    }

    // Helper function לבדיקה אם יש users
    private suspend fun isEmpty(users: kotlinx.coroutines.flow.Flow<List<User>>): Boolean {
        return try {
            users.first().isEmpty()
        } catch (e: Exception) {
            true // אם יש שגיאה, נניח שאין users
        }
    }
}