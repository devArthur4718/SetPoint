package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.domain.repository.WorkoutAssignmentRepository
import com.devarthur.setpoint.domain.repository.WorkoutTemplateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Caso de uso: aluno visualiza os treinos atribuídos a ele.
 * Dados lidos do cache local (offline-first).
 */
class GetMyAssignedWorkoutsUseCase(
    private val workoutAssignmentRepository: WorkoutAssignmentRepository,
    private val workoutTemplateRepository: WorkoutTemplateRepository,
) {
    /**
     * Retorna a lista de treinos atribuídos ao aluno (atribuição + template com exercícios).
     * Atribuições cujo template não existe são omitidas.
     * @param studentUserId Id do User aluno.
     * @return Result.success com lista de [AssignedWorkoutView] ou Result.failure.
     */
    suspend fun execute(studentUserId: String): Result<List<AssignedWorkoutView>> = withContext(Dispatchers.Default) {
        if (studentUserId.isBlank()) return@withContext Result.failure(IllegalArgumentException("studentUserId não pode ser vazio"))

        val assignments = workoutAssignmentRepository.listByStudentUserId(studentUserId)
        val views = assignments.mapNotNull { assignment ->
            val template = workoutTemplateRepository.getById(assignment.workoutTemplateId) ?: return@mapNotNull null
            AssignedWorkoutView(assignment = assignment, template = template)
        }
        Result.success(views)
    }
}
