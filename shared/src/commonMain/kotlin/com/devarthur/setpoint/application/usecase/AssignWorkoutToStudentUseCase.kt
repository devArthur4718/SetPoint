package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.domain.Role
import com.devarthur.setpoint.domain.WorkoutAssignment
import com.devarthur.setpoint.domain.repository.UserRepository
import com.devarthur.setpoint.domain.repository.WorkoutAssignmentRepository
import com.devarthur.setpoint.domain.repository.WorkoutTemplateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Caso de uso: professor atribui um template de treino a um aluno (WorkoutAssignment).
 * Não acessa UI nem rede; usa apenas repositórios e domínio.
 */
class AssignWorkoutToStudentUseCase(
    private val workoutTemplateRepository: WorkoutTemplateRepository,
    private val userRepository: UserRepository,
    private val workoutAssignmentRepository: WorkoutAssignmentRepository,
    private val idGenerator: IdGenerator,
) {
    /**
     * Atribui um treino (template) a um aluno.
     * @param workoutTemplateId Id do WorkoutTemplate a ser atribuído.
     * @param studentUserId Id do User aluno (deve ser role STUDENT).
     * @param trainerId Contexto do professor que está atribuindo (o template deve ser dele).
     * @return Result.success com [WorkoutAssignment] criado ou Result.failure com mensagem de erro.
     */
    suspend fun execute(
        workoutTemplateId: String,
        studentUserId: String,
        trainerId: String,
    ): Result<WorkoutAssignment> = withContext(Dispatchers.Default) {
        if (workoutTemplateId.isBlank()) return@withContext Result.failure(IllegalArgumentException("workoutTemplateId não pode ser vazio"))
        if (studentUserId.isBlank()) return@withContext Result.failure(IllegalArgumentException("studentUserId não pode ser vazio"))

        val template = workoutTemplateRepository.getById(workoutTemplateId)
            ?: return@withContext Result.failure(IllegalArgumentException("Template não encontrado: $workoutTemplateId"))
        if (template.trainerId != trainerId) {
            return@withContext Result.failure(IllegalArgumentException("Template não pertence ao professor"))
        }

        val student = userRepository.getById(studentUserId)
            ?: return@withContext Result.failure(IllegalArgumentException("Aluno não encontrado: $studentUserId"))
        if (student.role != Role.STUDENT) {
            return@withContext Result.failure(IllegalArgumentException("Usuário não é aluno (role: ${student.role})"))
        }

        val assignmentId = idGenerator.generate()
        val assignmentResult = WorkoutAssignment.create(
            id = assignmentId,
            workoutTemplateId = workoutTemplateId,
            studentUserId = studentUserId,
            assignedAt = null,
        )
        val assignment = assignmentResult.getOrElse { return@withContext Result.failure(it) }

        workoutAssignmentRepository.save(assignment).getOrElse { return@withContext Result.failure(it) }

        Result.success(assignment)
    }
}
