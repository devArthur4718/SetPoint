package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.data.local.InMemoryLocalDataSource
import com.devarthur.setpoint.data.repository.ExerciseRepositoryImpl
import com.devarthur.setpoint.data.repository.UserRepositoryImpl
import com.devarthur.setpoint.data.repository.WorkoutAssignmentRepositoryImpl
import com.devarthur.setpoint.data.repository.WorkoutExecutionRepositoryImpl
import com.devarthur.setpoint.data.repository.WorkoutTemplateRepositoryImpl
import com.devarthur.setpoint.domain.Exercise
import com.devarthur.setpoint.domain.Role
import com.devarthur.setpoint.domain.User
import com.devarthur.setpoint.domain.WorkoutExercise
import com.devarthur.setpoint.domain.WorkoutTemplate
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RecordWorkoutExecutionUseCaseTest {

    private val local = InMemoryLocalDataSource()
    private val exerciseRepo = ExerciseRepositoryImpl(local)
    private val templateRepo = WorkoutTemplateRepositoryImpl(local)
    private val userRepo = UserRepositoryImpl(local)
    private val assignmentRepo = WorkoutAssignmentRepositoryImpl(local)
    private val executionRepo = WorkoutExecutionRepositoryImpl(local)

    private fun idGenerator(vararg ids: String) = object : IdGenerator {
        private var i = 0
        override fun generate(): String = ids[i++]
    }

    private suspend fun saveTemplateWithExercise(templateId: String, workoutExerciseId: String) {
        val ex = Exercise.create("ex-1", "Ex").getOrThrow()
        exerciseRepo.save(ex).getOrThrow()
        val we = WorkoutExercise.create(workoutExerciseId, "ex-1", 1, 3, 10).getOrThrow()
        val template = WorkoutTemplate.create(templateId, "Treino", "trainer-1", listOf(we), null).getOrThrow()
        templateRepo.save(template).getOrThrow()
    }

    private suspend fun saveAssignment(assignmentId: String, templateId: String, studentUserId: String) {
        val a = com.devarthur.setpoint.domain.WorkoutAssignment.create(assignmentId, templateId, studentUserId, null).getOrThrow()
        assignmentRepo.save(a).getOrThrow()
    }

    private suspend fun saveStudent(userId: String) {
        val u = User.create(userId, "a@b.com", "Aluno", Role.STUDENT).getOrThrow()
        userRepo.save(u).getOrThrow()
    }

    @Test
    fun execute_emptySetExecutions_createsExecutionAndPersists() = runBlocking {
        saveTemplateWithExercise("wt-1", "we-1")
        saveAssignment("wa-1", "wt-1", "student-1")
        saveStudent("student-1")
        val idGen = idGenerator("wex-1")
        val uc = RecordWorkoutExecutionUseCase(assignmentRepo, templateRepo, executionRepo, idGen)

        val result = uc.execute(
            workoutAssignmentId = "wa-1",
            studentUserId = "student-1",
            setExecutionItems = emptyList(),
            executedAt = 5000L,
        )

        assertTrue(result.isSuccess)
        val exec = result.getOrThrow()
        assertEquals("wex-1", exec.id)
        assertEquals("wa-1", exec.workoutAssignmentId)
        assertEquals(5000L, exec.executedAt)
        assertEquals(0, exec.setExecutions.size)
        assertEquals(exec, executionRepo.getById("wex-1"))
    }

    @Test
    fun execute_withSetExecutions_createsExecutionWithSetsAndPersists() = runBlocking {
        saveTemplateWithExercise("wt-1", "we-1")
        saveAssignment("wa-1", "wt-1", "student-1")
        saveStudent("student-1")
        val idGen = idGenerator("wex-1", "se-1", "se-2")
        val uc = RecordWorkoutExecutionUseCase(assignmentRepo, templateRepo, executionRepo, idGen)

        val result = uc.execute(
            workoutAssignmentId = "wa-1",
            studentUserId = "student-1",
            setExecutionItems = listOf(
                SetExecutionItem("we-1", setNumber = 1, actualReps = 10, actualLoadKg = 60.0),
                SetExecutionItem("we-1", setNumber = 2, actualReps = 8, actualLoadKg = 60.0),
            ),
            executedAt = 6000L,
        )

        assertTrue(result.isSuccess)
        val exec = result.getOrThrow()
        assertEquals(2, exec.setExecutions.size)
        assertEquals(10, exec.setExecutions[0].actualReps)
        assertEquals(60.0, exec.setExecutions[0].actualLoadKg)
    }

    @Test
    fun execute_assignmentNotOwnedByStudent_returnsFailure() = runBlocking {
        saveTemplateWithExercise("wt-1", "we-1")
        saveAssignment("wa-1", "wt-1", "student-1")
        saveStudent("student-1")
        val idGen = idGenerator("wex-1")
        val uc = RecordWorkoutExecutionUseCase(assignmentRepo, templateRepo, executionRepo, idGen)

        val result = uc.execute(
            workoutAssignmentId = "wa-1",
            studentUserId = "student-other",
            setExecutionItems = emptyList(),
            executedAt = 5000L,
        )

        assertTrue(result.isFailure)
        assertNull(executionRepo.getById("wex-1"))
    }

    @Test
    fun execute_invalidWorkoutExerciseId_returnsFailure() = runBlocking {
        saveTemplateWithExercise("wt-1", "we-1")
        saveAssignment("wa-1", "wt-1", "student-1")
        saveStudent("student-1")
        val idGen = idGenerator("wex-1", "se-1")
        val uc = RecordWorkoutExecutionUseCase(assignmentRepo, templateRepo, executionRepo, idGen)

        val result = uc.execute(
            workoutAssignmentId = "wa-1",
            studentUserId = "student-1",
            setExecutionItems = listOf(SetExecutionItem("we-invalid", setNumber = 1)),
            executedAt = 5000L,
        )

        assertTrue(result.isFailure)
        assertNull(executionRepo.getById("wex-1"))
    }

    @Test
    fun execute_setNumberLessThanOne_returnsFailure() = runBlocking {
        saveTemplateWithExercise("wt-1", "we-1")
        saveAssignment("wa-1", "wt-1", "student-1")
        saveStudent("student-1")
        val idGen = idGenerator("wex-1", "se-1")
        val uc = RecordWorkoutExecutionUseCase(assignmentRepo, templateRepo, executionRepo, idGen)

        val result = uc.execute(
            workoutAssignmentId = "wa-1",
            studentUserId = "student-1",
            setExecutionItems = listOf(SetExecutionItem("we-1", setNumber = 0)),
            executedAt = 5000L,
        )

        assertTrue(result.isFailure)
    }
}
