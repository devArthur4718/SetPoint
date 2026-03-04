package com.devarthur.setpoint.data.repository

import com.devarthur.setpoint.data.local.LocalDataSource
import com.devarthur.setpoint.domain.WorkoutAssignment
import com.devarthur.setpoint.domain.repository.WorkoutAssignmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorkoutAssignmentRepositoryImpl(
    private val local: LocalDataSource,
) : WorkoutAssignmentRepository {

    override suspend fun save(assignment: WorkoutAssignment): Result<Unit> = withContext(Dispatchers.Default) {
        local.saveWorkoutAssignment(assignment)
        Result.success(Unit)
    }

    override suspend fun getById(id: String): WorkoutAssignment? = withContext(Dispatchers.Default) {
        local.getWorkoutAssignmentById(id)
    }

    override suspend fun listByStudentUserId(studentUserId: String): List<WorkoutAssignment> = withContext(Dispatchers.Default) {
        local.getWorkoutAssignmentsByStudentUserId(studentUserId)
    }
}
