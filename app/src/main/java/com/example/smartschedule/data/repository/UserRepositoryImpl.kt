package com.example.smartschedule.data.repository

import com.example.smartschedule.data.database.dao.UserDao
import com.example.smartschedule.data.mappers.hashPassword
import com.example.smartschedule.data.mappers.toDomain
import com.example.smartschedule.data.mappers.toEntity
import com.example.smartschedule.domain.models.User
import com.example.smartschedule.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import com.example.smartschedule.domain.common.Result
import com.example.smartschedule.domain.common.safeUserDbOperation
import com.example.smartschedule.domain.errors.user_error.UserError

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    private var currentUser: User? = null

    override fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers().map { entities ->
            entities.map { userEntity ->
                userEntity.toDomain()
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

    override suspend fun registerUserWithResult(user: User, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            safeUserDbOperation {
                // Check if email already exists
                val emailExists = userDao.isEmailExists(user.email) > 0
                if (emailExists) {
                    throw UserError.DuplicateEmail(user.email)
                }

                // Hash password
                val passwordHash = try {
                    hashPassword(password)
                } catch (e: Exception) {
                    throw UserError.PasswordHashFailure(e)
                }

                // Create user entity with hashed password
                val userEntity = user.toEntity().copy(passwordHash = passwordHash)

                // Insert to database
                userDao.insertUser(userEntity)

                // Return the user (with original data, DB handles ID generation)
                user
            }
        }

    override suspend fun loginWithResult(email: String, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            safeUserDbOperation {
                // Hash the provided password
                val passwordHash = try {
                    hashPassword(password)
                } catch (e: Exception) {
                    throw UserError.PasswordHashFailure(e)
                }

                // Attempt login
                val userEntity = userDao.login(email, passwordHash)
                    ?: throw UserError.InvalidCredentials(email)

                // Convert to domain model and set as current user
                val user = userEntity.toDomain()
                currentUser = user

                user
            }
        }

    override suspend fun getCurrentUserWithResult(): Result<User?> = withContext(Dispatchers.IO) {
        safeUserDbOperation {
            currentUser // This is already stored in memory
        }
    }

    override suspend fun logoutWithResult(): Result<Unit> = withContext(Dispatchers.IO) {
        safeUserDbOperation {
            currentUser = null
        }
    }

    override suspend fun isLoggedInWithResult(): Result<Boolean> = withContext(Dispatchers.IO) {
        safeUserDbOperation {
            currentUser != null
        }
    }

    override suspend fun getUserByIdWithResult(id: String): Result<User?> =
        withContext(Dispatchers.IO) {
            safeUserDbOperation {
                userDao.getUserById(id)?.toDomain()
            }
        }

    override suspend fun getUserByEmailWithResult(email: String): Result<User?> =
        withContext(Dispatchers.IO) {
            safeUserDbOperation {
                userDao.getUserByEmail(email)?.toDomain()
            }
        }

    override suspend fun isEmailExistsWithResult(email: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            safeUserDbOperation {
                userDao.isEmailExists(email) > 0
            }
        }

    override suspend fun insertUserWithResult(user: User): Result<User> =
        withContext(Dispatchers.IO) {
            safeUserDbOperation {
                userDao.insertUser(user.toEntity())
                user
            }
        }

    override suspend fun deleteUserWithResult(user: User): Result<Boolean> =
        withContext(Dispatchers.IO) {
            safeUserDbOperation {
                userDao.deleteUser(user.toEntity())
                true
            }
        }
}
