package com.smartschedule.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smartschedule.data.local.database.AppDatabase
import com.smartschedule.domain.models.ShiftTemplate
import com.smartschedule.domain.models.ShiftType
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek

@RunWith(AndroidJUnit4::class)
class ShiftTemplateRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: ShiftTemplateRepositoryImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = ShiftTemplateRepositoryImpl(db.shiftTemplateDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_getAllShiftTemplates() = runTest {
        val shiftTemplate = ShiftTemplate(
            id = 0,
            dayOfWeek = DayOfWeek.MONDAY,
            shiftType = ShiftType.MORNING,
            requiredHeadcount = 2
        )

        repository.insertShiftTemplate(shiftTemplate)
        val result = repository.getAllShiftTemplates()

        TestCase.assertEquals(1, result.size)
        TestCase.assertEquals(DayOfWeek.MONDAY, result.first().dayOfWeek)
    }

    @Test
    fun update_shiftTemplate() = runTest {
        val shiftTemplate = ShiftTemplate(
            id = 0,
            dayOfWeek = DayOfWeek.TUESDAY,
            shiftType = ShiftType.NOON,
            requiredHeadcount = 3
        )

        repository.insertShiftTemplate(shiftTemplate)
        val inserted = repository.getAllShiftTemplates().first()
        val updated = inserted.copy(requiredHeadcount = 4)

        repository.updateShiftTemplate(updated)
        val result = repository.getShiftTemplateById(inserted.id)

        TestCase.assertNotNull(result)
        TestCase.assertEquals(4, result?.requiredHeadcount)
    }

    @Test
    fun delete_shiftTemplate() = runTest {
        val shiftTemplate = ShiftTemplate(
            id = 0,
            dayOfWeek = DayOfWeek.WEDNESDAY,
            shiftType = ShiftType.NIGHT,
            requiredHeadcount = 1
        )

        repository.insertShiftTemplate(shiftTemplate)
        val inserted = repository.getAllShiftTemplates().first()

        repository.deleteShiftTemplate(inserted)
        val result = repository.getAllShiftTemplates()

        TestCase.assertEquals(0, result.size)
    }
}
