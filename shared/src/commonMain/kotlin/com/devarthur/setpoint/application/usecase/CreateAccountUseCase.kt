package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.application.auth.PasswordHasher
import com.devarthur.setpoint.data.local.LocalDataSource
import com.devarthur.setpoint.domain.Role
import com.devarthur.setpoint.domain.StudentProfile
import com.devarthur.setpoint.domain.User
import com.devarthur.setpoint.domain.repository.StudentProfileRepository
import com.devarthur.setpoint.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val PASSWORD_MIN_LENGTH = 6
private const val MESSAGE_EMAIL_ALREADY_REGISTERED = "E-mail já cadastrado"
private const val MESSAGE_PASSWORD_TOO_SHORT = "Senha deve ter no mínimo $PASSWORD_MIN_LENGTH caracteres"
private const val MESSAGE_INVALID_ROLE = "Papel deve ser Professor ou Aluno"

/**
 * Caso de uso: autocadastro — criar conta com e-mail, senha, nome e papel (TRAINER ou STUDENT).
 * Cria User, persiste hash da senha e, se STUDENT, cria StudentProfile.
 * Não exige usuário autenticado.
 */
class CreateAccountUseCase(
    private val userRepository: UserRepository,
    private val studentProfileRepository: StudentProfileRepository,
    private val localDataSource: LocalDataSource,
    private val passwordHasher: PasswordHasher,
    private val idGenerator: IdGenerator,
) {
    /**
     * Cria uma conta com os dados validados.
     * @param email E-mail (único no sistema).
     * @param password Senha em texto plano (mín. 6 caracteres; será armazenado apenas o hash).
     * @param name Nome (1–120 caracteres).
     * @param role TRAINER ou STUDENT.
     * @param displayName Opcional; usado quando role = STUDENT.
     * @return Result.success com [CreateAccountResult] ou Result.failure com mensagem de erro.
     */
    suspend fun execute(
        email: String,
        password: String,
        name: String,
        role: Role,
        displayName: String? = null,
    ): Result<CreateAccountResult> = withContext(Dispatchers.Default) {
        val emailTrimmed = email.trim()
        val emailNormalized = emailTrimmed.lowercase()

        when (val emailError = User.validateEmail(emailTrimmed)) {
            null -> { /* ok */ }
            else -> return@withContext Result.failure(IllegalArgumentException(emailError))
        }
        if (password.length < PASSWORD_MIN_LENGTH) {
            return@withContext Result.failure(IllegalArgumentException(MESSAGE_PASSWORD_TOO_SHORT))
        }
        when (val nameError = User.validateName(name)) {
            null -> { /* ok */ }
            else -> return@withContext Result.failure(IllegalArgumentException(nameError))
        }
        if (role != Role.TRAINER && role != Role.STUDENT) {
            return@withContext Result.failure(IllegalArgumentException(MESSAGE_INVALID_ROLE))
        }

        val existingUser = userRepository.getByEmail(emailNormalized)
        if (existingUser != null) {
            return@withContext Result.failure(IllegalArgumentException(MESSAGE_EMAIL_ALREADY_REGISTERED))
        }

        val userId = idGenerator.generate()
        val passwordHash = passwordHasher.hash(password)

        val userResult = User.create(
            id = userId,
            email = emailNormalized,
            name = name.trim(),
            role = role,
        )
        val user = userResult.getOrElse { return@withContext Result.failure(it) }

        userRepository.save(user).getOrElse { return@withContext Result.failure(it) }
        localDataSource.setPasswordHash(userId, passwordHash)

        if (role == Role.STUDENT) {
            val profileId = idGenerator.generate()
            val profileResult = StudentProfile.create(
                id = profileId,
                userId = userId,
                displayName = displayName,
                createdAt = null,
            )
            val profile = profileResult.getOrElse { return@withContext Result.failure(it) }
            studentProfileRepository.save(profile).getOrElse { return@withContext Result.failure(it) }
        }

        Result.success(
            CreateAccountResult(
                userId = user.id,
                email = user.email,
                name = user.name,
                role = user.role,
            ),
        )
    }
}
