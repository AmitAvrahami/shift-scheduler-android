package com.example.smartschedule.data.repository

import com.example.smartschedule.data.database.dao.UserDao
import com.example.smartschedule.data.mappers.hashPassword
import com.example.smartschedule.data.mappers.toDomain
import com.example.smartschedule.data.mappers.toEntity
import com.example.smartschedule.domain.models.User
import com.example.smartschedule.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    private var currentUser: User? = null

    override fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers().map{entities ->
            entities.map {
                userEntity -> userEntity.toDomain()
            }
        }
    }

    override suspend fun insertUser(user: User) {
        userDao.insertUser(user.toEntity())
    }

    override suspend fun deleteUser(user: User) {
        userDao.deleteUser(user.toEntity())
    }

    override suspend fun getUserById(id: String): User? {
        return userDao.getUserById(id)?.toDomain()
    }

    override suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)?.toDomain()
    }

    override suspend fun isEmailExists(email: String): Boolean {
        return userDao.isEmailExists(email) > 0
    }

    override suspend fun registerUser(user: User, password: String): Boolean {
        return try {
            val passwordHash = hashPassword(password)
            val userEntity = user.toEntity().copy(passwordHash = passwordHash)
            userDao.insertUser(userEntity)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): User? {
        val passwordHash = hashPassword(password)
        val user = userDao.login(email, passwordHash)?.toDomain()

        currentUser = user
        return user
    }

    override suspend fun logout() {
        currentUser = null
    }

    override suspend fun getCurrentUser(): User? {
        return currentUser
    }

    override suspend fun isLoggedIn(): Boolean {
        return currentUser != null

    }

}