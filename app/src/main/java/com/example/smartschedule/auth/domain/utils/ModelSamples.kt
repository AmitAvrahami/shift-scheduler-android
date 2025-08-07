package com.example.smartschedule.auth.domain.utils

import com.example.smartschedule.auth.domain.models.user.InfoDetail
import com.example.smartschedule.auth.domain.models.user.User
import com.example.smartschedule.auth.domain.models.user.UserRole

object ModelSamples {
    val user1 = User(
        id = "user123",
        username = "john.doe",
        password = "password123",
        email = "john.doe@example.com",
        infoDetails = InfoDetail(
            firstName = "John",
            lastName = "Doe",
            profilePicture = "/path/to/profile/pic1.jpg",
            phoneNumber = "123-456-7890"
        ),
        userRole = UserRole.ADMIN,
        isActive = true
    )
    val user2 = User(
        id = "user456",
        username = "jane.smith",
        password = "password456",
        email = "jane.smith@example.com",
        infoDetails = InfoDetail(
            firstName = "Jane",
            lastName = "Smith",
            profilePicture = null,
            phoneNumber = "098-765-4321"
        ),
        userRole = UserRole.MANAGER,
        isActive = true
    )
    val user3 = User(
        id = "user789",
        username = "alice.jones",
        password = "password789",
        email = "alice.jones@example.com",
        infoDetails = InfoDetail(
            firstName = "Alice",
            lastName = "Jones",
            profilePicture = "/path/to/profile/pic2.png",
            phoneNumber = "555-555-5555"
        ),
        userRole = UserRole.EMPLOYEE,
        isActive = false
    )

    fun accessLogin(username: String, password: String): Boolean {
        return when {
            username == user1.username && password == user1.password -> true
            username == user2.username && password == user2.password -> true
            username == user3.username && password == user3.password -> true
            else -> false
        }
    }
}

