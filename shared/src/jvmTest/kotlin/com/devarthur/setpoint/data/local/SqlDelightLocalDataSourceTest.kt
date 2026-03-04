package com.devarthur.setpoint.data.local

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.devarthur.setpoint.data.repository.UserRepositoryImpl
import com.devarthur.setpoint.db.SetPointDb
import com.devarthur.setpoint.domain.Role
import com.devarthur.setpoint.domain.User
import kotlinx.coroutines.runBlocking
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Testes para SqlDelightLocalDataSource.
 * - saveUser + getUserById: valida que a implementação persistente grava e lê (in-memory).
 * - UserRepository com SqlDelightLocalDataSource: valida que repositórios existentes funcionam com a nova implementação.
 */
class SqlDelightLocalDataSourceTest {

    private var driver: JdbcSqliteDriver? = null

    @AfterTest
    fun tearDown() {
        driver?.close()
    }

    @Test
    fun saveUser_andGetUserById_returnsSavedUser() {
        val d = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        SetPointDb.Schema.create(d)
        driver = d
        val ds = SqlDelightLocalDataSource(d)
        val user = User.create("u1", "trainer@test.com", "João", Role.TRAINER).getOrThrow()
        ds.saveUser(user)
        val loaded = ds.getUserById("u1")
        assertEquals(user.id, loaded?.id)
        assertEquals(user.email, loaded?.email)
        assertEquals(user.name, loaded?.name)
        assertEquals(user.role, loaded?.role)
    }

    @Test
    fun getUserById_notFound_returnsNull() {
        val d = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        SetPointDb.Schema.create(d)
        driver = d
        val ds = SqlDelightLocalDataSource(d)
        assertNull(ds.getUserById("nonexistent"))
    }

    @Test
    fun userRepository_withSqlDelightLocalDataSource_saveAndGetSucceeds() = runBlocking {
        val d = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        SetPointDb.Schema.create(d)
        driver = d
        val ds = SqlDelightLocalDataSource(d)
        val repo = UserRepositoryImpl(ds)
        val user = User.create("u2", "aluno@test.com", "Maria", Role.STUDENT).getOrThrow()
        repo.save(user)
        val loaded = repo.getById("u2")
        assertEquals(user, loaded)
        assertTrue(repo.list().any { it.id == "u2" })
    }
}
