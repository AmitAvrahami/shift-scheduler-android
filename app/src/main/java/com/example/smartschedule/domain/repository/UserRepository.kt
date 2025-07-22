package com.example.smartschedule.domain.repository

import com.example.smartschedule.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    // 📁 CRUD Operations
    fun getAllUsers(): Flow<List<User>>
    suspend fun insertUser(user: User)
    suspend fun deleteUser(user: User)
    suspend fun getUserById(id: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun isEmailExists(email: String): Boolean
    suspend fun registerUser(user: User, password: String): Boolean

    // 🔐 Authentication Operations (Combined!)
    suspend fun login(email: String, password: String): User?
    suspend fun logout()
    suspend fun getCurrentUser(): User?
    suspend fun isLoggedIn(): Boolean
}