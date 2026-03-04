package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.data.local.InMemoryLocalDataSource
import com.devarthur.setpoint.data.repository.ExerciseRepositoryImpl
import com.devarthur.setpoint.data.repository.WorkoutTemplateRepositoryImpl
import com.devarthur.setpoint.domain.Exercise
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CreateWorkoutTemplateUseCaseTest {

    private val local = InMemoryLocalDataSource()
    private val exerciseRepo = ExerciseRepositoryImpl(local)
    private val templateRepo = WorkoutTemplateRepositoryImpl(local)

    private fun fixedIdGenerator(vararg ids: String) = object : IdGenerator {
        private var index = 0
        override fun generate(): String = ids[index++]
    }

    private suspend fun saveExercise(id: String, name: String) {
        val ex = Exercise.create(id, name).getOrThrow()
        exerciseRepo.save(ex).getOrThrow()
    }

    @Test
    fun execute_withValidData_returnsSuccessAndPersists() = runBlocking {
        saveExercise("ex-1", "Supino reto")
        saveExercise("ex-2", "Agachamento")
        val idGen = fixedIdGenerator("wt-1", "we-1", "we-2")
        val uc = CreateWorkoutTemplateUseCase(exerciseRepo, templateRepo, idGen)

        val result = uc.execute(
            name = "Treino A",
            trainerId = "trainer-1",
            items = listOf(
                WorkoutTemplateItem(exerciseId = "ex-1", order = 1, sets = 3, reps = 10, loadKg = 60.0, restSeconds = 90),
                WorkoutTemplateItem(exerciseId = "ex-2", order = 2, sets = 4, reps = 8, restSeconds = 120),
            ),
        )

        assertTrue(result.isSuccess)
        val template = result.getOrThrow()
        assertEquals("wt-1", template.id)
        assertEquals("Treino A", template.name)
        assertEquals("trainer-1", template.trainerId)
        assertEquals(2, template.exercises.size)
        assertEquals("we-1", template.exercises[0].id)
        assertEquals("ex-1", template.exercises[0].exerciseId)
        assertEquals(1, template.exercises[0].order)
        assertEquals(3, template.exercises[0].sets)
        assertEquals(10, template.exercises[0].reps)
        assertEquals(60.0, template.exercises[0].loadKg)
        assertEquals(90, template.exercises[0].restSeconds)
        assertEquals("we-2", template.exercises[1].id)
        assertEquals("ex-2", template.exercises[1].exerciseId)
        assertEquals(2, template.exercises[1].order)
        assertEquals(4, template.exercises[1].sets)
        assertEquals(8, template.exercises[1].reps)
        assertNull(template.exercises[1].loadKg)
        assertEquals(120, template.exercises[1].restSeconds)

        assertEquals(template, templateRepo.getById("wt-1"))
        assertEquals(listOf(template), templateRepo.listByTrainerId("trainer-1"))
    }

    @Test
    fun execute_emptyName_returnsFailureAndDoesNotPersist() = runBlocking {
        saveExercise("ex-1", "Supino")
        val idGen = fixedIdGenerator("wt-1", "we-1")
        val uc = CreateWorkoutTemplateUseCase(exerciseRepo, templateRepo, idGen)

        val result = uc.execute(
            name = "",
            trainerId = "trainer-1",
            items = listOf(WorkoutTemplateItem(exerciseId = "ex-1", order = 1, sets = 3, reps = 10)),
        )

        assertTrue(result.isFailure)
        assertNull(templateRepo.getById("wt-1"))
    }

    @Test
    fun execute_emptyList_returnsFailureAndDoesNotPersist() = runBlocking {
        saveExercise("ex-1", "Supino")
        val idGen = fixedIdGenerator("wt-1")
        val uc = CreateWorkoutTemplateUseCase(exerciseRepo, templateRepo, idGen)

        val result = uc.execute(
            name = "Treino",
            trainerId = "trainer-1",
            items = emptyList(),
        )

        assertTrue(result.isFailure)
        assertNull(templateRepo.getById("wt-1"))
    }

    @Test
    fun execute_exerciseNotFound_returnsFailureAndDoesNotPersist() = runBlocking {
        saveExercise("ex-1", "Supino")
        val idGen = fixedIdGenerator("wt-1", "we-1", "we-2")
        val uc = CreateWorkoutTemplateUseCase(exerciseRepo, templateRepo, idGen)

        val result = uc.execute(
            name = "Treino",
            trainerId = "trainer-1",
            items = listOf(
                WorkoutTemplateItem(exerciseId = "ex-1", order = 1, sets = 3, reps = 10),
                WorkoutTemplateItem(exerciseId = "ex-nonexistent", order = 2, sets = 3, reps = 8),
            ),
        )

        assertTrue(result.isFailure)
        assertNull(templateRepo.getById("wt-1"))
    }

    @Test
    fun execute_invalidSets_returnsFailureAndDoesNotPersist() = runBlocking {
        saveExercise("ex-1", "Supino")
        val idGen = fixedIdGenerator("wt-1", "we-1")
        val uc = CreateWorkoutTemplateUseCase(exerciseRepo, templateRepo, idGen)

        val result = uc.execute(
            name = "Treino",
            trainerId = "trainer-1",
            items = listOf(WorkoutTemplateItem(exerciseId = "ex-1", order = 1, sets = 0, reps = 10)),
        )

        assertTrue(result.isFailure)
        assertNull(templateRepo.getById("wt-1"))
    }

    @Test
    fun execute_invalidReps_returnsFailureAndDoesNotPersist() = runBlocking {
        saveExercise("ex-1", "Supino")
        val idGen = fixedIdGenerator("wt-1", "we-1")
        val uc = CreateWorkoutTemplateUseCase(exerciseRepo, templateRepo, idGen)

        val result = uc.execute(
            name = "Treino",
            trainerId = "trainer-1",
            items = listOf(WorkoutTemplateItem(exerciseId = "ex-1", order = 1, sets = 3, reps = 0)),
        )

        assertTrue(result.isFailure)
        assertNull(templateRepo.getById("wt-1"))
    }

    @Test
    fun execute_negativeLoadKg_returnsFailureAndDoesNotPersist() = runBlocking {
        saveExercise("ex-1", "Supino")
        val idGen = fixedIdGenerator("wt-1", "we-1")
        val uc = CreateWorkoutTemplateUseCase(exerciseRepo, templateRepo, idGen)

        val result = uc.execute(
            name = "Treino",
            trainerId = "trainer-1",
            items = listOf(WorkoutTemplateItem(exerciseId = "ex-1", order = 1, sets = 3, reps = 10, loadKg = -5.0)),
        )

        assertTrue(result.isFailure)
        assertNull(templateRepo.getById("wt-1"))
    }
}
