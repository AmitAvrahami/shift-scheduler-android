package com.smartschedule.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smartschedule.data.local.database.AppDatabase
import com.smartschedule.domain.models.Shift
import com.smartschedule.domain.models.ShiftType
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class ShiftRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: ShiftRepositoryImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = ShiftRepositoryImpl(db.shiftDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_getAllShifts() = runTest {
        val shift = Shift(
            id = 0,
            workScheduleId = 1L,
            date = LocalDate.now(),
            shiftType = ShiftType.MORNING,
            requiredHeadcount = 2
        )

        repository.insertShift(shift)
        val result = repository.getAllShifts()

        TestCase.assertEquals(1, result.size)
        TestCase.assertEquals(1L, result.first().workScheduleId)
    }

    @Test
    fun update_shift() = runTest {
        val shift = Shift(
            id = 0,
            workScheduleId = 1L,
            date = LocalDate.now(),
            shiftType = ShiftType.MORNING,
            requiredHeadcount = 2
        )

        repository.insertShift(shift)
        val inserted = repository.getAllShifts().first()
        val updated = inserted.copy(requiredHeadcount = 3)

        repository.updateShift(updated)
        val result = repository.getShiftById(inserted.id)

        TestCase.assertNotNull(result)
        TestCase.assertEquals(3, result?.requiredHeadcount)
    }

    @Test
    fun delete_shift() = runTest {
        val shift = Shift(
            id = 0,
            workScheduleId = 1L,
            date = LocalDate.now(),
            shiftType = ShiftType.MORNING,
            requiredHeadcount = 2
        )

        repository.insertShift(shift)
        val inserted = repository.getAllShifts().first()

        repository.deleteShift(inserted)
        val result = repository.getAllShifts()

        TestCase.assertEquals(0, result.size)
    }
}
