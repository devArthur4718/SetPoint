package com.devarthur.setpoint.domain.repository

import com.devarthur.setpoint.domain.Exercise

interface ExerciseRepository {
    suspend fun save(exercise: Exercise): Result<Unit>
    suspend fun getById(id: String): Exercise?
    suspend fun list(): List<Exercise>
}
