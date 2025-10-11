package com.smartschedule.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smartschedule.data.local.database.AppDatabase
import com.smartschedule.domain.models.Constraint
import com.smartschedule.domain.models.ConstraintType
import com.smartschedule.domain.models.ShiftType
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class ConstraintRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: ConstraintRepositoryImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = ConstraintRepositoryImpl(db.constraintDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_getAllConstraints() = runTest {
        val constraint = Constraint(
            employeeId = 1L,
            dateStart = LocalDate.now(),
            dateEnd = LocalDate.now(),
            constraintType = ConstraintType.SINGLE_SHIFT,
            shiftType = ShiftType.MORNING
        )

        repository.insertConstraint(constraint)
        val result = repository.getAllConstraints()

        assertEquals(1, result.size)
        assertEquals(1L, result.first().employeeId)
    }

    @Test
    fun update_constraint() = runTest {
        val constraint = Constraint(
            employeeId = 1L,
            dateStart = LocalDate.now(),
            dateEnd = LocalDate.now(),
            constraintType = ConstraintType.DAY_OFF
        )

        repository.insertConstraint(constraint)
        val inserted = repository.getAllConstraints().first()
        val updated = inserted.copy(reason = "Updated reason")

        repository.updateConstraint(updated)
        val result = repository.getConstraintById(inserted.id)

        assertNotNull(result)
        assertEquals("Updated reason", result?.reason)
    }

    @Test
    fun delete_constraint() = runTest {
        val constraint = Constraint(
            employeeId = 1L,
            dateStart = LocalDate.now(),
            dateEnd = LocalDate.now(),
            constraintType = ConstraintType.DAY_OFF
        )

        repository.insertConstraint(constraint)
        val inserted = repository.getAllConstraints().first()

        repository.deleteConstraint(inserted)
        val result = repository.getAllConstraints()

        assertEquals(0, result.size)
    }
}
