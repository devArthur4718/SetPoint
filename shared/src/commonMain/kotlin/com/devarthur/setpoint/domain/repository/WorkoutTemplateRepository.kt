package com.devarthur.setpoint.domain.repository

import com.devarthur.setpoint.domain.WorkoutTemplate

interface WorkoutTemplateRepository {
    suspend fun save(template: WorkoutTemplate): Result<Unit>
    suspend fun getById(id: String): WorkoutTemplate?
    suspend fun listByTrainerId(trainerId: String): List<WorkoutTemplate>
}
