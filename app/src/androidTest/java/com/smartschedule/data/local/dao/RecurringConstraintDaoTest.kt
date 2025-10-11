package com.smartschedule.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smartschedule.data.local.database.AppDatabase
import com.smartschedule.data.local.entities.RecurringConstraintEntity
import com.smartschedule.domain.models.ShiftType
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek

@RunWith(AndroidJUnit4::class)
class RecurringConstraintDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: RecurringConstraintDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.recurringConstraintDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_query_recurring_constraint() = runTest {
        val constraint = RecurringConstraintEntity(
            employeeId = 1,
            dayOfWeek = DayOfWeek.MONDAY,
            shiftTypes = setOf(ShiftType.MORNING),
            reason = "test constraint"
        )

        dao.insertRecurringConstraint(constraint)
        val allConstraints = dao.getAllRecurringConstraints()

        Assert.assertEquals(1, allConstraints.size)
        Assert.assertEquals("test constraint", allConstraints.first().reason)
    }

    @Test
    fun update_recurring_constraint() = runTest {
        val constraint = RecurringConstraintEntity(
            employeeId = 1,
            dayOfWeek = DayOfWeek.MONDAY,
            shiftTypes = setOf(ShiftType.MORNING),
            reason = "test constraint"
        )
        dao.insertRecurringConstraint(constraint)
        val inserted = dao.getAllRecurringConstraints().first()
        val updated = inserted.copy(reason = "updated constraint")

        dao.updateRecurringConstraint(updated)

        val after = dao.getRecurringConstraintById(inserted.id)
        Assert.assertNotNull(after)
        Assert.assertEquals("updated constraint", after?.reason)
    }

    @Test
    fun delete_recurring_constraint() = runTest {
        val constraint = RecurringConstraintEntity(
            employeeId = 1,
            dayOfWeek = DayOfWeek.MONDAY,
            shiftTypes = setOf(ShiftType.MORNING),
            reason = "test constraint"
        )
        dao.insertRecurringConstraint(constraint)
        val saved = dao.getAllRecurringConstraints().first()

        dao.deleteRecurringConstraint(saved)

        val after = dao.getRecurringConstraintById(saved.id)
        Assert.assertNull(after)
    }
}
