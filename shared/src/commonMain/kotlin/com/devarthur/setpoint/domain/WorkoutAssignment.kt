package com.devarthur.setpoint.domain

import kotlin.ConsistentCopyVisibility

/**
 * Atribuição de um [WorkoutTemplate] a um aluno (User STUDENT) pelo professor.
 * Criação apenas via [WorkoutAssignment.create].
 */
@ConsistentCopyVisibility
data class WorkoutAssignment internal constructor(
    val id: String,
    val workoutTemplateId: String,
    val studentUserId: String,
    val assignedAt: Long?,
) {
    companion object {
        /**
         * Cria um [WorkoutAssignment] com dados validados.
         * @return [Result.success] com WorkoutAssignment ou [Result.failure] com mensagem de erro.
         */
        fun create(
            id: String,
            workoutTemplateId: String,
            studentUserId: String,
            assignedAt: Long? = null,
        ): Result<WorkoutAssignment> {
            if (workoutTemplateId.isBlank()) return Result.failure(IllegalArgumentException("workoutTemplateId não pode ser vazio"))
            if (studentUserId.isBlank()) return Result.failure(IllegalArgumentException("studentUserId não pode ser vazio"))
            return Result.success(
                WorkoutAssignment(
                    id = id,
                    workoutTemplateId = workoutTemplateId.trim(),
                    studentUserId = studentUserId.trim(),
                    assignedAt = assignedAt,
                )
            )
        }
    }
}
