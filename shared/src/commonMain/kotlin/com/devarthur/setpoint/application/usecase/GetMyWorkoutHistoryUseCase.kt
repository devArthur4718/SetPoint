package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.domain.WorkoutExecution
import com.devarthur.setpoint.domain.repository.WorkoutAssignmentRepository
import com.devarthur.setpoint.domain.repository.WorkoutExecutionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Caso de uso: aluno visualiza seu próprio histórico de execuções.
 * Dados lidos do cache local (offline-first). Ordenado por executedAt decrescente (mais recente primeiro).
 */
class GetMyWorkoutHistoryUseCase(
    private val workoutAssignmentRepository: WorkoutAssignmentRepository,
    private val workoutExecutionRepository: WorkoutExecutionRepository,
) {
    /**
     * Retorna a lista de execuções do aluno, ordenada por data (mais recente primeiro).
     * @param studentUserId Id do User aluno.
     * @return Result.success com lista de [WorkoutExecution] ou Result.failure.
     */
    suspend fun execute(studentUserId: String): Result<List<WorkoutExecution>> = withContext(Dispatchers.Default) {
        if (studentUserId.isBlank()) return@withContext Result.failure(IllegalArgumentException("studentUserId não pode ser vazio"))

        val assignments = workoutAssignmentRepository.listByStudentUserId(studentUserId)
        val allExecutions = assignments.flatMap { assignment ->
            workoutExecutionRepository.listByWorkoutAssignmentId(assignment.id)
        }
        val sorted = allExecutions.sortedByDescending { it.executedAt }
        Result.success(sorted)
    }
}
