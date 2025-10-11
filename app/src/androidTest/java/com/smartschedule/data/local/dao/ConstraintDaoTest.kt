package com.smartschedule.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smartschedule.data.local.database.AppDatabase
import com.smartschedule.data.local.entities.ConstraintEntity
import com.smartschedule.domain.models.ConstraintType
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class ConstraintDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: ConstraintDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.constraintDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_query_constraint() = runTest {
        val constraint = ConstraintEntity(
            employeeId = 1,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(1),
            type = ConstraintType.SINGLE_SHIFT,
            reason = "test constraint"
        )

        dao.insertConstraint(constraint)
        val allConstraints = dao.getAllConstraints()

        Assert.assertEquals(1, allConstraints.size)
        Assert.assertEquals("test constraint", allConstraints.first().reason)
    }

    @Test
    fun update_constraint() = runTest {
        val constraint = ConstraintEntity(
            employeeId = 1,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(1),
            type = ConstraintType.SINGLE_SHIFT,
            reason = "test constraint"
        )
        dao.insertConstraint(constraint)
        val inserted = dao.getAllConstraints().first()
        val updated = inserted.copy(reason = "updated constraint")

        dao.updateConstraint(updated)

        val after = dao.getConstraintById(inserted.id)
        Assert.assertNotNull(after)
        Assert.assertEquals("updated constraint", after?.reason)
    }

    @Test
    fun delete_constraint() = runTest {
        val constraint = ConstraintEntity(
            employeeId = 1,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(1),
            type = ConstraintType.SINGLE_SHIFT,
            reason = "test constraint"
        )
        dao.insertConstraint(constraint)
        val saved = dao.getAllConstraints().first()

        dao.deleteConstraint(saved)

        val after = dao.getConstraintById(saved.id)
        Assert.assertNull(after)
    }
}
