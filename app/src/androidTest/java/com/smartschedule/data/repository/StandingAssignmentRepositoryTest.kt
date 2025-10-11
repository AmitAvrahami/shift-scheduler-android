package com.smartschedule.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smartschedule.data.local.database.AppDatabase
import com.smartschedule.domain.models.StandingAssignment
import com.smartschedule.domain.models.ShiftType
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek

@RunWith(AndroidJUnit4::class)
class StandingAssignmentRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: StandingAssignmentRepositoryImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = StandingAssignmentRepositoryImpl(db.standingAssignmentDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_getAllStandingAssignments() = runTest {
        val standingAssignment = StandingAssignment(
            id = 0,
            employeeId = 1L,
            dayOfWeek = DayOfWeek.MONDAY,
            shiftType = ShiftType.MORNING,
            active = true
        )

        repository.insertStandingAssignment(standingAssignment)
        val result = repository.getAllStandingAssignments()

        TestCase.assertEquals(1, result.size)
        TestCase.assertEquals(1L, result.first().employeeId)
    }

    @Test
    fun update_standingAssignment() = runTest {
        val standingAssignment = StandingAssignment(
            id = 0,
            employeeId = 1L,
            dayOfWeek = DayOfWeek.TUESDAY,
            shiftType = ShiftType.NOON,
            active = true
        )

        repository.insertStandingAssignment(standingAssignment)
        val inserted = repository.getAllStandingAssignments().first()
        val updated = inserted.copy(active = false)

        repository.updateStandingAssignment(updated)
        val result = repository.getStandingAssignmentById(inserted.id)

        TestCase.assertNotNull(result)
        TestCase.assertEquals(false, result?.active)
    }

    @Test
    fun delete_standingAssignment() = runTest {
        val standingAssignment = StandingAssignment(
            id = 0,
            employeeId = 1L,
            dayOfWeek = DayOfWeek.WEDNESDAY,
            shiftType = ShiftType.NIGHT,
            active = true
        )

        repository.insertStandingAssignment(standingAssignment)
        val inserted = repository.getAllStandingAssignments().first()

        repository.deleteStandingAssignment(inserted)
        val result = repository.getAllStandingAssignments()

        TestCase.assertEquals(0, result.size)
    }
}
