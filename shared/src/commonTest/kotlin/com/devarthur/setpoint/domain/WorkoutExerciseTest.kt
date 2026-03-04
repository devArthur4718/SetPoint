package com.devarthur.setpoint.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WorkoutExerciseTest {

    @Test
    fun create_withValidData_returnsSuccess() {
        val result = WorkoutExercise.create(
            id = "we1",
            exerciseId = "ex-1",
            order = 1,
            sets = 3,
            reps = 12,
        )
        assertTrue(result.isSuccess)
        val we = result.getOrThrow()
        assertEquals("we1", we.id)
        assertEquals("ex-1", we.exerciseId)
        assertEquals(1, we.order)
        assertEquals(3, we.sets)
        assertEquals(12, we.reps)
        assertEquals(null, we.loadKg)
        assertEquals(null, we.restSeconds)
    }

    @Test
    fun create_withLoadKgAndRestSeconds_returnsSuccess() {
        val result = WorkoutExercise.create(
            id = "we2",
            exerciseId = "ex-2",
            order = 2,
            sets = 4,
            reps = 10,
            loadKg = 20.0,
            restSeconds = 90,
        )
        assertTrue(result.isSuccess)
        val we = result.getOrThrow()
        assertEquals(20.0, we.loadKg)
        assertEquals(90, we.restSeconds)
    }

    @Test
    fun create_emptyExerciseId_returnsFailure() {
        val result = WorkoutExercise.create(
            id = "we1",
            exerciseId = "",
            order = 1,
            sets = 3,
            reps = 12,
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("exerciseId"))
    }

    @Test
    fun create_blankExerciseId_returnsFailure() {
        val result = WorkoutExercise.create(
            id = "we1",
            exerciseId = "   ",
            order = 1,
            sets = 3,
            reps = 12,
        )
        assertTrue(result.isFailure)
    }

    @Test
    fun create_setsZero_returnsFailure() {
        val result = WorkoutExercise.create(
            id = "we1",
            exerciseId = "ex-1",
            order = 1,
            sets = 0,
            reps = 12,
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("sets"))
    }

    @Test
    fun create_repsZero_returnsFailure() {
        val result = WorkoutExercise.create(
            id = "we1",
            exerciseId = "ex-1",
            order = 1,
            sets = 3,
            reps = 0,
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("reps"))
    }

    @Test
    fun create_negativeLoadKg_returnsFailure() {
        val result = WorkoutExercise.create(
            id = "we1",
            exerciseId = "ex-1",
            order = 1,
            sets = 3,
            reps = 12,
            loadKg = -5.0,
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("loadKg"))
    }

    @Test
    fun create_negativeRestSeconds_returnsFailure() {
        val result = WorkoutExercise.create(
            id = "we1",
            exerciseId = "ex-1",
            order = 1,
            sets = 3,
            reps = 12,
            restSeconds = -10,
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("restSeconds"))
    }
}
