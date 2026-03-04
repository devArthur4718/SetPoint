package com.devarthur.setpoint.data.repository

import com.devarthur.setpoint.data.local.LocalDataSource
import com.devarthur.setpoint.domain.User
import com.devarthur.setpoint.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val local: LocalDataSource,
) : UserRepository {

    override suspend fun save(user: User): Result<Unit> = withContext(Dispatchers.Default) {
        val existing = local.getUserByEmail(user.email)
        if (existing != null && existing.id != user.id) {
            return@withContext Result.failure(IllegalArgumentException("Já existe usuário com este email"))
        }
        local.saveUser(user)
        Result.success(Unit)
    }

    override suspend fun getById(id: String): User? = withContext(Dispatchers.Default) {
        local.getUserById(id)
    }

    override suspend fun getByEmail(email: String): User? = withContext(Dispatchers.Default) {
        local.getUserByEmail(email)
    }

    override suspend fun list(): List<User> = withContext(Dispatchers.Default) {
        local.getAllUsers()
    }
}
