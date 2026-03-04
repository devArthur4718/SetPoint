package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.data.local.InMemoryLocalDataSource
import com.devarthur.setpoint.data.repository.ExerciseRepositoryImpl
import com.devarthur.setpoint.data.repository.UserRepositoryImpl
import com.devarthur.setpoint.data.repository.WorkoutAssignmentRepositoryImpl
import com.devarthur.setpoint.data.repository.WorkoutExecutionRepositoryImpl
import com.devarthur.setpoint.data.repository.WorkoutTemplateRepositoryImpl
import com.devarthur.setpoint.domain.Exercise
import com.devarthur.setpoint.domain.Role
import com.devarthur.setpoint.domain.SetExecution
import com.devarthur.setpoint.domain.User
import com.devarthur.setpoint.domain.WorkoutExercise
import com.devarthur.setpoint.domain.WorkoutExecution
import com.devarthur.setpoint.domain.WorkoutTemplate
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetStudentWorkoutHistoryUseCaseTest {

    private val local = InMemoryLocalDataSource()
    private val userRepo = UserRepositoryImpl(local)
    private val assignmentRepo = WorkoutAssignmentRepositoryImpl(local)
    private val executionRepo = WorkoutExecutionRepositoryImpl(local)

    private suspend fun saveStudent(userId: String, email: String, name: String) {
        val user = User.create(userId, email, name, Role.STUDENT).getOrThrow()
        userRepo.save(user).getOrThrow()
    }

    private suspend fun saveAssignment(assignmentId: String, templateId: String, studentUserId: String) {
        val assignment = com.devarthur.setpoint.domain.WorkoutAssignment.create(
            assignmentId, templateId, studentUserId, null
        ).getOrThrow()
        assignmentRepo.save(assignment).getOrThrow()
    }

    private suspend fun saveExecution(executionId: String, assignmentId: String, executedAt: Long, setExecutions: List<SetExecution> = emptyList()) {
        val execution = WorkoutExecution.create(executionId, assignmentId, executedAt, setExecutions).getOrThrow()
        executionRepo.save(execution).getOrThrow()
    }

    @Test
    fun execute_withAssignmentsAndExecutions_returnsHistory() = runBlocking {
        saveStudent("student-1", "a@b.com", "Maria")
        saveAssignment("wa-1", "wt-1", "student-1")
        saveExecution("wex-1", "wa-1", 1000L)
        saveExecution("wex-2", "wa-1", 2000L)
        val uc = GetStudentWorkoutHistoryUseCase(userRepo, assignmentRepo, executionRepo)

        val result = uc.execute(studentUserId = "student-1", trainerId = "trainer-1")

        assertTrue(result.isSuccess)
        val history = result.getOrThrow()
        assertEquals(1, history.assignmentsWithExecutions.size)
        assertEquals("wa-1", history.assignmentsWithExecutions[0].assignment.id)
        assertEquals(2, history.assignmentsWithExecutions[0].executions.size)
        assertEquals(listOf(1000L, 2000L), history.assignmentsWithExecutions[0].executions.map { it.executedAt }.sorted())
    }

    @Test
    fun execute_studentNotFound_returnsFailure() = runBlocking {
        val uc = GetStudentWorkoutHistoryUseCase(userRepo, assignmentRepo, executionRepo)
        val result = uc.execute(studentUserId = "nonexistent", trainerId = "trainer-1")
        assertTrue(result.isFailure)
    }

    @Test
    fun execute_userNotStudent_returnsFailure() = runBlocking {
        val trainer = User.create("t1", "t@b.com", "Coach", Role.TRAINER).getOrThrow()
        userRepo.save(trainer).getOrThrow()
        val uc = GetStudentWorkoutHistoryUseCase(userRepo, assignmentRepo, executionRepo)
        val result = uc.execute(studentUserId = "t1", trainerId = "trainer-1")
        assertTrue(result.isFailure)
    }

    @Test
    fun execute_studentWithNoAssignments_returnsEmptyStructure() = runBlocking {
        saveStudent("student-1", "a@b.com", "Maria")
        val uc = GetStudentWorkoutHistoryUseCase(userRepo, assignmentRepo, executionRepo)
        val result = uc.execute(studentUserId = "student-1", trainerId = "trainer-1")
        assertTrue(result.isSuccess)
        assertEquals(0, result.getOrThrow().assignmentsWithExecutions.size)
    }
}
