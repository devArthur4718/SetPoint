package com.devarthur.setpoint.domain

import kotlin.ConsistentCopyVisibility

/**
 * Usuário do sistema. Criação apenas via [User.create].
 * Regra: email único no sistema (garantido pela camada de persistência).
 */
@ConsistentCopyVisibility
data class User internal constructor(
    val id: String,
    val email: String,
    val name: String,
    val role: Role,
) {
    companion object {
        private const val NAME_MAX_LENGTH = 120

        /** Regex simples para formato de email (não valida domínio real). */
        private val EMAIL_REGEX = Regex(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        )

        /**
         * Cria um [User] com dados validados.
         * @return [Result.success] com User ou [Result.failure] com mensagem de erro.
         */
        fun create(
            id: String,
            email: String,
            name: String,
            role: Role,
        ): Result<User> {
            when (val emailError = validateEmail(email)) {
                null -> { /* ok */ }
                else -> return Result.failure(IllegalArgumentException(emailError))
            }
            when (val nameError = validateName(name)) {
                null -> { /* ok */ }
                else -> return Result.failure(IllegalArgumentException(nameError))
            }
            return Result.success(User(id = id, email = email.trim(), name = name.trim(), role = role))
        }

        internal fun validateEmail(email: String): String? {
            val trimmed = email.trim()
            if (trimmed.isEmpty()) return "Email não pode ser vazio"
            if (!EMAIL_REGEX.matches(trimmed)) return "Email em formato inválido"
            return null
        }

        internal fun validateName(name: String): String? {
            val trimmed = name.trim()
            if (trimmed.isEmpty()) return "Nome não pode ser vazio"
            if (trimmed.length > NAME_MAX_LENGTH) return "Nome deve ter no máximo $NAME_MAX_LENGTH caracteres"
            return null
        }
    }
}
