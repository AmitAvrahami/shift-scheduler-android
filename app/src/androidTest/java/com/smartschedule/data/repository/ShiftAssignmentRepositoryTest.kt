package com.smartschedule.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smartschedule.data.local.database.AppDatabase
import com.smartschedule.domain.models.ShiftAssignment
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class ShiftAssignmentRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: ShiftAssignmentRepositoryImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = ShiftAssignmentRepositoryImpl(db.shiftAssignmentDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_getAllShiftAssignments() = runTest {
        val shiftAssignment = ShiftAssignment(
            id = 0,
            shiftId = 1L,
            employeeId = 1L,
            assignedAt = LocalDateTime.now(),
            assignedBy = "test_user"
        )

        repository.insertShiftAssignment(shiftAssignment)
        val result = repository.getAllShiftAssignments()

        TestCase.assertEquals(1, result.size)
        TestCase.assertEquals(1L, result.first().employeeId)
    }

    @Test
    fun update_shiftAssignment() = runTest {
        val shiftAssignment = ShiftAssignment(
            id = 0,
            shiftId = 1L,
            employeeId = 1L,
            assignedAt = LocalDateTime.now(),
            assignedBy = "test_user"
        )

        repository.insertShiftAssignment(shiftAssignment)
        val inserted = repository.getAllShiftAssignments().first()
        val updated = inserted.copy(assignedBy = "updated_user")

        repository.updateShiftAssignment(updated)
        val result = repository.getShiftAssignmentById(inserted.id)

        TestCase.assertNotNull(result)
        TestCase.assertEquals("updated_user", result?.assignedBy)
    }

    @Test
    fun delete_shiftAssignment() = runTest {
        val shiftAssignment = ShiftAssignment(
            id = 0,
            shiftId = 1L,
            employeeId = 1L,
            assignedAt = LocalDateTime.now(),
            assignedBy = "test_user"
        )

        repository.insertShiftAssignment(shiftAssignment)
        val inserted = repository.getAllShiftAssignments().first()

        repository.deleteShiftAssignment(inserted)
        val result = repository.getAllShiftAssignments()

        TestCase.assertEquals(0, result.size)
    }
}
