package com.devarthur.setpoint.domain

import kotlin.ConsistentCopyVisibility

/**
 * Template de treino criado pelo professor.
 * Criação apenas via [WorkoutTemplate.create].
 * A lista de exercises é armazenada ordenada por [WorkoutExercise.order].
 */
@ConsistentCopyVisibility
data class WorkoutTemplate internal constructor(
    val id: String,
    val name: String,
    val trainerId: String,
    val exercises: List<WorkoutExercise>,
    val createdAt: Long?,
) {
    init {
        require(exercises.isNotEmpty()) { "exercises não pode ser vazio" }
    }

    companion object {
        /**
         * Cria um [WorkoutTemplate] com dados validados.
         * A lista [exercises] é copiada e ordenada por [WorkoutExercise.order].
         * @return [Result.success] com WorkoutTemplate ou [Result.failure] com mensagem de erro.
         */
        fun create(
            id: String,
            name: String,
            trainerId: String,
            exercises: List<WorkoutExercise>,
            createdAt: Long? = null,
        ): Result<WorkoutTemplate> {
            when (val error = User.validateName(name)) {
                null -> { /* ok */ }
                else -> return Result.failure(IllegalArgumentException(error))
            }
            if (trainerId.isBlank()) return Result.failure(IllegalArgumentException("trainerId não pode ser vazio"))
            if (exercises.isEmpty()) return Result.failure(IllegalArgumentException("exercises não pode ser vazio"))
            val sortedExercises = exercises.sortedBy { it.order }
            return Result.success(
                WorkoutTemplate(
                    id = id,
                    name = name.trim(),
                    trainerId = trainerId.trim(),
                    exercises = sortedExercises,
                    createdAt = createdAt,
                )
            )
        }
    }
}
