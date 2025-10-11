package com.smartschedule.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smartschedule.data.local.database.AppDatabase
import com.smartschedule.data.local.entities.ShiftTemplateEntity
import com.smartschedule.domain.models.ShiftType
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek

@RunWith(AndroidJUnit4::class)
class ShiftTemplateDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: ShiftTemplateDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.shiftTemplateDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_query_shift_template() = runTest {
        val template = ShiftTemplateEntity(
            dayOfWeek = DayOfWeek.MONDAY,
            shiftType = ShiftType.MORNING,
            requiredHeadcount = 2
        )

        dao.insertShiftTemplate(template)
        val allTemplates = dao.getAllShiftTemplates()

        Assert.assertEquals(1, allTemplates.size)
        Assert.assertEquals(ShiftType.MORNING, allTemplates.first().shiftType)
    }

    @Test
    fun update_shift_template() = runTest {
        val template = ShiftTemplateEntity(
            dayOfWeek = DayOfWeek.MONDAY,
            shiftType = ShiftType.MORNING,
            requiredHeadcount = 2
        )
        dao.insertShiftTemplate(template)
        val inserted = dao.getAllShiftTemplates().first()
        val updated = inserted.copy(requiredHeadcount = 3)

        dao.updateShiftTemplate(updated)

        val after = dao.getShiftTemplateById(inserted.id)
        Assert.assertNotNull(after)
        Assert.assertEquals(3, after?.requiredHeadcount)
    }

    @Test
    fun delete_shift_template() = runTest {
        val template = ShiftTemplateEntity(
            dayOfWeek = DayOfWeek.MONDAY,
            shiftType = ShiftType.MORNING,
            requiredHeadcount = 2
        )
        dao.insertShiftTemplate(template)
        val saved = dao.getAllShiftTemplates().first()

        dao.deleteShiftTemplate(saved)

        val after = dao.getShiftTemplateById(saved.id)
        Assert.assertNull(after)
    }
}
