package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.domain.WorkoutAssignment
import com.devarthur.setpoint.domain.WorkoutExecution

/**
 * Resultado do caso de uso de histórico do aluno (professor).
 * Agrupa as atribuições do aluno e as execuções de cada uma.
 */
data class StudentWorkoutHistoryResult(
    val assignmentsWithExecutions: List<AssignmentWithExecutions>,
) {
    data class AssignmentWithExecutions(
        val assignment: WorkoutAssignment,
        val executions: List<WorkoutExecution>,
    )
}
