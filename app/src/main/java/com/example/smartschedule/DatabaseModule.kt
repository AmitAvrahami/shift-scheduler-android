package com.example.smartschedule

import android.content.Context
import androidx.room.Room
import com.example.smartschedule.data.database.AppDataBase


object DatabaseModule {

    @Volatile
    private var INSTANCE : AppDataBase? = null

    fun getDatabase(context: Context): AppDataBase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        "smart_schedule_database"
                    ).fallbackToDestructiveMigration(true)
                .build()
            INSTANCE = instance
            instance

        }
    }
}