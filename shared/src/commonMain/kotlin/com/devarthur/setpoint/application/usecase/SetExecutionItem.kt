package com.devarthur.setpoint.application.usecase

/**
 * Entrada para uma série registrada no caso de uso de executar treino.
 */
data class SetExecutionItem(
    val workoutExerciseId: String,
    val setNumber: Int,
    val actualReps: Int? = null,
    val actualLoadKg: Double? = null,
)
