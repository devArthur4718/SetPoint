package com.devarthur.setpoint.data.repository

import com.devarthur.setpoint.data.local.LocalDataSource
import com.devarthur.setpoint.domain.WorkoutExecution
import com.devarthur.setpoint.domain.repository.WorkoutExecutionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorkoutExecutionRepositoryImpl(
    private val local: LocalDataSource,
) : WorkoutExecutionRepository {

    override suspend fun save(execution: WorkoutExecution): Result<Unit> = withContext(Dispatchers.Default) {
        local.saveWorkoutExecution(execution)
        Result.success(Unit)
    }

    override suspend fun getById(id: String): WorkoutExecution? = withContext(Dispatchers.Default) {
        local.getWorkoutExecutionById(id)
    }

    override suspend fun listByWorkoutAssignmentId(assignmentId: String): List<WorkoutExecution> = withContext(Dispatchers.Default) {
        local.getWorkoutExecutionsByAssignmentId(assignmentId)
    }
}
