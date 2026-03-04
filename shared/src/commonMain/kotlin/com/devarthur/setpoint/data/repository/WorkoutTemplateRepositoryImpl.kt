package com.devarthur.setpoint.data.repository

import com.devarthur.setpoint.data.local.LocalDataSource
import com.devarthur.setpoint.domain.WorkoutTemplate
import com.devarthur.setpoint.domain.repository.WorkoutTemplateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorkoutTemplateRepositoryImpl(
    private val local: LocalDataSource,
) : WorkoutTemplateRepository {

    override suspend fun save(template: WorkoutTemplate): Result<Unit> = withContext(Dispatchers.Default) {
        local.saveWorkoutTemplate(template)
        Result.success(Unit)
    }

    override suspend fun getById(id: String): WorkoutTemplate? = withContext(Dispatchers.Default) {
        local.getWorkoutTemplateById(id)
    }

    override suspend fun listByTrainerId(trainerId: String): List<WorkoutTemplate> = withContext(Dispatchers.Default) {
        local.getWorkoutTemplatesByTrainerId(trainerId)
    }
}
