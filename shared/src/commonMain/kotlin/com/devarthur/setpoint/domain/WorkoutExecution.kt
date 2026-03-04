package com.devarthur.setpoint.domain

import kotlin.ConsistentCopyVisibility

/**
 * Uma sessão de execução do treino pelo aluno (uma vez que o aluno realizou o treino).
 * Criação apenas via [WorkoutExecution.create].
 */
@ConsistentCopyVisibility
data class WorkoutExecution internal constructor(
    val id: String,
    val workoutAssignmentId: String,
    val executedAt: Long,
    val setExecutions: List<SetExecution>,
) {
    companion object {
        /**
         * Cria um [WorkoutExecution] com dados validados.
         * [setExecutions] pode ser vazia; a lista é copiada para imutabilidade.
         * @return [Result.success] com WorkoutExecution ou [Result.failure] com mensagem de erro.
         */
        fun create(
            id: String,
            workoutAssignmentId: String,
            executedAt: Long,
            setExecutions: List<SetExecution>,
        ): Result<WorkoutExecution> {
            if (workoutAssignmentId.isBlank()) return Result.failure(IllegalArgumentException("workoutAssignmentId não pode ser vazio"))
            return Result.success(
                WorkoutExecution(
                    id = id,
                    workoutAssignmentId = workoutAssignmentId.trim(),
                    executedAt = executedAt,
                    setExecutions = setExecutions.toList(),
                )
            )
        }
    }
}
