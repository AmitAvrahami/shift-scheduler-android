package com.smartschedule.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.smartschedule.data.local.dao.*
import com.smartschedule.data.local.entities.*

@Database(
    entities = [
        ShiftEntity::class,
        EmployeeEntity::class,
        ConstraintEntity::class,
        WorkScheduleEntity::class,
        ShiftExchangeEntity::class,
        ShiftTemplateEntity::class,
        ShiftAssignmentEntity::class,
        StandingAssignmentEntity::class,
        RecurringConstraintEntity::class
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shiftDao(): ShiftDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun constraintDao(): ConstraintDao
    abstract fun workScheduleDao(): WorkScheduleDao
    abstract fun shiftExchangeDao(): ShiftExchangeDao
    abstract fun shiftTemplateDao(): ShiftTemplateDao
    abstract fun shiftAssignmentDao(): ShiftAssignmentDao
    abstract fun standingAssignmentDao(): StandingAssignmentDao
    abstract fun recurringConstraintDao(): RecurringConstraintDao
}