package com.devarthur.setpoint.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StudentProfileTest {

    @Test
    fun createStudentProfile_withValidUserId_returnsSuccess() {
        val result = StudentProfile.create(
            id = "sp1",
            userId = "user-123",
        )
        assertTrue(result.isSuccess)
        val profile = result.getOrThrow()
        assertEquals("sp1", profile.id)
        assertEquals("user-123", profile.userId)
        assertEquals(null, profile.displayName)
        assertEquals(null, profile.createdAt)
    }

    @Test
    fun createStudentProfile_withDisplayNameAndCreatedAt_returnsSuccess() {
        val result = StudentProfile.create(
            id = "sp2",
            userId = "user-456",
            displayName = "Maria Aluna",
            createdAt = 1_700_000_000_000L,
        )
        assertTrue(result.isSuccess)
        val profile = result.getOrThrow()
        assertEquals("sp2", profile.id)
        assertEquals("user-456", profile.userId)
        assertEquals("Maria Aluna", profile.displayName)
        assertEquals(1_700_000_000_000L, profile.createdAt)
    }

    @Test
    fun createStudentProfile_emptyUserId_returnsFailure() {
        val result = StudentProfile.create(
            id = "sp1",
            userId = "",
        )
        assertTrue(result.isFailure)
        assertTrue(
            result.exceptionOrNull()!!.message!!.contains("userId"),
            result.exceptionOrNull()!!.message!!
        )
    }

    @Test
    fun createStudentProfile_blankUserId_returnsFailure() {
        val result = StudentProfile.create(
            id = "sp1",
            userId = "   \t  ",
        )
        assertTrue(result.isFailure)
    }
}
