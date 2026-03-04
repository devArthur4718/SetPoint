package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.domain.User
import com.devarthur.setpoint.domain.WorkoutExercise
import com.devarthur.setpoint.domain.WorkoutTemplate
import com.devarthur.setpoint.domain.repository.ExerciseRepository
import com.devarthur.setpoint.domain.repository.WorkoutTemplateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Caso de uso: professor cria um template de treino (WorkoutTemplate com WorkoutExercise).
 * Não acessa UI nem rede; usa apenas repositórios e domínio.
 */
class CreateWorkoutTemplateUseCase(
    private val exerciseRepository: ExerciseRepository,
    private val workoutTemplateRepository: WorkoutTemplateRepository,
    private val idGenerator: IdGenerator,
) {
    /**
     * Cria um template de treino com os dados validados.
     * @param name Nome do treino (1–120 caracteres).
     * @param trainerId Contexto do professor que está criando.
     * @param items Lista de exercícios do treino (exerciseId, order, sets, reps, loadKg?, restSeconds?).
     * @return Result.success com [WorkoutTemplate] criado ou Result.failure com mensagem de erro.
     */
    suspend fun execute(
        name: String,
        trainerId: String,
        items: List<WorkoutTemplateItem>,
    ): Result<WorkoutTemplate> = withContext(Dispatchers.Default) {
        if (items.isEmpty()) return@withContext Result.failure(IllegalArgumentException("Lista de exercícios não pode ser vazia"))
        when (val nameError = User.validateName(name)) {
            null -> { /* ok */ }
            else -> return@withContext Result.failure(IllegalArgumentException(nameError))
        }
        if (trainerId.isBlank()) return@withContext Result.failure(IllegalArgumentException("trainerId não pode ser vazio"))

        for (item in items) {
            if (exerciseRepository.getById(item.exerciseId) == null) {
                return@withContext Result.failure(IllegalArgumentException("Exercício não encontrado: ${item.exerciseId}"))
            }
        }

        val templateId = idGenerator.generate()
        val workoutExercises = mutableListOf<WorkoutExercise>()
        for (item in items) {
            val weId = idGenerator.generate()
            val weResult = WorkoutExercise.create(
                id = weId,
                exerciseId = item.exerciseId,
                order = item.order,
                sets = item.sets,
                reps = item.reps,
                loadKg = item.loadKg,
                restSeconds = item.restSeconds,
            )
            val we = weResult.getOrElse { return@withContext Result.failure(it) }
            workoutExercises.add(we)
        }

        val templateResult = WorkoutTemplate.create(
            id = templateId,
            name = name,
            trainerId = trainerId,
            exercises = workoutExercises,
            createdAt = null,
        )
        val template = templateResult.getOrElse { return@withContext Result.failure(it) }

        workoutTemplateRepository.save(template).getOrElse { return@withContext Result.failure(it) }

        Result.success(template)
    }
}
