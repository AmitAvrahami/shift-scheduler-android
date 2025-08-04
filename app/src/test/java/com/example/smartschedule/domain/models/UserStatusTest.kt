package com.example.smartschedule.domain.models

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import org.junit.Test
import com.example.smartschedule.core.domain.models.UserStatus


class UserStatusTest {

    @Test
    fun `ACTIVE user should be able to login`() {
        assertThat(UserStatus.ACTIVE.canLogin()).isTrue()
    }

    @Test
    fun `INACTIVE user should not be able to login`() {
        assertThat(UserStatus.INACTIVE.canLogin()).isFalse()
    }

    @Test
    fun `PENDING user should not be able to login`() {
        assertThat(UserStatus.PENDING.canLogin()).isFalse()
    }


    @Test
    fun `ACTIVE user should be visible in lists`() {
        assertThat(UserStatus.ACTIVE.isVisible()).isTrue()
    }

    @Test
    fun `PENDING user should be visible in lists`() {
        assertThat(UserStatus.PENDING.isVisible()).isTrue()
    }

    @Test
    fun `INACTIVE user should not be visible in lists`() {
        assertThat(UserStatus.INACTIVE.isVisible()).isFalse()
    }

    @Test
    fun `only ACTIVE users should have both login and visibility permissions`() {
        // רק משתמש פעיל אמור להיות לו הכל
        val activeStatus = UserStatus.ACTIVE

        assertThat(activeStatus.canLogin()).isTrue()
        assertThat(activeStatus.isVisible()).isTrue()
    }

    @Test
    fun `INACTIVE users should have no permissions`() {
        // משתמש לא פעיל - אין לו כלום
        val inactiveStatus = UserStatus.INACTIVE

        assertThat(inactiveStatus.canLogin()).isFalse()
        assertThat(inactiveStatus.isVisible()).isFalse()
    }

    @Test
    fun `PENDING users should be visible but cannot login`() {
        // משתמש ממתין - רואים אותו אבל הוא לא יכול להתחבר
        val pendingStatus = UserStatus.PENDING

        assertThat(pendingStatus.isVisible()).isTrue()
        assertThat(pendingStatus.canLogin()).isFalse()
    }

    @Test
    fun `verify status business rules for all statuses`() {
        // בדיקה מקיפה של כל הסטטוסים בפעם אחת
        val expectations = mapOf(
            UserStatus.ACTIVE to Pair(true, true),      // canLogin, isVisible
            UserStatus.PENDING to Pair(false, true),     // canLogin, isVisible
            UserStatus.INACTIVE to Pair(false, false)    // canLogin, isVisible
        )

        expectations.forEach { (status, expected) ->
            val (expectedCanLogin, expectedIsVisible) = expected
            assertWithMessage("$status.canLogin()")
                .that(status.canLogin())
                .isEqualTo(expectedCanLogin)

            assertWithMessage("$status.isVisible()")
                .that(status.isVisible())
                .isEqualTo(expectedIsVisible)
        }
    }

    @Test
    fun `displayName should be in Hebrew for all statuses`() {
        // וידוא שכל הסטטוסים יש להם שם תצוגה בעברית
        assertThat(UserStatus.ACTIVE.displayName).isEqualTo("פעיל")
        assertThat(UserStatus.INACTIVE.displayName).isEqualTo("לא פעיל")
        assertThat(UserStatus.PENDING.displayName).isEqualTo("ממתין לאישור")
    }

    @Test
    fun `all statuses should have non-empty display names`() {
        // וידוא שאין סטטוס עם שם ריק
        UserStatus.entries.forEach { status ->
            assertWithMessage("$status should have display name")
                .that(status.displayName)
                .isNotEmpty()
        }
    }
}

