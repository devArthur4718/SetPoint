package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.domain.StudentProfile
import com.devarthur.setpoint.domain.User

/**
 * Resultado do caso de uso de criar aluno: User e StudentProfile criados.
 */
data class CreateStudentResult(
    val user: User,
    val studentProfile: StudentProfile,
)
