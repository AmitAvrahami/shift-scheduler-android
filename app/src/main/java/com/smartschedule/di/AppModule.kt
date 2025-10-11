package com.smartschedule.di


import android.content.Context
import androidx.room.Room
import com.smartschedule.data.local.database.AppDatabase
import com.smartschedule.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "smart_schedule.db"
        ).build()

    // 🧩 DAOs
    @Provides fun provideEmployeeDao(db: AppDatabase): EmployeeDao = db.employeeDao()
    @Provides fun provideShiftDao(db: AppDatabase): ShiftDao = db.shiftDao()
    @Provides fun provideConstraintDao(db: AppDatabase): ConstraintDao = db.constraintDao()
    @Provides fun provideWorkScheduleDao(db: AppDatabase): WorkScheduleDao = db.workScheduleDao()
    @Provides fun provideShiftExchangeDao(db: AppDatabase): ShiftExchangeDao = db.shiftExchangeDao()
    @Provides fun provideShiftTemplateDao(db: AppDatabase): ShiftTemplateDao = db.shiftTemplateDao()
    @Provides fun provideShiftAssignmentDao(db: AppDatabase): ShiftAssignmentDao = db.shiftAssignmentDao()
    @Provides fun provideStandingAssignmentDao(db: AppDatabase): StandingAssignmentDao = db.standingAssignmentDao()
    @Provides fun provideRecurringConstraintDao(db: AppDatabase): RecurringConstraintDao = db.recurringConstraintDao()
}
