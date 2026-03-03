package com.devarthur.setpoint.domain

import kotlin.ConsistentCopyVisibility

/**
 * Perfil do aluno, vinculado a um [User] com role [Role.STUDENT].
 * Criação apenas via [StudentProfile.create].
 *
 * Regra "um StudentProfile por User STUDENT" é garantida pela camada de aplicação/repositório,
 * não pelo domínio puro (ex.: ao persistir, o repositório impede duplicata por userId).
 */
@ConsistentCopyVisibility
data class StudentProfile internal constructor(
    val id: String,
    val userId: String,
    val displayName: String?,
    val createdAt: Long?,
) {
    companion object {
        /**
         * Cria um [StudentProfile] com [userId] válido.
         * @param createdAt epoch millis (opcional); geração fica fora do domínio.
         * @return [Result.success] com StudentProfile ou [Result.failure] com mensagem de erro.
         */
        fun create(
            id: String,
            userId: String,
            displayName: String? = null,
            createdAt: Long? = null,
        ): Result<StudentProfile> {
            when (val error = validateUserId(userId)) {
                null -> { /* ok */ }
                else -> return Result.failure(IllegalArgumentException(error))
            }
            return Result.success(
                StudentProfile(
                    id = id,
                    userId = userId.trim(),
                    displayName = displayName?.trim()?.takeIf { it.isNotEmpty() },
                    createdAt = createdAt,
                )
            )
        }

        internal fun validateUserId(userId: String): String? {
            if (userId.isBlank()) return "userId não pode ser vazio"
            return null
        }
    }
}
