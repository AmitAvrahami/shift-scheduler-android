package com.example.smartschedule.domain.repository

import com.example.smartschedule.domain.common.Result
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


    //TODO : Delete The Functions Without Result

    // CRUD Operations with Result
    fun getAllUsersWithResult(): Flow<Result<List<User>>>
    suspend fun getUserByIdWithResult(id: String): Result<User?>
    suspend fun getUserByEmailWithResult(email: String): Result<User?>
    suspend fun isEmailExistsWithResult(email: String): Result<Boolean>
    suspend fun insertUserWithResult(user: User): Result<User>
    suspend fun deleteUserWithResult(user: User): Result<Boolean>

    // Authentication Operations with Result
    suspend fun registerUserWithResult(user: User, password: String): Result<User>
    suspend fun loginWithResult(email: String, password: String): Result<User>
    suspend fun logoutWithResult(): Result<Unit>
    suspend fun getCurrentUserWithResult(): Result<User?>
    suspend fun isLoggedInWithResult(): Result<Boolean>
}