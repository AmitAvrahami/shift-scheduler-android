package com.example.smartschedule.domain.models

enum class ShiftType(
    val displayName: String,
    val defaultStartHour: Int,
    val defaultStartMinute: Int,
    val defaultEndHour: Int,
    val defaultEndMinute: Int
) {
    MORNING(
        displayName = "משמרת בוקר",
        defaultStartHour = 6,
        defaultStartMinute = 45,
        defaultEndHour = 14,
        defaultEndMinute = 45
    ),
    AFTERNOON(
        displayName = "משמרת צהריים",
        defaultStartHour = 14,
        defaultStartMinute = 45,
        defaultEndHour = 22,
        defaultEndMinute = 45
    ),
    NIGHT(
        displayName = "משמרת לילה",
        defaultStartHour = 22,
        defaultStartMinute = 45,
        defaultEndHour = 6,
        defaultEndMinute = 45
    );

    fun getTimeRange(): String {
        return "${defaultStartHour}:${defaultStartMinute.toString().padStart(2, '0')} - ${defaultEndHour}:${defaultEndMinute.toString().padStart(2, '0')}"
    }
}