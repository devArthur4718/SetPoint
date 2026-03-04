package com.devarthur.setpoint.data.repository

import com.devarthur.setpoint.data.local.LocalDataSource
import com.devarthur.setpoint.domain.StudentProfile
import com.devarthur.setpoint.domain.repository.StudentProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StudentProfileRepositoryImpl(
    private val local: LocalDataSource,
) : StudentProfileRepository {

    override suspend fun save(profile: StudentProfile): Result<Unit> = withContext(Dispatchers.Default) {
        val existing = local.getStudentProfileByUserId(profile.userId)
        if (existing != null && existing.id != profile.id) {
            return@withContext Result.failure(IllegalArgumentException("Já existe perfil de aluno para este userId"))
        }
        local.saveStudentProfile(profile)
        Result.success(Unit)
    }

    override suspend fun getById(id: String): StudentProfile? = withContext(Dispatchers.Default) {
        local.getStudentProfileById(id)
    }

    override suspend fun getByUserId(userId: String): StudentProfile? = withContext(Dispatchers.Default) {
        local.getStudentProfileByUserId(userId)
    }
}
