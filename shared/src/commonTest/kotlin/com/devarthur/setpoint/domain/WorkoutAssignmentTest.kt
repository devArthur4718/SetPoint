package com.devarthur.setpoint.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WorkoutAssignmentTest {

    @Test
    fun create_withValidData_returnsSuccess() {
        val result = WorkoutAssignment.create(
            id = "wa1",
            workoutTemplateId = "wt-1",
            studentUserId = "user-student-1",
        )
        assertTrue(result.isSuccess)
        val wa = result.getOrThrow()
        assertEquals("wa1", wa.id)
        assertEquals("wt-1", wa.workoutTemplateId)
        assertEquals("user-student-1", wa.studentUserId)
        assertEquals(null, wa.assignedAt)
    }

    @Test
    fun create_withAssignedAt_returnsSuccess() {
        val result = WorkoutAssignment.create(
            id = "wa2",
            workoutTemplateId = "wt-2",
            studentUserId = "user-student-2",
            assignedAt = 1_700_000_000_000L,
        )
        assertTrue(result.isSuccess)
        assertEquals(1_700_000_000_000L, result.getOrThrow().assignedAt)
    }

    @Test
    fun create_emptyWorkoutTemplateId_returnsFailure() {
        val result = WorkoutAssignment.create(
            id = "wa1",
            workoutTemplateId = "",
            studentUserId = "user-1",
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("workoutTemplateId"))
    }

    @Test
    fun create_blankWorkoutTemplateId_returnsFailure() {
        val result = WorkoutAssignment.create(
            id = "wa1",
            workoutTemplateId = "   ",
            studentUserId = "user-1",
        )
        assertTrue(result.isFailure)
    }

    @Test
    fun create_emptyStudentUserId_returnsFailure() {
        val result = WorkoutAssignment.create(
            id = "wa1",
            workoutTemplateId = "wt-1",
            studentUserId = "",
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("studentUserId"))
    }

    @Test
    fun create_blankStudentUserId_returnsFailure() {
        val result = WorkoutAssignment.create(
            id = "wa1",
            workoutTemplateId = "wt-1",
            studentUserId = "  \t  ",
        )
        assertTrue(result.isFailure)
    }
}
