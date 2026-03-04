package com.devarthur.setpoint.domain.repository

import com.devarthur.setpoint.domain.WorkoutAssignment

interface WorkoutAssignmentRepository {
    suspend fun save(assignment: WorkoutAssignment): Result<Unit>
    suspend fun getById(id: String): WorkoutAssignment?
    suspend fun listByStudentUserId(studentUserId: String): List<WorkoutAssignment>
}
