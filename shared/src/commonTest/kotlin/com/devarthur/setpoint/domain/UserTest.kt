package com.devarthur.setpoint.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserTest {

    @Test
    fun createUser_withValidData_roleTrainer_returnsSuccess() {
        val result = User.create(
            id = "u1",
            email = "trainer@example.com",
            name = "João Silva",
            role = Role.TRAINER,
        )
        assertTrue(result.isSuccess)
        val user = result.getOrThrow()
        assertEquals("u1", user.id)
        assertEquals("trainer@example.com", user.email)
        assertEquals("João Silva", user.name)
        assertEquals(Role.TRAINER, user.role)
    }

    @Test
    fun createUser_withValidData_roleStudent_returnsSuccess() {
        val result = User.create(
            id = "u2",
            email = "aluno@example.com",
            name = "Maria Santos",
            role = Role.STUDENT,
        )
        assertTrue(result.isSuccess)
        val user = result.getOrThrow()
        assertEquals("u2", user.id)
        assertEquals("aluno@example.com", user.email)
        assertEquals("Maria Santos", user.name)
        assertEquals(Role.STUDENT, user.role)
    }

    @Test
    fun createUser_emptyEmail_returnsFailure() {
        val result = User.create(
            id = "u1",
            email = "",
            name = "João",
            role = Role.TRAINER,
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertTrue(
            result.exceptionOrNull()!!.message!!.contains("vazio"),
            result.exceptionOrNull()!!.message!!
        )
    }

    @Test
    fun createUser_blankEmail_returnsFailure() {
        val result = User.create(
            id = "u1",
            email = "   ",
            name = "João",
            role = Role.TRAINER,
        )
        assertTrue(result.isFailure)
    }

    @Test
    fun createUser_invalidEmailFormat_returnsFailure() {
        val result = User.create(
            id = "u1",
            email = "nao-e-email",
            name = "João",
            role = Role.TRAINER,
        )
        assertTrue(result.isFailure)
        assertTrue(
            result.exceptionOrNull()!!.message!!.contains("formato"),
            result.exceptionOrNull()!!.message!!
        )
    }

    @Test
    fun createUser_emptyName_returnsFailure() {
        val result = User.create(
            id = "u1",
            email = "a@b.com",
            name = "",
            role = Role.TRAINER,
        )
        assertTrue(result.isFailure)
        assertTrue(
            result.exceptionOrNull()!!.message!!.contains("Nome"),
            result.exceptionOrNull()!!.message!!
        )
    }

    @Test
    fun createUser_blankName_returnsFailure() {
        val result = User.create(
            id = "u1",
            email = "a@b.com",
            name = "   \t  ",
            role = Role.TRAINER,
        )
        assertTrue(result.isFailure)
    }

    @Test
    fun createUser_nameOver120Characters_returnsFailure() {
        val longName = "a".repeat(121)
        val result = User.create(
            id = "u1",
            email = "a@b.com",
            name = longName,
            role = Role.TRAINER,
        )
        assertTrue(result.isFailure)
        assertTrue(
            result.exceptionOrNull()!!.message!!.contains("120"),
            result.exceptionOrNull()!!.message!!
        )
    }

    @Test
    fun createUser_nameExactly120Characters_returnsSuccess() {
        val name = "a".repeat(120)
        val result = User.create(
            id = "u1",
            email = "a@b.com",
            name = name,
            role = Role.TRAINER,
        )
        assertTrue(result.isSuccess)
        assertEquals(120, result.getOrThrow().name.length)
    }
}
