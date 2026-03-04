package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.domain.SetExecution
import com.devarthur.setpoint.domain.WorkoutExecution
import com.devarthur.setpoint.domain.repository.WorkoutAssignmentRepository
import com.devarthur.setpoint.domain.repository.WorkoutExecutionRepository
import com.devarthur.setpoint.domain.repository.WorkoutTemplateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Caso de uso: aluno registra uma execução de treino (WorkoutExecution e opcionalmente SetExecutions).
 */
class RecordWorkoutExecutionUseCase(
    private val workoutAssignmentRepository: WorkoutAssignmentRepository,
    private val workoutTemplateRepository: WorkoutTemplateRepository,
    private val workoutExecutionRepository: WorkoutExecutionRepository,
    private val idGenerator: IdGenerator,
) {
    /**
     * Registra uma execução do treino atribuído.
     * @param workoutAssignmentId Id da atribuição que o aluno está executando.
     * @param studentUserId Id do aluno (deve ser o dono da atribuição).
     * @param setExecutionItems Lista de séries realizadas (pode ser vazia).
     * @param executedAt Epoch millis da execução (obrigatório).
     * @return Result.success com [WorkoutExecution] criada ou Result.failure.
     */
    suspend fun execute(
        workoutAssignmentId: String,
        studentUserId: String,
        setExecutionItems: List<SetExecutionItem>,
        executedAt: Long,
    ): Result<WorkoutExecution> = withContext(Dispatchers.Default) {
        if (workoutAssignmentId.isBlank()) return@withContext Result.failure(IllegalArgumentException("workoutAssignmentId não pode ser vazio"))
        if (studentUserId.isBlank()) return@withContext Result.failure(IllegalArgumentException("studentUserId não pode ser vazio"))

        val assignment = workoutAssignmentRepository.getById(workoutAssignmentId)
            ?: return@withContext Result.failure(IllegalArgumentException("Atribuição não encontrada: $workoutAssignmentId"))
        if (assignment.studentUserId != studentUserId) {
            return@withContext Result.failure(IllegalArgumentException("Atribuição não pertence ao aluno"))
        }

        val template = workoutTemplateRepository.getById(assignment.workoutTemplateId)
            ?: return@withContext Result.failure(IllegalArgumentException("Template da atribuição não encontrado"))
        val validExerciseIds = template.exercises.map { it.id }.toSet()

        val setExecutions = mutableListOf<SetExecution>()
        val executionId = idGenerator.generate()
        for (item in setExecutionItems) {
            if (item.workoutExerciseId !in validExerciseIds) {
                return@withContext Result.failure(IllegalArgumentException("workoutExerciseId não pertence ao template: ${item.workoutExerciseId}"))
            }
            val setId = idGenerator.generate()
            val setResult = SetExecution.create(
                id = setId,
                workoutExecutionId = executionId,
                workoutExerciseId = item.workoutExerciseId,
                setNumber = item.setNumber,
                actualReps = item.actualReps,
                actualLoadKg = item.actualLoadKg,
            )
            val setExecution = setResult.getOrElse { return@withContext Result.failure(it) }
            setExecutions.add(setExecution)
        }

        val executionResult = WorkoutExecution.create(
            id = executionId,
            workoutAssignmentId = workoutAssignmentId,
            executedAt = executedAt,
            setExecutions = setExecutions,
        )
        val execution = executionResult.getOrElse { return@withContext Result.failure(it) }

        workoutExecutionRepository.save(execution).getOrElse { return@withContext Result.failure(it) }

        Result.success(execution)
    }
}
