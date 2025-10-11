package com.smartschedule.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smartschedule.data.local.database.AppDatabase
import com.smartschedule.domain.models.ScheduleStatus
import com.smartschedule.domain.models.WorkSchedule
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class WorkScheduleRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: WorkScheduleRepositoryImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = WorkScheduleRepositoryImpl(db.workScheduleDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_getAllWorkSchedules() = runTest {
        val sunday = LocalDate.now().with(DayOfWeek.SUNDAY)
        val workSchedule = WorkSchedule(
            id = 0,
            weekStartDate = sunday,
            status = ScheduleStatus.DRAFT,
            createdBy = "test_user",
            createdAt = LocalDateTime.now()
        )

        repository.insertWorkSchedule(workSchedule)
        val result = repository.getAllWorkSchedules()

        TestCase.assertEquals(1, result.size)
        TestCase.assertEquals("test_user", result.first().createdBy)
    }

    @Test
    fun update_workSchedule() = runTest {
        val sunday = LocalDate.now().with(DayOfWeek.SUNDAY)
        val workSchedule = WorkSchedule(
            id = 0,
            weekStartDate = sunday,
            status = ScheduleStatus.DRAFT,
            createdBy = "test_user",
            createdAt = LocalDateTime.now(),
            approvedAt = null
        )

        repository.insertWorkSchedule(workSchedule)
        val inserted = repository.getAllWorkSchedules().first()
        val updated = inserted.copy(status = ScheduleStatus.PUBLISHED, approvedAt = LocalDateTime.now())

        repository.updateWorkSchedule(updated)
        val result = repository.getWorkScheduleById(inserted.id)

        TestCase.assertNotNull(result)
        TestCase.assertEquals(ScheduleStatus.PUBLISHED, result?.status)
    }

    @Test
    fun delete_workSchedule() = runTest {
        val sunday = LocalDate.now().with(DayOfWeek.SUNDAY)
        val workSchedule = WorkSchedule(
            id = 0,
            weekStartDate = sunday,
            status = ScheduleStatus.DRAFT,
            createdBy = "test_user",
            createdAt = LocalDateTime.now()
        )

        repository.insertWorkSchedule(workSchedule)
        val inserted = repository.getAllWorkSchedules().first()

        repository.deleteWorkSchedule(inserted)
        val result = repository.getAllWorkSchedules()

        TestCase.assertEquals(0, result.size)
    }
}
