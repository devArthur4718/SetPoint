package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.domain.Role
import com.devarthur.setpoint.domain.repository.UserRepository
import com.devarthur.setpoint.domain.repository.WorkoutAssignmentRepository
import com.devarthur.setpoint.domain.repository.WorkoutExecutionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Caso de uso: professor visualiza o histórico de execuções de um aluno.
 * Dados lidos do cache local (offline-first).
 */
class GetStudentWorkoutHistoryUseCase(
    private val userRepository: UserRepository,
    private val workoutAssignmentRepository: WorkoutAssignmentRepository,
    private val workoutExecutionRepository: WorkoutExecutionRepository,
) {
    /**
     * Retorna as atribuições do aluno e as execuções de cada uma.
     * @param studentUserId Id do User aluno (role STUDENT).
     * @param trainerId Contexto do professor (auditoria; MVP não restringe por trainer).
     * @return Result.success com [StudentWorkoutHistoryResult] ou Result.failure.
     */
    suspend fun execute(
        studentUserId: String,
        trainerId: String,
    ): Result<StudentWorkoutHistoryResult> = withContext(Dispatchers.Default) {
        if (studentUserId.isBlank()) return@withContext Result.failure(IllegalArgumentException("studentUserId não pode ser vazio"))

        val student = userRepository.getById(studentUserId)
            ?: return@withContext Result.failure(IllegalArgumentException("Aluno não encontrado: $studentUserId"))
        if (student.role != Role.STUDENT) {
            return@withContext Result.failure(IllegalArgumentException("Usuário não é aluno (role: ${student.role})"))
        }

        val assignments = workoutAssignmentRepository.listByStudentUserId(studentUserId)
        val assignmentsWithExecutions = assignments.map { assignment ->
            val executions = workoutExecutionRepository.listByWorkoutAssignmentId(assignment.id)
            StudentWorkoutHistoryResult.AssignmentWithExecutions(assignment = assignment, executions = executions)
        }
        Result.success(StudentWorkoutHistoryResult(assignmentsWithExecutions = assignmentsWithExecutions))
    }
}
