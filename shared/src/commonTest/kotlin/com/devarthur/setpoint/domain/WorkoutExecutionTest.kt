package com.devarthur.setpoint.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WorkoutExecutionTest {

    private fun validSetExecution(setNumber: Int) = SetExecution.create(
        id = "se-$setNumber",
        workoutExecutionId = "wex-1",
        workoutExerciseId = "we-1",
        setNumber = setNumber,
        actualReps = 12,
    ).getOrThrow()

    @Test
    fun create_withValidDataAndEmptySetExecutions_returnsSuccess() {
        val result = WorkoutExecution.create(
            id = "wex1",
            workoutAssignmentId = "wa-1",
            executedAt = 1_700_000_000_000L,
            setExecutions = emptyList(),
        )
        assertTrue(result.isSuccess)
        val wex = result.getOrThrow()
        assertEquals("wex1", wex.id)
        assertEquals("wa-1", wex.workoutAssignmentId)
        assertEquals(1_700_000_000_000L, wex.executedAt)
        assertTrue(wex.setExecutions.isEmpty())
    }

    @Test
    fun create_withSetExecutions_returnsSuccess() {
        val sets = listOf(validSetExecution(1), validSetExecution(2))
        val result = WorkoutExecution.create(
            id = "wex2",
            workoutAssignmentId = "wa-2",
            executedAt = 1_700_000_001_000L,
            setExecutions = sets,
        )
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrThrow().setExecutions.size)
    }

    @Test
    fun create_emptyWorkoutAssignmentId_returnsFailure() {
        val result = WorkoutExecution.create(
            id = "wex1",
            workoutAssignmentId = "",
            executedAt = 1_700_000_000_000L,
            setExecutions = emptyList(),
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("workoutAssignmentId"))
    }

    @Test
    fun create_blankWorkoutAssignmentId_returnsFailure() {
        val result = WorkoutExecution.create(
            id = "wex1",
            workoutAssignmentId = "   \t  ",
            executedAt = 1_700_000_000_000L,
            setExecutions = emptyList(),
        )
        assertTrue(result.isFailure)
    }
}
