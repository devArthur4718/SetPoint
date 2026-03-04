package com.devarthur.setpoint.domain.repository

import com.devarthur.setpoint.domain.StudentProfile

interface StudentProfileRepository {
    suspend fun save(profile: StudentProfile): Result<Unit>
    suspend fun getById(id: String): StudentProfile?
    suspend fun getByUserId(userId: String): StudentProfile?
}
