package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.data.local.InMemoryLocalDataSource
import com.devarthur.setpoint.data.repository.ExerciseRepositoryImpl
import com.devarthur.setpoint.data.repository.UserRepositoryImpl
import com.devarthur.setpoint.data.repository.WorkoutAssignmentRepositoryImpl
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

class AssignWorkoutToStudentUseCaseTest {

    private val local = InMemoryLocalDataSource()
    private val exerciseRepo = ExerciseRepositoryImpl(local)
    private val templateRepo = WorkoutTemplateRepositoryImpl(local)
    private val userRepo = UserRepositoryImpl(local)
    private val assignmentRepo = WorkoutAssignmentRepositoryImpl(local)

    private fun fixedIdGenerator(assignmentId: String) = object : IdGenerator {
        override fun generate(): String = assignmentId
    }

    private suspend fun saveTemplate(id: String, name: String, trainerId: String) {
        val ex = Exercise.create("ex-$id", "Exercise").getOrThrow()
        exerciseRepo.save(ex).getOrThrow()
        val we = WorkoutExercise.create("we-$id", "ex-$id", 1, 3, 10).getOrThrow()
        val template = WorkoutTemplate.create(id, name, trainerId, listOf(we), null).getOrThrow()
        templateRepo.save(template).getOrThrow()
    }

    private suspend fun saveStudent(userId: String, email: String, name: String) {
        val user = User.create(userId, email, name, Role.STUDENT).getOrThrow()
        userRepo.save(user).getOrThrow()
    }

    @Test
    fun execute_withValidData_returnsSuccessAndPersists() = runBlocking {
        saveTemplate("wt-1", "Treino A", "trainer-1")
        saveStudent("student-1", "aluno@test.com", "Maria")
        val idGen = fixedIdGenerator("wa-1")
        val uc = AssignWorkoutToStudentUseCase(templateRepo, userRepo, assignmentRepo, idGen)

        val result = uc.execute(
            workoutTemplateId = "wt-1",
            studentUserId = "student-1",
            trainerId = "trainer-1",
        )

        assertTrue(result.isSuccess)
        val assignment = result.getOrThrow()
        assertEquals("wa-1", assignment.id)
        assertEquals("wt-1", assignment.workoutTemplateId)
        assertEquals("student-1", assignment.studentUserId)
        assertNull(assignment.assignedAt)

        assertEquals(assignment, assignmentRepo.getById("wa-1"))
        assertEquals(listOf(assignment), assignmentRepo.listByStudentUserId("student-1"))
    }

    @Test
    fun execute_templateNotFound_returnsFailureAndDoesNotPersist() = runBlocking {
        saveStudent("student-1", "aluno@test.com", "Maria")
        val idGen = fixedIdGenerator("wa-1")
        val uc = AssignWorkoutToStudentUseCase(templateRepo, userRepo, assignmentRepo, idGen)

        val result = uc.execute(
            workoutTemplateId = "wt-nonexistent",
            studentUserId = "student-1",
            trainerId = "trainer-1",
        )

        assertTrue(result.isFailure)
        assertNull(assignmentRepo.getById("wa-1"))
    }

    @Test
    fun execute_templateBelongsToOtherTrainer_returnsFailureAndDoesNotPersist() = runBlocking {
        saveTemplate("wt-1", "Treino A", "trainer-1")
        saveStudent("student-1", "aluno@test.com", "Maria")
        val idGen = fixedIdGenerator("wa-1")
        val uc = AssignWorkoutToStudentUseCase(templateRepo, userRepo, assignmentRepo, idGen)

        val result = uc.execute(
            workoutTemplateId = "wt-1",
            studentUserId = "student-1",
            trainerId = "trainer-other",
        )

        assertTrue(result.isFailure)
        assertNull(assignmentRepo.getById("wa-1"))
    }

    @Test
    fun execute_studentNotFound_returnsFailureAndDoesNotPersist() = runBlocking {
        saveTemplate("wt-1", "Treino A", "trainer-1")
        val idGen = fixedIdGenerator("wa-1")
        val uc = AssignWorkoutToStudentUseCase(templateRepo, userRepo, assignmentRepo, idGen)

        val result = uc.execute(
            workoutTemplateId = "wt-1",
            studentUserId = "student-nonexistent",
            trainerId = "trainer-1",
        )

        assertTrue(result.isFailure)
        assertNull(assignmentRepo.getById("wa-1"))
    }

    @Test
    fun execute_userIsNotStudent_returnsFailureAndDoesNotPersist() = runBlocking {
        saveTemplate("wt-1", "Treino A", "trainer-1")
        val trainerUser = User.create("trainer-1", "trainer@test.com", "Coach", Role.TRAINER).getOrThrow()
        userRepo.save(trainerUser).getOrThrow()
        val idGen = fixedIdGenerator("wa-1")
        val uc = AssignWorkoutToStudentUseCase(templateRepo, userRepo, assignmentRepo, idGen)

        val result = uc.execute(
            workoutTemplateId = "wt-1",
            studentUserId = "trainer-1",
            trainerId = "trainer-1",
        )

        assertTrue(result.isFailure)
        assertNull(assignmentRepo.getById("wa-1"))
    }

    @Test
    fun execute_blankWorkoutTemplateId_returnsFailureAndDoesNotPersist() = runBlocking {
        saveTemplate("wt-1", "Treino A", "trainer-1")
        saveStudent("student-1", "aluno@test.com", "Maria")
        val idGen = fixedIdGenerator("wa-1")
        val uc = AssignWorkoutToStudentUseCase(templateRepo, userRepo, assignmentRepo, idGen)

        val result = uc.execute(
            workoutTemplateId = "",
            studentUserId = "student-1",
            trainerId = "trainer-1",
        )

        assertTrue(result.isFailure)
        assertNull(assignmentRepo.getById("wa-1"))
    }

    @Test
    fun execute_blankStudentUserId_returnsFailureAndDoesNotPersist() = runBlocking {
        saveTemplate("wt-1", "Treino A", "trainer-1")
        saveStudent("student-1", "aluno@test.com", "Maria")
        val idGen = fixedIdGenerator("wa-1")
        val uc = AssignWorkoutToStudentUseCase(templateRepo, userRepo, assignmentRepo, idGen)

        val result = uc.execute(
            workoutTemplateId = "wt-1",
            studentUserId = "",
            trainerId = "trainer-1",
        )

        assertTrue(result.isFailure)
        assertNull(assignmentRepo.getById("wa-1"))
    }
}
