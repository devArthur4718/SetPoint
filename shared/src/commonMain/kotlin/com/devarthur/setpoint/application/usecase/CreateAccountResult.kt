package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.domain.Role

/**
 * Resultado do caso de uso [CreateAccountUseCase].
 * Expõe os dados do usuário criado (autocadastro).
 */
data class CreateAccountResult(
    val userId: String,
    val email: String,
    val name: String,
    val role: Role,
)
