package com.example.smartschedule.domain.models

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import org.junit.Test
import com.example.smartschedule.core.domain.common.*
import com.example.smartschedule.core.domain.models.UserType

class UserTypeTest {

    //canCreateManagers
    @Test
    fun `ADMIN should be able to create managers`() {
        assertThat(UserType.ADMIN.canCreateManagers()).isTrue()
    }

    @Test
    fun `MANAGER should not be able to create managers`(){
        assertThat(UserType.MANAGER.canCreateManagers()).isFalse()
    }

    @Test
    fun `EMPLOYEE should not be able to create managers`() {
        assertThat(UserType.EMPLOYEE.canCreateManagers()).isFalse()
    }

    //canManageSchedules

    @Test
    fun `ADMIN should be able to manage schedules`() {
        assertThat(UserType.ADMIN.canManageSchedules()).isTrue()
    }

    @Test
    fun `MANAGER should be able to manage schedules`() {
        assertThat(UserType.MANAGER.canManageSchedules()).isTrue()
    }

    @Test
    fun `EMPLOYEE should not be able to manage schedules`() {
        assertThat(UserType.EMPLOYEE.canManageSchedules()).isFalse()
    }


    //canViewSchedules

    @Test
    fun `all user types should be able to view schedules`() {
        UserType.entries.forEach { userType ->
         assertWithMessage("UserType $userType should view schedules")
             .that(userType.canViewSchedules()).isTrue()
        }
    }

    @Test
    fun `only ADMIN should be able to manage system rules`() {

        assertThat(UserType.ADMIN.canManageSystemRules()).isTrue()
        assertThat(UserType.MANAGER.canManageSystemRules()).isFalse()
        assertThat(UserType.EMPLOYEE.canManageSystemRules()).isFalse()
    }
    // רק מנהל ואדמין יכולים לנהל עובדים
    @Test
    fun `MANAGER and ADMIN should be able to manage employees`() {
        assertThat(UserType.ADMIN.canManageEmployees()).isTrue()
        assertThat(UserType.MANAGER.canManageEmployees()).isTrue()
        assertThat(UserType.EMPLOYEE.canManageEmployees()).isFalse()
    }
    // עובד רגיל לא אמור להיות לו הרשאות ניהול
    @Test
    fun `EMPLOYEE should not have any management permissions`() {
        val employee = UserType.EMPLOYEE

        assertThat(employee.canCreateManagers()).isFalse()
        assertThat(employee.canManageSchedules()).isFalse()
        assertThat(employee.canApproveShiftSwaps()).isFalse()
        assertThat(employee.canManageEmployees()).isFalse()
        assertThat(employee.canManageSystemRules()).isFalse()
    }
    // אדמין אמור להיות לו הכל
    @Test
    fun `ADMIN should have all permissions`() {
        val admin = UserType.ADMIN

        assertThat(admin.canCreateManagers()).isTrue()
        assertThat(admin.canManageSchedules()).isTrue()
        assertThat(admin.canApproveShiftSwaps()).isTrue()
        assertThat(admin.canViewReports()).isTrue()
        assertThat(admin.canManageEmployees()).isTrue()
        assertThat(admin.canManageSystemRules()).isTrue()
        assertThat(admin.canViewSchedules()).isTrue()
    }
}