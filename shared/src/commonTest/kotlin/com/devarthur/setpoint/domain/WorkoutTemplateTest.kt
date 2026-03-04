package com.devarthur.setpoint.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WorkoutTemplateTest {

    private fun validWorkoutExercise(order: Int) = WorkoutExercise.create(
        id = "we-$order",
        exerciseId = "ex-$order",
        order = order,
        sets = 3,
        reps = 12,
    ).getOrThrow()

    @Test
    fun create_withValidData_returnsSuccess() {
        val exercises = listOf(validWorkoutExercise(1), validWorkoutExercise(2))
        val result = WorkoutTemplate.create(
            id = "wt1",
            name = "Treino A",
            trainerId = "trainer-123",
            exercises = exercises,
        )
        assertTrue(result.isSuccess)
        val wt = result.getOrThrow()
        assertEquals("wt1", wt.id)
        assertEquals("Treino A", wt.name)
        assertEquals("trainer-123", wt.trainerId)
        assertEquals(2, wt.exercises.size)
        assertEquals(null, wt.createdAt)
    }

    @Test
    fun create_withCreatedAt_returnsSuccess() {
        val exercises = listOf(validWorkoutExercise(1))
        val result = WorkoutTemplate.create(
            id = "wt1",
            name = "Treino B",
            trainerId = "trainer-456",
            exercises = exercises,
            createdAt = 1_700_000_000_000L,
        )
        assertTrue(result.isSuccess)
        assertEquals(1_700_000_000_000L, result.getOrThrow().createdAt)
    }

    @Test
    fun create_ordersExercisesByOrder() {
        val exercises = listOf(
            validWorkoutExercise(2),
            validWorkoutExercise(1),
        )
        val result = WorkoutTemplate.create(
            id = "wt1",
            name = "Treino",
            trainerId = "t1",
            exercises = exercises,
        )
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().exercises[0].order)
        assertEquals(2, result.getOrThrow().exercises[1].order)
    }

    @Test
    fun create_emptyName_returnsFailure() {
        val exercises = listOf(validWorkoutExercise(1))
        val result = WorkoutTemplate.create(
            id = "wt1",
            name = "",
            trainerId = "t1",
            exercises = exercises,
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("Nome"))
    }

    @Test
    fun create_nameOver120Characters_returnsFailure() {
        val exercises = listOf(validWorkoutExercise(1))
        val result = WorkoutTemplate.create(
            id = "wt1",
            name = "a".repeat(121),
            trainerId = "t1",
            exercises = exercises,
        )
        assertTrue(result.isFailure)
    }

    @Test
    fun create_emptyTrainerId_returnsFailure() {
        val exercises = listOf(validWorkoutExercise(1))
        val result = WorkoutTemplate.create(
            id = "wt1",
            name = "Treino",
            trainerId = "",
            exercises = exercises,
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("trainerId"))
    }

    @Test
    fun create_blankTrainerId_returnsFailure() {
        val exercises = listOf(validWorkoutExercise(1))
        val result = WorkoutTemplate.create(
            id = "wt1",
            name = "Treino",
            trainerId = "   ",
            exercises = exercises,
        )
        assertTrue(result.isFailure)
    }

    @Test
    fun create_emptyExercisesList_returnsFailure() {
        val result = WorkoutTemplate.create(
            id = "wt1",
            name = "Treino",
            trainerId = "t1",
            exercises = emptyList(),
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("exercises"))
    }
}
