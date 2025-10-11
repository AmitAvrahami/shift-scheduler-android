package com.smartschedule.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smartschedule.data.local.database.AppDatabase
import com.smartschedule.data.local.entities.ShiftExchangeEntity
import com.smartschedule.domain.models.ExchangeStatus
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class ShiftExchangeDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: ShiftExchangeDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.shiftExchangeDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_query_shift_exchange() = runTest {
        val exchange = ShiftExchangeEntity(
            fromEmployeeId = 1,
            toEmployeeId = 2,
            shiftId = 1,
            status = ExchangeStatus.PENDING,
            requestedAt = LocalDateTime.now()
        )

        dao.insertShiftExchange(exchange)
        val allExchanges = dao.getAllShiftExchanges()

        Assert.assertEquals(1, allExchanges.size)
        Assert.assertEquals(ExchangeStatus.PENDING, allExchanges.first().status)
    }

    @Test
    fun update_shift_exchange() = runTest {
        val exchange = ShiftExchangeEntity(
            fromEmployeeId = 1,
            toEmployeeId = 2,
            shiftId = 1,
            status = ExchangeStatus.PENDING,
            requestedAt = LocalDateTime.now()
        )
        dao.insertShiftExchange(exchange)
        val inserted = dao.getAllShiftExchanges().first()
        val updated = inserted.copy(status = ExchangeStatus.APPROVED)

        dao.updateShiftExchange(updated)

        val after = dao.getShiftExchangeById(inserted.id)
        Assert.assertNotNull(after)
        Assert.assertEquals(ExchangeStatus.APPROVED, after?.status)
    }

    @Test
    fun delete_shift_exchange() = runTest {
        val exchange = ShiftExchangeEntity(
            fromEmployeeId = 1,
            toEmployeeId = 2,
            shiftId = 1,
            status = ExchangeStatus.PENDING,
            requestedAt = LocalDateTime.now()
        )
        dao.insertShiftExchange(exchange)
        val saved = dao.getAllShiftExchanges().first()

        dao.deleteShiftExchange(saved)

        val after = dao.getShiftExchangeById(saved.id)
        Assert.assertNull(after)
    }
}
