package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.data.local.InMemoryLocalDataSource
import com.devarthur.setpoint.data.repository.WorkoutAssignmentRepositoryImpl
import com.devarthur.setpoint.data.repository.WorkoutExecutionRepositoryImpl
import com.devarthur.setpoint.domain.WorkoutExecution
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetMyWorkoutHistoryUseCaseTest {

    private val local = InMemoryLocalDataSource()
    private val assignmentRepo = WorkoutAssignmentRepositoryImpl(local)
    private val executionRepo = WorkoutExecutionRepositoryImpl(local)

    private suspend fun saveAssignment(assignmentId: String, templateId: String, studentUserId: String) {
        val a = com.devarthur.setpoint.domain.WorkoutAssignment.create(assignmentId, templateId, studentUserId, null).getOrThrow()
        assignmentRepo.save(a).getOrThrow()
    }

    private suspend fun saveExecution(executionId: String, assignmentId: String, executedAt: Long) {
        val e = WorkoutExecution.create(executionId, assignmentId, executedAt, emptyList()).getOrThrow()
        executionRepo.save(e).getOrThrow()
    }

    @Test
    fun execute_withExecutions_returnsOrderedByExecutedAtDescending() = runBlocking {
        saveAssignment("wa-1", "wt-1", "student-1")
        saveExecution("wex-1", "wa-1", 1000L)
        saveExecution("wex-2", "wa-1", 3000L)
        saveExecution("wex-3", "wa-1", 2000L)
        val uc = GetMyWorkoutHistoryUseCase(assignmentRepo, executionRepo)

        val result = uc.execute("student-1")

        assertTrue(result.isSuccess)
        val list = result.getOrThrow()
        assertEquals(3, list.size)
        assertEquals(3000L, list[0].executedAt)
        assertEquals(2000L, list[1].executedAt)
        assertEquals(1000L, list[2].executedAt)
    }

    @Test
    fun execute_noExecutions_returnsEmptyList() = runBlocking {
        saveAssignment("wa-1", "wt-1", "student-1")
        val uc = GetMyWorkoutHistoryUseCase(assignmentRepo, executionRepo)
        val result = uc.execute("student-1")
        assertTrue(result.isSuccess)
        assertEquals(0, result.getOrThrow().size)
    }

    @Test
    fun execute_blankStudentUserId_returnsFailure() = runBlocking {
        val uc = GetMyWorkoutHistoryUseCase(assignmentRepo, executionRepo)
        val result = uc.execute("")
        assertTrue(result.isFailure)
    }
}
