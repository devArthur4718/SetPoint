package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.data.local.InMemoryLocalDataSource
import com.devarthur.setpoint.data.repository.ExerciseRepositoryImpl
import com.devarthur.setpoint.data.repository.WorkoutAssignmentRepositoryImpl
import com.devarthur.setpoint.data.repository.WorkoutTemplateRepositoryImpl
import com.devarthur.setpoint.domain.Exercise
import com.devarthur.setpoint.domain.WorkoutExercise
import com.devarthur.setpoint.domain.WorkoutTemplate
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetMyAssignedWorkoutsUseCaseTest {

    private val local = InMemoryLocalDataSource()
    private val exerciseRepo = ExerciseRepositoryImpl(local)
    private val templateRepo = WorkoutTemplateRepositoryImpl(local)
    private val assignmentRepo = WorkoutAssignmentRepositoryImpl(local)

    private suspend fun saveTemplate(id: String, name: String, trainerId: String) {
        val ex = Exercise.create("ex-$id", "Exercise").getOrThrow()
        exerciseRepo.save(ex).getOrThrow()
        val we = WorkoutExercise.create("we-$id", "ex-$id", 1, 3, 10).getOrThrow()
        val template = WorkoutTemplate.create(id, name, trainerId, listOf(we), null).getOrThrow()
        templateRepo.save(template).getOrThrow()
    }

    private suspend fun saveAssignment(assignmentId: String, templateId: String, studentUserId: String) {
        val assignment = com.devarthur.setpoint.domain.WorkoutAssignment.create(
            assignmentId, templateId, studentUserId, null
        ).getOrThrow()
        assignmentRepo.save(assignment).getOrThrow()
    }

    @Test
    fun execute_withAssignments_returnsAssignedWorkoutsWithTemplates() = runBlocking {
        saveTemplate("wt-1", "Treino A", "trainer-1")
        saveAssignment("wa-1", "wt-1", "student-1")
        val uc = GetMyAssignedWorkoutsUseCase(assignmentRepo, templateRepo)

        val result = uc.execute("student-1")

        assertTrue(result.isSuccess)
        val views = result.getOrThrow()
        assertEquals(1, views.size)
        assertEquals("wa-1", views[0].assignment.id)
        assertEquals("wt-1", views[0].template.id)
        assertEquals("Treino A", views[0].template.name)
        assertEquals(1, views[0].template.exercises.size)
    }

    @Test
    fun execute_noAssignments_returnsEmptyList() = runBlocking {
        val uc = GetMyAssignedWorkoutsUseCase(assignmentRepo, templateRepo)
        val result = uc.execute("student-1")
        assertTrue(result.isSuccess)
        assertEquals(0, result.getOrThrow().size)
    }

    @Test
    fun execute_blankStudentUserId_returnsFailure() = runBlocking {
        val uc = GetMyAssignedWorkoutsUseCase(assignmentRepo, templateRepo)
        val result = uc.execute("")
        assertTrue(result.isFailure)
    }
}
