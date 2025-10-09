package com.smartschedule.domain.models

enum class UserRole{
    MANAGER,
    EMPLOYEE,
}

enum class ShiftType{
    MORNING, NOON, NIGHT
}

enum class ConstraintType{
    SINGLE_SHIFT , DAY_OFF , DATE_RANGE
}


enum class ScheduleStatus{
    DRAFT, PENDING_APPROVAL, PUBLISHED
}

enum class ExchangeStatus{
    PENDING, APPROVED, REJECTED, CANCELLED
}