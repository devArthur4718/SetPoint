package com.devarthur.setpoint.domain.repository

import com.devarthur.setpoint.domain.User

interface UserRepository {
    suspend fun save(user: User): Result<Unit>
    suspend fun getById(id: String): User?
    suspend fun getByEmail(email: String): User?
    suspend fun list(): List<User>
}
