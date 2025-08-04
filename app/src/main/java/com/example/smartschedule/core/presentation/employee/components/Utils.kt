package com.example.smartschedule.core.presentation.employee.components

import androidx.compose.ui.graphics.Color
import com.example.smartschedule.core.domain.models.UserStatus
import com.example.smartschedule.core.domain.models.UserType

data class DropdownOption(
    val value: String,
    val displayName: String
)

data class PasswordStrength(
    val progress: Float,
    val color: Color,
    val description: String,
    val requirements: List<PasswordRequirement>
)

data class PasswordRequirement(
    val text: String,
    val isMet: Boolean
)

fun calculatePasswordStrength(password: String): PasswordStrength {
    val requirements = listOf(
        PasswordRequirement("לפחות 8 תווים", password.length >= 8),
        PasswordRequirement("אות גדולה", password.any { it.isUpperCase() }),
        PasswordRequirement("אות קטנה", password.any { it.isLowerCase() }),
        PasswordRequirement("מספר", password.any { it.isDigit() }),
        PasswordRequirement("סימן מיוחד", password.any { !it.isLetterOrDigit() })
    )

    val metRequirements = requirements.count { it.isMet }
    val progress = metRequirements / requirements.size.toFloat()

    val (color, description) = when {
        progress < 0.4f -> Color(0xFFf44336) to "חלשה"
        progress < 0.6f -> Color(0xFFFF9800) to "בינונית"
        progress < 0.8f -> Color(0xFF2196F3) to "טובה"
        else -> Color(0xFF4CAF50) to "חזקה מאוד"
    }

    return PasswordStrength(progress, color, description, requirements)
}

fun getUserTypeDisplayName(userType: UserType): String {
    return when (userType) {
        UserType.ADMIN -> "מנהל מערכת"
        UserType.MANAGER -> "מנהל"
        UserType.EMPLOYEE -> "עובד"
    }
}

fun getUserStatusDisplayName(userStatus: UserStatus): String {
    return when (userStatus) {
        UserStatus.ACTIVE -> "פעיל"
        UserStatus.INACTIVE -> "לא פעיל"
        UserStatus.PENDING -> "ממתין לאישור"
    }
}