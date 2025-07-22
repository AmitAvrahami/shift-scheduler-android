package com.example.smartschedule.data.mappers


import com.example.smartschedule.data.database.entities.UserEntity
import com.example.smartschedule.domain.models.User
import com.example.smartschedule.domain.models.UserType
import java.security.MessageDigest

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = this.id.toString(),
        name = this.name,
        email = this.email,
        passwordHash = "", //TODO : hash password
        userType = this.type.name
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email,
        type = UserType.valueOf(this.userType)
    )
}

fun hashPassword(password: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(password.toByteArray())
    return hashBytes.joinToString("") { "%02x".format(it) }
}