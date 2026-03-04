package com.devarthur.setpoint.data.repository

import com.devarthur.setpoint.data.local.LocalDataSource
import com.devarthur.setpoint.domain.Exercise
import com.devarthur.setpoint.domain.repository.ExerciseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExerciseRepositoryImpl(
    private val local: LocalDataSource,
) : ExerciseRepository {

    override suspend fun save(exercise: Exercise): Result<Unit> = withContext(Dispatchers.Default) {
        local.saveExercise(exercise)
        Result.success(Unit)
    }

    override suspend fun getById(id: String): Exercise? = withContext(Dispatchers.Default) {
        local.getExerciseById(id)
    }

    override suspend fun list(): List<Exercise> = withContext(Dispatchers.Default) {
        local.getAllExercises()
    }
}
