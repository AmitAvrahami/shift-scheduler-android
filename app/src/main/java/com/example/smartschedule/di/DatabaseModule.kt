package com.example.smartschedule.di

import android.content.Context
import androidx.room.Room
import com.example.smartschedule.data.database.AppDataBase
import com.example.smartschedule.data.database.dao.EmployeeDao
import com.example.smartschedule.data.database.dao.ShiftDao
import com.example.smartschedule.data.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDataBase {
        return Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "smart_schedule_database"
            ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideEmployeeDao(database: AppDataBase): EmployeeDao {
        return database.employeeDao()
    }

    @Provides
    fun provideShiftDao(database: AppDataBase): ShiftDao {
        return database.shiftDao()
    }

    @Provides
    fun provideUserDao(database: AppDataBase): UserDao {
        return database.userDao()
    }
}