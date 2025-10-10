package com.smartschedule.data.local.database

import androidx.room.TypeConverter
import com.smartschedule.domain.models.*
import java.time.*
import java.time.format.DateTimeFormatter

class Converters {

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // 🗓️ LocalDate
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.format(dateFormatter)

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? =
        value?.let { LocalDate.parse(it, dateFormatter) }

    // ⏰ LocalDateTime
    @TypeConverter
    fun fromLocalDateTime(dt: LocalDateTime?): String? = dt?.format(dateTimeFormatter)

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? =
        value?.let { LocalDateTime.parse(it, dateTimeFormatter) }

    // 🕐 LocalTime
    @TypeConverter
    fun fromLocalTime(time: LocalTime?): String? = time?.toString()

    @TypeConverter
    fun toLocalTime(value: String?): LocalTime? =
        value?.let { LocalTime.parse(it) }

    // 📅 DayOfWeek
    @TypeConverter
    fun fromDayOfWeek(day: DayOfWeek?): String? = day?.name

    @TypeConverter
    fun toDayOfWeek(value: String?): DayOfWeek? =
        value?.let { DayOfWeek.valueOf(it) }

    // 🧩 ScheduleStatus
    @TypeConverter
    fun fromScheduleStatus(status: ScheduleStatus?): String? = status?.name

    @TypeConverter
    fun toScheduleStatus(value: String?): ScheduleStatus? =
        value?.let { ScheduleStatus.valueOf(it) }

    // 🧩 ShiftType
    @TypeConverter
    fun fromShiftType(type: ShiftType?): String? = type?.name

    @TypeConverter
    fun toShiftType(value: String?): ShiftType? =
        value?.let { ShiftType.valueOf(it) }

    // 🧩 ConstraintType
    @TypeConverter
    fun fromConstraintType(type: ConstraintType?): String? = type?.name

    @TypeConverter
    fun toConstraintType(value: String?): ConstraintType? =
        value?.let { ConstraintType.valueOf(it) }

    // 🧩 ExchangeStatus
    @TypeConverter
    fun fromExchangeStatus(status: ExchangeStatus?): String? = status?.name

    @TypeConverter
    fun toExchangeStatus(value: String?): ExchangeStatus? =
        value?.let { ExchangeStatus.valueOf(it) }

    // 🧩 UserRole
    @TypeConverter
    fun fromUserRole(role: UserRole?): String? = role?.name

    @TypeConverter
    fun toUserRole(value: String?): UserRole? =
        value?.let { UserRole.valueOf(it) }

    // 🧩 Set<ShiftType>
    @TypeConverter
    fun fromShiftTypeSet(set: Set<ShiftType>?): String? =
        set?.joinToString(",") { it.name }

    @TypeConverter
    fun toShiftTypeSet(value: String?): Set<ShiftType> =
        value
            ?.split(",")
            ?.mapNotNull { runCatching { ShiftType.valueOf(it) }.getOrNull() }
            ?.toSet()
            ?: emptySet()
}
