package com.example.smartschedule.data.database

import androidx.room.*
import com.example.smartschedule.data.database.converters.DateTimeConverter
import com.example.smartschedule.data.database.dao.EmployeeDao
import com.example.smartschedule.data.database.dao.ShiftDao
import com.example.smartschedule.data.database.dao.UserDao
import com.example.smartschedule.data.database.entities.EmployeeEntity
import com.example.smartschedule.data.database.entities.ShiftEntity
import com.example.smartschedule.data.database.entities.UserEntity


@Database(
    entities = [
        EmployeeEntity::class,
        ShiftEntity::class,
        UserEntity::class
               ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeConverter::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao
    abstract fun shiftDao(): ShiftDao
    abstract fun userDao(): UserDao
}