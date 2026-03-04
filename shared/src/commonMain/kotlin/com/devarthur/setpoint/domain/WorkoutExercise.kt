package com.devarthur.setpoint.domain

import kotlin.ConsistentCopyVisibility

/**
 * Um exercício dentro de um treino (template), com parâmetros de execução.
 * Criação apenas via [WorkoutExercise.create].
 */
@ConsistentCopyVisibility
data class WorkoutExercise internal constructor(
    val id: String,
    val exerciseId: String,
    val order: Int,
    val sets: Int,
    val reps: Int,
    val loadKg: Double?,
    val restSeconds: Int?,
) {
    companion object {
        /**
         * Cria um [WorkoutExercise] com dados validados.
         * @return [Result.success] com WorkoutExercise ou [Result.failure] com mensagem de erro.
         */
        fun create(
            id: String,
            exerciseId: String,
            order: Int,
            sets: Int,
            reps: Int,
            loadKg: Double? = null,
            restSeconds: Int? = null,
        ): Result<WorkoutExercise> {
            when (val error = validate(exerciseId, sets, reps, loadKg, restSeconds)) {
                null -> { /* ok */ }
                else -> return Result.failure(IllegalArgumentException(error))
            }
            return Result.success(
                WorkoutExercise(
                    id = id,
                    exerciseId = exerciseId.trim(),
                    order = order,
                    sets = sets,
                    reps = reps,
                    loadKg = loadKg?.takeIf { it >= 0 },
                    restSeconds = restSeconds?.takeIf { it >= 0 },
                )
            )
        }

        internal fun validate(
            exerciseId: String,
            sets: Int,
            reps: Int,
            loadKg: Double?,
            restSeconds: Int?,
        ): String? {
            if (exerciseId.isBlank()) return "exerciseId não pode ser vazio"
            if (sets < 1) return "sets deve ser >= 1"
            if (reps < 1) return "reps deve ser >= 1"
            if (loadKg != null && loadKg < 0) return "loadKg não pode ser negativo"
            if (restSeconds != null && restSeconds < 0) return "restSeconds não pode ser negativo"
            return null
        }
    }
}
