package com.devarthur.setpoint.domain

import kotlin.ConsistentCopyVisibility

/**
 * Registro de uma série executada (repetições e carga reais).
 * Criação apenas via [SetExecution.create].
 */
@ConsistentCopyVisibility
data class SetExecution internal constructor(
    val id: String,
    val workoutExecutionId: String,
    val workoutExerciseId: String,
    val setNumber: Int,
    val actualReps: Int?,
    val actualLoadKg: Double?,
) {
    companion object {
        /**
         * Cria um [SetExecution] com dados validados.
         * @return [Result.success] com SetExecution ou [Result.failure] com mensagem de erro.
         */
        fun create(
            id: String,
            workoutExecutionId: String,
            workoutExerciseId: String,
            setNumber: Int,
            actualReps: Int? = null,
            actualLoadKg: Double? = null,
        ): Result<SetExecution> {
            when (val error = validate(workoutExecutionId, workoutExerciseId, setNumber, actualReps, actualLoadKg)) {
                null -> { /* ok */ }
                else -> return Result.failure(IllegalArgumentException(error))
            }
            return Result.success(
                SetExecution(
                    id = id,
                    workoutExecutionId = workoutExecutionId.trim(),
                    workoutExerciseId = workoutExerciseId.trim(),
                    setNumber = setNumber,
                    actualReps = actualReps?.takeIf { it >= 0 },
                    actualLoadKg = actualLoadKg?.takeIf { it >= 0 },
                )
            )
        }

        internal fun validate(
            workoutExecutionId: String,
            workoutExerciseId: String,
            setNumber: Int,
            actualReps: Int?,
            actualLoadKg: Double?,
        ): String? {
            if (workoutExecutionId.isBlank()) return "workoutExecutionId não pode ser vazio"
            if (workoutExerciseId.isBlank()) return "workoutExerciseId não pode ser vazio"
            if (setNumber < 1) return "setNumber deve ser >= 1"
            if (actualReps != null && actualReps < 0) return "actualReps não pode ser negativo"
            if (actualLoadKg != null && actualLoadKg < 0) return "actualLoadKg não pode ser negativo"
            return null
        }
    }
}
