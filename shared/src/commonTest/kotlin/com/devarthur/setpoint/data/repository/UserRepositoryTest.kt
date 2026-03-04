package com.devarthur.setpoint.data.repository

import com.devarthur.setpoint.data.local.InMemoryLocalDataSource
import com.devarthur.setpoint.domain.Role
import com.devarthur.setpoint.domain.User
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UserRepositoryTest {

    private val local = InMemoryLocalDataSource()
    private val repo = UserRepositoryImpl(local)

    @Test
    fun save_and_getById_returnsUser() = runBlocking {
        val user = User.create("u1", "a@b.com", "João", Role.TRAINER).getOrThrow()
        repo.save(user)
        assertEquals(user, repo.getById("u1"))
    }

    @Test
    fun save_and_getByEmail_returnsUser() = runBlocking {
        val user = User.create("u2", "trainer@setpoint.com", "Maria", Role.TRAINER).getOrThrow()
        repo.save(user)
        assertEquals(user, repo.getByEmail("trainer@setpoint.com"))
    }

    @Test
    fun list_returnsAllUsers() = runBlocking {
        val u1 = User.create("u1", "a@b.com", "A", Role.TRAINER).getOrThrow()
        val u2 = User.create("u2", "b@b.com", "B", Role.STUDENT).getOrThrow()
        repo.save(u1)
        repo.save(u2)
        val list = repo.list()
        assertEquals(2, list.size)
        assertTrue(list.any { it.id == "u1" })
        assertTrue(list.any { it.id == "u2" })
    }

    @Test
    fun save_duplicateEmail_fails() = runBlocking {
        val u1 = User.create("u1", "same@mail.com", "First", Role.TRAINER).getOrThrow()
        val u2 = User.create("u2", "same@mail.com", "Second", Role.STUDENT).getOrThrow()
        repo.save(u1)
        val result = repo.save(u2)
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("email"))
    }

    @Test
    fun save_sameUser_updateSucceeds() = runBlocking {
        val user = User.create("u1", "a@b.com", "João", Role.TRAINER).getOrThrow()
        repo.save(user)
        val result = repo.save(user)
        assertTrue(result.isSuccess)
        assertEquals(user, repo.getById("u1"))
    }

    @Test
    fun getById_notFound_returnsNull() = runBlocking {
        assertNull(repo.getById("nonexistent"))
    }
}
