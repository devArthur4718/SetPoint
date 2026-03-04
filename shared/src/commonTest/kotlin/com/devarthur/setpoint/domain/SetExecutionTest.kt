package com.devarthur.setpoint.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SetExecutionTest {

    @Test
    fun create_withValidData_returnsSuccess() {
        val result = SetExecution.create(
            id = "se1",
            workoutExecutionId = "wex-1",
            workoutExerciseId = "we-1",
            setNumber = 1,
        )
        assertTrue(result.isSuccess)
        val se = result.getOrThrow()
        assertEquals("se1", se.id)
        assertEquals("wex-1", se.workoutExecutionId)
        assertEquals("we-1", se.workoutExerciseId)
        assertEquals(1, se.setNumber)
        assertEquals(null, se.actualReps)
        assertEquals(null, se.actualLoadKg)
    }

    @Test
    fun create_withActualRepsAndLoad_returnsSuccess() {
        val result = SetExecution.create(
            id = "se2",
            workoutExecutionId = "wex-1",
            workoutExerciseId = "we-1",
            setNumber = 2,
            actualReps = 12,
            actualLoadKg = 20.0,
        )
        assertTrue(result.isSuccess)
        val se = result.getOrThrow()
        assertEquals(12, se.actualReps)
        assertEquals(20.0, se.actualLoadKg)
    }

    @Test
    fun create_emptyWorkoutExecutionId_returnsFailure() {
        val result = SetExecution.create(
            id = "se1",
            workoutExecutionId = "",
            workoutExerciseId = "we-1",
            setNumber = 1,
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("workoutExecutionId"))
    }

    @Test
    fun create_emptyWorkoutExerciseId_returnsFailure() {
        val result = SetExecution.create(
            id = "se1",
            workoutExecutionId = "wex-1",
            workoutExerciseId = "",
            setNumber = 1,
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("workoutExerciseId"))
    }

    @Test
    fun create_setNumberZero_returnsFailure() {
        val result = SetExecution.create(
            id = "se1",
            workoutExecutionId = "wex-1",
            workoutExerciseId = "we-1",
            setNumber = 0,
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("setNumber"))
    }

    @Test
    fun create_negativeActualReps_returnsFailure() {
        val result = SetExecution.create(
            id = "se1",
            workoutExecutionId = "wex-1",
            workoutExerciseId = "we-1",
            setNumber = 1,
            actualReps = -1,
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("actualReps"))
    }

    @Test
    fun create_negativeActualLoadKg_returnsFailure() {
        val result = SetExecution.create(
            id = "se1",
            workoutExecutionId = "wex-1",
            workoutExerciseId = "we-1",
            setNumber = 1,
            actualLoadKg = -5.0,
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("actualLoadKg"))
    }
}
