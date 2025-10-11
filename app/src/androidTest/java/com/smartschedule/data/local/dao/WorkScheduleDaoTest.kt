package com.smartschedule.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smartschedule.data.local.database.AppDatabase
import com.smartschedule.data.local.entities.WorkScheduleEntity
import com.smartschedule.domain.models.ScheduleStatus
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class WorkScheduleDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: WorkScheduleDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.workScheduleDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_query_work_schedule() = runTest {
        val schedule = WorkScheduleEntity(
            weekStartDate = LocalDate.now(),
            status = ScheduleStatus.DRAFT,
            createdBy = "test user",
            createdAt = LocalDateTime.now()
        )

        dao.insertWorkSchedule(schedule)
        val allSchedules = dao.getAllWorkSchedules()

        Assert.assertEquals(1, allSchedules.size)
        Assert.assertEquals("test user", allSchedules.first().createdBy)
    }

    @Test
    fun update_work_schedule() = runTest {
        val schedule = WorkScheduleEntity(
            weekStartDate = LocalDate.now(),
            status = ScheduleStatus.DRAFT,
            createdBy = "test user",
            createdAt = LocalDateTime.now()
        )
        dao.insertWorkSchedule(schedule)
        val inserted = dao.getAllWorkSchedules().first()
        val updated = inserted.copy(status = ScheduleStatus.PUBLISHED)

        dao.updateWorkSchedule(updated)

        val after = dao.getWorkScheduleById(inserted.id)
        Assert.assertNotNull(after)
        Assert.assertEquals(ScheduleStatus.PUBLISHED, after?.status)
    }

    @Test
    fun delete_work_schedule() = runTest {
        val schedule = WorkScheduleEntity(
            weekStartDate = LocalDate.now(),
            status = ScheduleStatus.DRAFT,
            createdBy = "test user",
            createdAt = LocalDateTime.now()
        )
        dao.insertWorkSchedule(schedule)
        val saved = dao.getAllWorkSchedules().first()

        dao.deleteWorkSchedule(saved)

        val after = dao.getWorkScheduleById(saved.id)
        Assert.assertNull(after)
    }
}
