package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.domain.Role
import com.devarthur.setpoint.domain.StudentProfile
import com.devarthur.setpoint.domain.User
import com.devarthur.setpoint.domain.repository.StudentProfileRepository
import com.devarthur.setpoint.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Caso de uso: professor cria um aluno (User STUDENT + StudentProfile).
 * Não acessa UI nem rede; usa apenas repositórios e domínio.
 */
class CreateStudentUseCase(
    private val userRepository: UserRepository,
    private val studentProfileRepository: StudentProfileRepository,
    private val idGenerator: IdGenerator,
) {
    /**
     * Cria um aluno com os dados validados.
     * @param email Email do aluno (deve ser válido e único).
     * @param name Nome do aluno (1–120 caracteres).
     * @param displayName Nome de exibição opcional (para o professor identificar na lista).
     * @param trainerId Contexto do professor que está criando (não persistido no resultado).
     * @return Result.success com [CreateStudentResult] ou Result.failure com mensagem de erro.
     */
    suspend fun execute(
        email: String,
        name: String,
        displayName: String? = null,
        trainerId: String,
    ): Result<CreateStudentResult> = withContext(Dispatchers.Default) {
        val userId = idGenerator.generate()
        val userResult = User.create(userId, email, name, Role.STUDENT)
        val user = userResult.getOrElse { return@withContext Result.failure(it) }

        userRepository.save(user).getOrElse { return@withContext Result.failure(it) }

        val profileId = idGenerator.generate()
        val profileResult = StudentProfile.create(
            id = profileId,
            userId = userId,
            displayName = displayName,
            createdAt = null,
        )
        val profile = profileResult.getOrElse { return@withContext Result.failure(it) }

        studentProfileRepository.save(profile).getOrElse { return@withContext Result.failure(it) }

        Result.success(CreateStudentResult(user = user, studentProfile = profile))
    }
}
