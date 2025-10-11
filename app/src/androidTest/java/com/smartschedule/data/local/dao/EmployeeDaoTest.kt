package com.smartschedule.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smartschedule.data.local.database.AppDatabase
import com.smartschedule.data.local.entities.EmployeeEntity
import com.smartschedule.domain.models.UserRole
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runner.manipulation.Ordering

@RunWith(AndroidJUnit4::class)
class EmployeeDaoTest{

    private lateinit var db: AppDatabase
    private lateinit var dao: EmployeeDao

    @Before
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.employeeDao()
    }


    @After
    fun tearDown() {
        db.close()
    }


    @Test
    fun insert_and_query_employee() = runTest {
        val e = EmployeeEntity(
            name = "Alice",
            isStudent = false,
            maxShiftsPerWeek = 5,
            isActive = true,
            canWorkFriday = true,
            canWorkSaturday = false,
            userRole = UserRole.EMPLOYEE,
            notes = "new hire"
        )

        dao.insertEmployee(e)
        val all = dao.getAllEmployees()

        assertEquals(1, all.size)
        assertEquals("Alice", all.first().name)
    }

    @Test
    fun update_employee() = runTest {
        val e = EmployeeEntity(
            name = "Bob",
            isStudent = true,
            maxShiftsPerWeek = 3,
            isActive = true,
            canWorkFriday = false,
            canWorkSaturday = false,
            userRole = com.smartschedule.domain.models.UserRole.EMPLOYEE,
            notes = null
        )
        dao.insertEmployee(e)
        val inserted = dao.getAllEmployees().first()
        val updated = inserted.copy(maxShiftsPerWeek = 4, isActive = false)

        dao.updateEmployee(updated)

        val after = dao.getEmployeeById(inserted.id)
        assertNotNull(after)
        assertEquals(4, after?.maxShiftsPerWeek)
        assertEquals(false, after?.isActive)
    }

    @Test
    fun delete_employee() = runTest {
        val e = EmployeeEntity(
            name = "Charlie",
            isStudent = false,
            maxShiftsPerWeek = 2,
            isActive = true,
            canWorkFriday = false,
            canWorkSaturday = false,
            userRole = com.smartschedule.domain.models.UserRole.EMPLOYEE,
            notes = null
        )
        dao.insertEmployee(e)
        val saved = dao.getAllEmployees().first()

        dao.deleteEmployee(saved)

        val after = dao.getEmployeeById(saved.id)
        assertNull(after)
    }
}