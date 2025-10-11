package com.smartschedule.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smartschedule.data.local.database.AppDatabase
import com.smartschedule.data.local.entities.ShiftAssignmentEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class ShiftAssignmentDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: ShiftAssignmentDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.shiftAssignmentDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_query_shift_assignment() = runTest {
        val assignment = ShiftAssignmentEntity(
            shiftId = 1,
            employeeId = 1,
            assignedAt = LocalDateTime.now(),
            assignedBy = "test user"
        )

        dao.insertShiftAssignment(assignment)
        val allAssignments = dao.getAllShiftAssignments()

        Assert.assertEquals(1, allAssignments.size)
        Assert.assertEquals("test user", allAssignments.first().assignedBy)
    }

    @Test
    fun update_shift_assignment() = runTest {
        val assignment = ShiftAssignmentEntity(
            shiftId = 1,
            employeeId = 1,
            assignedAt = LocalDateTime.now(),
            assignedBy = "test user"
        )
        dao.insertShiftAssignment(assignment)
        val inserted = dao.getAllShiftAssignments().first()
        val updated = inserted.copy(assignedBy = "updated user")

        dao.updateShiftAssignment(updated)

        val after = dao.getShiftAssignmentById(inserted.id)
        Assert.assertNotNull(after)
        Assert.assertEquals("updated user", after?.assignedBy)
    }

    @Test
    fun delete_shift_assignment() = runTest {
        val assignment = ShiftAssignmentEntity(
            shiftId = 1,
            employeeId = 1,
            assignedAt = LocalDateTime.now(),
            assignedBy = "test user"
        )
        dao.insertShiftAssignment(assignment)
        val saved = dao.getAllShiftAssignments().first()

        dao.deleteShiftAssignment(saved)

        val after = dao.getShiftAssignmentById(saved.id)
        Assert.assertNull(after)
    }
}
