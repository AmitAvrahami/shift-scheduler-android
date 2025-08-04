package com.example.smartschedule.core.data.mappers


import com.example.smartschedule.core.data.database.entities.UserEntity
import com.example.smartschedule.core.domain.models.User
import com.example.smartschedule.core.domain.models.UserStatus
import com.example.smartschedule.core.domain.models.UserType
import java.security.MessageDigest

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = this.id.toString(),
        name = this.name,
        email = this.email,
        passwordHash = "", //TODO : hash password
        userType = this.type.name,
        status = this.status.name,
        createdDate = this.createdDate
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email,
        type = UserType.valueOf(this.userType),
        status = UserStatus.valueOf(this.status),
        createdDate = this.createdDate

    )
}

fun hashPassword(password: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(password.toByteArray())
    return hashBytes.joinToString("") { "%02x".format(it) }
}