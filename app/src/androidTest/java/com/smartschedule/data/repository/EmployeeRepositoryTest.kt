package com.smartschedule.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smartschedule.data.local.database.AppDatabase
import com.smartschedule.data.mappers.toEntity
import com.smartschedule.domain.models.Employee
import com.smartschedule.domain.models.UserRole
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EmployeeRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: EmployeeRepositoryImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = EmployeeRepositoryImpl(db.employeeDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_getAllEmployees() = runTest {
        val employee = Employee(
            id = 0,
            name = "Alice",
            isStudent = false,
            maxShiftsPerWeek = 5,
            isActive = true,
            canWorkFriday = true,
            canWorkSaturday = false,
            userRole = UserRole.EMPLOYEE,
            notes = null
        )

        repository.insertEmployee(employee)
        val result = repository.getAllEmployees()

        assertEquals(1, result.size)
        assertEquals("Alice", result.first().name)
    }

    @Test
    fun update_employee() = runTest {
        val employee = Employee(
            id = 0,
            name = "Bob",
            isStudent = true,
            maxShiftsPerWeek = 4,
            isActive = true,
            canWorkFriday = false,
            canWorkSaturday = false,
            userRole = UserRole.EMPLOYEE,
            notes = null
        )

        repository.insertEmployee(employee)
        val inserted = repository.getAllEmployees().first()
        val updated = inserted.copy(isActive = false, maxShiftsPerWeek = 3)

        repository.updateEmployee(updated)
        val result = repository.getEmployeeById(inserted.id!!)

        assertNotNull(result)
        assertEquals(false, result?.isActive)
        assertEquals(3, result?.maxShiftsPerWeek)
    }

    @Test
    fun delete_employee() = runTest {
        val employee = Employee(
            id = 0,
            name = "Charlie",
            isStudent = false,
            maxShiftsPerWeek = 5,
            isActive = true,
            canWorkFriday = false,
            canWorkSaturday = false,
            userRole = UserRole.EMPLOYEE,
            notes = null
        )

        repository.insertEmployee(employee)
        val inserted = repository.getAllEmployees().first()

        repository.deleteEmployee(inserted)
        val result = repository.getAllEmployees()

        assertEquals(0, result.size)
    }
}
