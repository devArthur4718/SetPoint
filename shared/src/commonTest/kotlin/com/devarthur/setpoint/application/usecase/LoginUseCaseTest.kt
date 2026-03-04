package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.application.auth.PasswordHasher
import com.devarthur.setpoint.application.auth.Sha256PasswordHasher
import com.devarthur.setpoint.data.local.InMemoryLocalDataSource
import com.devarthur.setpoint.data.repository.UserRepositoryImpl
import com.devarthur.setpoint.domain.Role
import com.devarthur.setpoint.domain.User
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LoginUseCaseTest {

    private val local = InMemoryLocalDataSource()
    private val userRepo = UserRepositoryImpl(local)
    private val passwordHasher: PasswordHasher = Sha256PasswordHasher()

    private suspend fun seedTrainer() {
        val u = User.create("trainer-1", "prof@setpoint.com", "Professor", Role.TRAINER).getOrThrow()
        userRepo.save(u).getOrThrow()
        local.setPasswordHash("trainer-1", passwordHasher.hash("123456"))
    }

    private suspend fun seedStudent() {
        val u = User.create("student-1", "aluno@setpoint.com", "Aluno", Role.STUDENT).getOrThrow()
        userRepo.save(u).getOrThrow()
        local.setPasswordHash("student-1", passwordHasher.hash("senha123"))
    }

    @Test
    fun invoke_validProfessorCredentials_returnsSuccess() = runBlocking {
        seedTrainer(); val uc = LoginUseCase(userRepo, local, passwordHasher)

        val result = uc("prof@setpoint.com", "123456", Role.TRAINER)

        assertTrue(result.isSuccess)
        val login = result.getOrThrow()
        assertEquals("trainer-1", login.userId)
        assertEquals(Role.TRAINER, login.role)
    }

    @Test
    fun invoke_validStudentCredentials_returnsSuccess() = runBlocking {
        seedStudent(); val uc = LoginUseCase(userRepo, local, passwordHasher)

        val result = uc("aluno@setpoint.com", "senha123", Role.STUDENT)

        assertTrue(result.isSuccess)
        val login = result.getOrThrow()
        assertEquals("student-1", login.userId)
        assertEquals(Role.STUDENT, login.role)
    }

    @Test
    fun invoke_userNotFound_returnsFailureWithGenericMessage() = runBlocking {
        val uc = LoginUseCase(userRepo, local, passwordHasher)

        val result = uc("naoexiste@setpoint.com", "qualquersenha", Role.TRAINER)

        assertFalse(result.isSuccess)
        assertEquals(LoginUseCase.INVALID_CREDENTIALS_MESSAGE, result.exceptionOrNull()?.message)
    }

    @Test
    fun invoke_wrongRole_returnsFailureWithGenericMessage() = runBlocking {
        seedTrainer(); val uc = LoginUseCase(userRepo, local, passwordHasher)

        val result = uc("prof@setpoint.com", "123456", Role.STUDENT)

        assertFalse(result.isSuccess)
        assertEquals(LoginUseCase.INVALID_CREDENTIALS_MESSAGE, result.exceptionOrNull()?.message)
    }

    @Test
    fun invoke_wrongPassword_returnsFailureWithGenericMessage() = runBlocking {
        seedTrainer(); val uc = LoginUseCase(userRepo, local, passwordHasher)

        val result = uc("prof@setpoint.com", "senhaerrada", Role.TRAINER)

        assertFalse(result.isSuccess)
        assertEquals(LoginUseCase.INVALID_CREDENTIALS_MESSAGE, result.exceptionOrNull()?.message)
    }

    @Test
    fun invoke_userWithoutStoredHash_returnsFailure() = runBlocking {
        val u = User.create("no-hash", "nohash@x.com", "No Hash", Role.TRAINER).getOrThrow()
        userRepo.save(u).getOrThrow(); val uc = LoginUseCase(userRepo, local, passwordHasher)

        val result = uc("nohash@x.com", "qualquer", Role.TRAINER)

        assertFalse(result.isSuccess)
        assertEquals(LoginUseCase.INVALID_CREDENTIALS_MESSAGE, result.exceptionOrNull()?.message)
    }
}
