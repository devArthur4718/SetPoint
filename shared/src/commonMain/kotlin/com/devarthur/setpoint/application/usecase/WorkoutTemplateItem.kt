package com.devarthur.setpoint.application.usecase

/**
 * Entrada para um exercício dentro do template no caso de uso de criar treino.
 * @param exerciseId Referência a um [com.devarthur.setpoint.domain.Exercise] existente.
 * @param order Posição no treino (1-based).
 * @param sets Número de séries (≥ 1).
 * @param reps Número de repetições por série (≥ 1).
 * @param loadKg Carga em kg (opcional, ≥ 0 quando presente).
 * @param restSeconds Descanso em segundos entre séries (opcional, ≥ 0 quando presente).
 */
data class WorkoutTemplateItem(
    val exerciseId: String,
    val order: Int,
    val sets: Int,
    val reps: Int,
    val loadKg: Double? = null,
    val restSeconds: Int? = null,
)
