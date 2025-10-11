package com.smartschedule.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smartschedule.data.local.database.AppDatabase
import com.smartschedule.data.local.entities.ShiftEntity
import com.smartschedule.domain.models.ShiftType
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class ShiftDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: ShiftDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.shiftDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_query_shift() = runTest {
        val shift = ShiftEntity(
            workScheduleId = 1,
            date = LocalDate.now(),
            shiftType = ShiftType.MORNING,
            requiredHeadcount = 2,
            notes = "test shift"
        )

        dao.insertShift(shift)
        val allShifts = dao.getAllShifts()

        assertEquals(1, allShifts.size)
        assertEquals("test shift", allShifts.first().notes)
    }

    @Test
    fun update_shift() = runTest {
        val shift = ShiftEntity(
            workScheduleId = 1,
            date = LocalDate.now(),
            shiftType = ShiftType.MORNING,
            requiredHeadcount = 2,
            notes = "test shift"
        )
        dao.insertShift(shift)
        val inserted = dao.getAllShifts().first()
        val updated = inserted.copy(requiredHeadcount = 3, notes = "updated shift")

        dao.updateShift(updated)

        val after = dao.getShiftById(inserted.id)
        assertNotNull(after)
        assertEquals(3, after?.requiredHeadcount)
        assertEquals("updated shift", after?.notes)
    }

    @Test
    fun delete_shift() = runTest {
        val shift = ShiftEntity(
            workScheduleId = 1,
            date = LocalDate.now(),
            shiftType = ShiftType.MORNING,
            requiredHeadcount = 2,
            notes = "test shift"
        )
        dao.insertShift(shift)
        val saved = dao.getAllShifts().first()

        dao.deleteShift(saved)

        val after = dao.getShiftById(saved.id)
        assertNull(after)
    }
}
