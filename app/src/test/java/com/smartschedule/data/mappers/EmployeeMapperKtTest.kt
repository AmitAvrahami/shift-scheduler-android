package com.smartschedule.data.mappers

import com.smartschedule.data.local.entities.EmployeeEntity
import com.smartschedule.domain.models.Employee
import com.smartschedule.domain.models.UserRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class EmployeeMapperKtTest {

    private val employeeEntity = EmployeeEntity(
        id = 1L,
        name = "John Doe",
        isStudent = true,
        maxShiftsPerWeek = 4,
        isActive = true,
        canWorkFriday = false,
        canWorkSaturday = true,
        userRole = UserRole.EMPLOYEE,
        notes = "Reliable employee"
    )

    private val employeeDomain = Employee(
        id = 1L,
        name = "Jane Doe",
        isStudent = false,
        maxShiftsPerWeek = 5,
        isActive = true,
        canWorkFriday = true,
        canWorkSaturday = false,
        userRole = UserRole.MANAGER,
        notes = "Team lead"
    )

    @Test
    fun `toDomain mapping validation`() {
        val domain = employeeEntity.toDomain()

        assertEquals(employeeEntity.id, domain.id)
        assertEquals(employeeEntity.name, domain.name)
        assertEquals(employeeEntity.isStudent, domain.isStudent)
        assertEquals(employeeEntity.maxShiftsPerWeek, domain.maxShiftsPerWeek)
        assertEquals(employeeEntity.isActive, domain.isActive)
        assertEquals(employeeEntity.canWorkFriday, domain.canWorkFriday)
        assertEquals(employeeEntity.canWorkSaturday, domain.canWorkSaturday)
        assertEquals(employeeEntity.userRole, domain.userRole)
        assertEquals(employeeEntity.notes, domain.notes)
    }

    @Test
    fun `toDomain with empty name`() {
        val entity = employeeEntity.copy(name = "")
        val domain = entity.toDomain()
        assert(domain.name.isBlank())
    }

    @Test
    fun `toDomain with nullable notes`() {
        val entity = employeeEntity.copy(notes = null)
        val domain = entity.toDomain()
        assertNull(domain.notes)
    }

    @Test
    fun `toDomain with special characters in strings`() {
        val specialName = "Jøhn Døe !@#$%^&*()"
        val specialNotes = "Notes with unicode characters: ö, ä, ü, ß and emojis 😃🎉"
        val entity = employeeEntity.copy(name = specialName, notes = specialNotes)

        val domain = entity.toDomain()

        assertEquals(specialName, domain.name)
        assertEquals(specialNotes, domain.notes)
    }

    @Test
    fun `toDomain with various boolean states`() {
        val entity = employeeEntity.copy(isStudent = false, isActive = false, canWorkFriday = true, canWorkSaturday = false)
        val domain = entity.toDomain()

        assertEquals(false, domain.isStudent)
        assertEquals(false, domain.isActive)
        assertEquals(true, domain.canWorkFriday)
        assertEquals(false, domain.canWorkSaturday)
    }

    @Test
    fun `toDomain with different user roles`() {
        val entity = employeeEntity.copy(userRole = UserRole.MANAGER)
        val domain = entity.toDomain()
        assertEquals(UserRole.MANAGER, domain.userRole)
    }

    @Test
    fun `toEntity mapping validation`() {
        val entity = employeeDomain.toEntity()

        assertEquals(employeeDomain.id, entity.id)
        assertEquals(employeeDomain.name, entity.name)
        assertEquals(employeeDomain.isStudent, entity.isStudent)
        assertEquals(employeeDomain.maxShiftsPerWeek, entity.maxShiftsPerWeek)
        assertEquals(employeeDomain.isActive, entity.isActive)
        assertEquals(employeeDomain.canWorkFriday, entity.canWorkFriday)
        assertEquals(employeeDomain.canWorkSaturday, entity.canWorkSaturday)
        assertEquals(employeeDomain.userRole, entity.userRole)
        assertEquals(employeeDomain.notes, entity.notes)
    }

    @Test
    fun `toEntity with null ID`() {
        val domainWithNullId = employeeDomain.copy(id = null)
        val entity = domainWithNullId.toEntity()
        assertEquals(0, entity.id)
    }

    @Test
    fun `toEntity with non null ID`() {
        val entity = employeeDomain.toEntity()
        assertNotNull(entity.id)
        assertEquals(employeeDomain.id, entity.id)
    }

    @Test
    fun `toEntity with empty name`() {
        val domain = employeeDomain.copy(name = "")
        val entity = domain.toEntity()
        assertEquals("", entity.name)
    }

    @Test
    fun `toEntity with nullable notes`() {
        val domain = employeeDomain.copy(notes = null)
        val entity = domain.toEntity()
        assertNull(entity.notes)
    }

    @Test
    fun `toEntity with special characters in strings`() {
        val specialName = "Jøhn Døe !@#$%^&*()"
        val specialNotes = "Notes with unicode characters: ö, ä, ü, ß and emojis 😃🎉"
        val domain = employeeDomain.copy(name = specialName, notes = specialNotes)

        val entity = domain.toEntity()

        assertEquals(specialName, entity.name)
        assertEquals(specialNotes, entity.notes)
    }
}
