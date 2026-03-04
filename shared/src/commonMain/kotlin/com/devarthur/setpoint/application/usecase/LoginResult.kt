package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.domain.Role

/**
 * Resultado do caso de uso de login: identificação do usuário autenticado.
 */
data class LoginResult(
    val userId: String,
    val role: Role,
)
