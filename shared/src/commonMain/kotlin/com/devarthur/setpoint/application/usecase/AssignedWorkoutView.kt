package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.domain.WorkoutAssignment
import com.devarthur.setpoint.domain.WorkoutTemplate

/**
 * Visão de um treino atribuído ao aluno (atribuição + template com exercícios).
 */
data class AssignedWorkoutView(
    val assignment: WorkoutAssignment,
    val template: WorkoutTemplate,
)
