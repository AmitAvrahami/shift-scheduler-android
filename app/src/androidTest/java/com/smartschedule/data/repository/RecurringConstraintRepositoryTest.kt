package com.smartschedule.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smartschedule.data.local.database.AppDatabase
import com.smartschedule.domain.models.RecurringConstraint
import com.smartschedule.domain.models.ShiftType
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek

@RunWith(AndroidJUnit4::class)
class RecurringConstraintRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: RecurringConstraintRepositoryImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = RecurringConstraintRepositoryImpl(db.recurringConstraintDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_getAllRecurringConstraints() = runTest {
        val recurringConstraint = RecurringConstraint(
            id = 0,
            employeeId = 1L,
            dayOfWeek = DayOfWeek.MONDAY,
            shiftTypes = setOf(ShiftType.MORNING)
        )

        repository.insertRecurringConstraint(recurringConstraint)
        val result = repository.getAllRecurringConstraints()

        TestCase.assertEquals(1, result.size)
        TestCase.assertEquals(1L, result.first().employeeId)
    }

    @Test
    fun update_recurringConstraint() = runTest {
        val recurringConstraint = RecurringConstraint(
            id = 0,
            employeeId = 1L,
            dayOfWeek = DayOfWeek.TUESDAY,
            shiftTypes = setOf(ShiftType.NOON)
        )

        repository.insertRecurringConstraint(recurringConstraint)
        val inserted = repository.getAllRecurringConstraints().first()
        val updated = inserted.copy(shiftTypes = setOf(ShiftType.NIGHT))

        repository.updateRecurringConstraint(updated)
        val result = repository.getRecurringConstraintById(inserted.id)

        TestCase.assertNotNull(result)
        TestCase.assertEquals(setOf(ShiftType.NIGHT), result?.shiftTypes)
    }

    @Test
    fun delete_recurringConstraint() = runTest {
        val recurringConstraint = RecurringConstraint(
            id = 0,
            employeeId = 1L,
            dayOfWeek = DayOfWeek.WEDNESDAY,
            shiftTypes = setOf(ShiftType.MORNING, ShiftType.NOON)
        )

        repository.insertRecurringConstraint(recurringConstraint)
        val inserted = repository.getAllRecurringConstraints().first()

        repository.deleteRecurringConstraint(inserted)
        val result = repository.getAllRecurringConstraints()

        TestCase.assertEquals(0, result.size)
    }
}
