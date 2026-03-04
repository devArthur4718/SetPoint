package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.application.auth.PasswordHasher
import com.devarthur.setpoint.data.local.LocalDataSource
import com.devarthur.setpoint.domain.Role
import com.devarthur.setpoint.domain.repository.UserRepository

/**
 * Caso de uso: login com e-mail e senha.
 * Valida credenciais contra usuário existente e hash de senha (MVP local).
 * Retorna mensagem genérica em falha (não revela se e-mail existe ou não).
 */
class LoginUseCase(
    private val userRepository: UserRepository,
    private val localDataSource: LocalDataSource,
    private val passwordHasher: PasswordHasher,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        expectedRole: Role,
    ): Result<LoginResult> {
        val user = userRepository.getByEmail(email.trim().lowercase())
            ?: return Result.failure(IllegalArgumentException(INVALID_CREDENTIALS_MESSAGE))
        if (user.role != expectedRole) return Result.failure(IllegalArgumentException(INVALID_CREDENTIALS_MESSAGE))
        val storedHash = localDataSource.getPasswordHash(user.id)
            ?: return Result.failure(IllegalArgumentException(INVALID_CREDENTIALS_MESSAGE))
        if (!passwordHasher.verify(password, storedHash)) return Result.failure(IllegalArgumentException(INVALID_CREDENTIALS_MESSAGE))
        return Result.success(LoginResult(userId = user.id, role = user.role))
    }

    companion object {
        /** Mensagem única exibida em qualquer falha de credenciais. */
        const val INVALID_CREDENTIALS_MESSAGE: String = "E-mail ou senha incorretos"
    }
}
