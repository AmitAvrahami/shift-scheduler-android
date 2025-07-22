package com.example.smartschedule

import android.app.Application
import com.example.smartschedule.core.utils.DataSeeder
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class HiltApplication : Application() {

    @Inject
    lateinit var dataSeeder: DataSeeder

    override fun onCreate() {
        super.onCreate()

        // יצירת dummy data ברקע
        CoroutineScope(Dispatchers.IO).launch {
            dataSeeder.seedInitialData()
        }
    }
}