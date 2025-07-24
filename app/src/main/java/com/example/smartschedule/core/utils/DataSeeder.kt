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
            val existingUsers = userRepository.getAllUsers()

            if (isEmpty(existingUsers)) {
                createDummyUsers()
            }
        } catch (e: Exception) {
            println("🚨 Error seeding data: ${e.message}")
        }
    }

    private suspend fun createDummyUsers() {
        val adminUser = User(
            id = "1",
            name = "עמית אברהמי",
            email = "amit@admin.com",
            type = UserType.ADMIN
        )

        userRepository.registerUser(adminUser, "123456")
        println("✅ נוצר ADMIN: ${adminUser.name}")
    }

    private suspend fun isEmpty(users: kotlinx.coroutines.flow.Flow<List<User>>): Boolean {
        return try {
            users.first().isEmpty()
        } catch (e: Exception) {
            true
        }
    }
}