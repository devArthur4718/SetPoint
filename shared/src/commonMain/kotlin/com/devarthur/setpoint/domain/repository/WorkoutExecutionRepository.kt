package com.devarthur.setpoint.domain.repository

import com.devarthur.setpoint.domain.WorkoutExecution

interface WorkoutExecutionRepository {
    suspend fun save(execution: WorkoutExecution): Result<Unit>
    suspend fun getById(id: String): WorkoutExecution?
    suspend fun listByWorkoutAssignmentId(assignmentId: String): List<WorkoutExecution>
}
