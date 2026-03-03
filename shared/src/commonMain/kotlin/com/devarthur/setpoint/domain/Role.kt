package com.devarthur.setpoint.domain

/**
 * Papel do usuário no sistema.
 * - TRAINER: personal trainer (gerencia alunos e treinos).
 * - STUDENT: aluno (executa treinos atribuídos).
 */
enum class Role {
    TRAINER,
    STUDENT,
}
