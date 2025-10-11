package com.smartschedule.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smartschedule.data.local.database.AppDatabase
import com.smartschedule.domain.models.ExchangeStatus
import com.smartschedule.domain.models.ShiftExchange
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class ShiftExchangeRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: ShiftExchangeRepositoryImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = ShiftExchangeRepositoryImpl(db.shiftExchangeDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_getAllShiftExchanges() = runTest {
        val shiftExchange = ShiftExchange(
            id = 0,
            fromEmployeeId = 1L,
            toEmployeeId = null,
            shiftId = 1L,
            status = ExchangeStatus.PENDING,
            requestedAt = LocalDateTime.now(),
            resolvedAt = null,
            managerNote = null
        )

        repository.insertShiftExchange(shiftExchange)
        val result = repository.getAllShiftExchanges()

        TestCase.assertEquals(1, result.size)
        TestCase.assertEquals(1L, result.first().fromEmployeeId)
    }

    @Test
    fun update_shiftExchange() = runTest {
        val shiftExchange = ShiftExchange(
            id = 0,
            fromEmployeeId = 1L,
            toEmployeeId = null,
            shiftId = 1L,
            status = ExchangeStatus.PENDING,
            requestedAt = LocalDateTime.now(),
            resolvedAt = null,
            managerNote = null
        )

        repository.insertShiftExchange(shiftExchange)
        val inserted = repository.getAllShiftExchanges().first()
        val updated = inserted.copy(status = ExchangeStatus.APPROVED, toEmployeeId = 2L, resolvedAt = LocalDateTime.now())

        repository.updateShiftExchange(updated)
        val result = repository.getShiftExchangeById(inserted.id)

        TestCase.assertNotNull(result)
        TestCase.assertEquals(ExchangeStatus.APPROVED, result?.status)
        TestCase.assertEquals(2L, result?.toEmployeeId)
    }

    @Test
    fun delete_shiftExchange() = runTest {
        val shiftExchange = ShiftExchange(
            id = 0,
            fromEmployeeId = 1L,
            toEmployeeId = null,
            shiftId = 1L,
            status = ExchangeStatus.PENDING,
            requestedAt = LocalDateTime.now(),
            resolvedAt = null,
            managerNote = null
        )

        repository.insertShiftExchange(shiftExchange)
        val inserted = repository.getAllShiftExchanges().first()

        repository.deleteShiftExchange(inserted)
        val result = repository.getAllShiftExchanges()

        TestCase.assertEquals(0, result.size)
    }
}
