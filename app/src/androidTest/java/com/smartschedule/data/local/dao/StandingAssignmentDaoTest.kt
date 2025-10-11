package com.smartschedule.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smartschedule.data.local.database.AppDatabase
import com.smartschedule.data.local.entities.StandingAssignmentEntity
import com.smartschedule.domain.models.ShiftType
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek

@RunWith(AndroidJUnit4::class)
class StandingAssignmentDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: StandingAssignmentDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.standingAssignmentDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_query_standing_assignment() = runTest {
        val assignment = StandingAssignmentEntity(
            employeeId = 1,
            dayOfWeek = DayOfWeek.MONDAY,
            shiftType = ShiftType.MORNING,
            active = true
        )

        dao.insertStandingAssignment(assignment)
        val allAssignments = dao.getAllStandingAssignments()

        Assert.assertEquals(1, allAssignments.size)
        Assert.assertEquals(true, allAssignments.first().active)
    }

    @Test
    fun update_standing_assignment() = runTest {
        val assignment = StandingAssignmentEntity(
            employeeId = 1,
            dayOfWeek = DayOfWeek.MONDAY,
            shiftType = ShiftType.MORNING,
            active = true
        )
        dao.insertStandingAssignment(assignment)
        val inserted = dao.getAllStandingAssignments().first()
        val updated = inserted.copy(active = false)

        dao.updateStandingAssignment(updated)

        val after = dao.getStandingAssignmentById(inserted.id)
        Assert.assertNotNull(after)
        Assert.assertEquals(false, after?.active)
    }

    @Test
    fun delete_standing_assignment() = runTest {
        val assignment = StandingAssignmentEntity(
            employeeId = 1,
            dayOfWeek = DayOfWeek.MONDAY,
            shiftType = ShiftType.MORNING,
            active = true
        )
        dao.insertStandingAssignment(assignment)
        val saved = dao.getAllStandingAssignments().first()

        dao.deleteStandingAssignment(saved)

        val after = dao.getStandingAssignmentById(saved.id)
        Assert.assertNull(after)
    }
}
