package com.devarthur.setpoint.domain

import kotlin.ConsistentCopyVisibility

/**
 * Exercício do catálogo (ex.: "Supino reto", "Agachamento").
 * Criação apenas via [Exercise.create].
 */
@ConsistentCopyVisibility
data class Exercise internal constructor(
    val id: String,
    val name: String,
    val description: String?,
) {
    companion object {
        /**
         * Cria um [Exercise] com nome validado.
         * Reutiliza regra de nome: não vazio, máximo 120 caracteres.
         * @return [Result.success] com Exercise ou [Result.failure] com mensagem de erro.
         */
        fun create(
            id: String,
            name: String,
            description: String? = null,
        ): Result<Exercise> {
            when (val error = User.validateName(name)) {
                null -> { /* ok */ }
                else -> return Result.failure(IllegalArgumentException(error))
            }
            return Result.success(
                Exercise(
                    id = id,
                    name = name.trim(),
                    description = description?.trim()?.takeIf { it.isNotEmpty() },
                )
            )
        }
    }
}
