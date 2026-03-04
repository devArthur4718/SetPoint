package com.devarthur.setpoint.application.usecase

import com.devarthur.setpoint.application.auth.PasswordHasher
import com.devarthur.setpoint.application.auth.Sha256PasswordHasher
import com.devarthur.setpoint.data.local.InMemoryLocalDataSource
import com.devarthur.setpoint.data.repository.StudentProfileRepositoryImpl
import com.devarthur.setpoint.data.repository.UserRepositoryImpl
import com.devarthur.setpoint.domain.Role
import com.devarthur.setpoint.domain.User
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CreateAccountUseCaseTest {

    private val local = InMemoryLocalDataSource()
    private val userRepo = UserRepositoryImpl(local)
    private val profileRepo = StudentProfileRepositoryImpl(local)
    private val passwordHasher: PasswordHasher = Sha256PasswordHasher()

    private fun fixedIdGenerator(vararg ids: String) = object : IdGenerator {
        private var i = 0
        override fun generate(): String = ids[i++]
    }

    private fun createUseCase(idGen: IdGenerator) = CreateAccountUseCase(
        userRepository = userRepo,
        studentProfileRepository = profileRepo,
        localDataSource = local,
        passwordHasher = passwordHasher,
        idGenerator = idGen,
    )

    @Test
    fun execute_validTrainer_returnsSuccessAndPersistsUserAndCredential() = runBlocking {
        val idGen = fixedIdGenerator("u-trainer-1")
        val uc = createUseCase(idGen)

        val result = uc.execute(
            email = "trainer@setpoint.com",
            password = "123456",
            name = "Carlos Professor",
            role = Role.TRAINER,
        )

        assertTrue(result.isSuccess)
        val created = result.getOrThrow()
        assertEquals("u-trainer-1", created.userId)
        assertEquals("trainer@setpoint.com", created.email)
        assertEquals("Carlos Professor", created.name)
        assertEquals(Role.TRAINER, created.role)

        assertEquals(created.userId, userRepo.getById(created.userId)?.id)
        val hash = local.getPasswordHash(created.userId)
        assertTrue(hash != null && passwordHasher.verify("123456", hash))
        assertNull(profileRepo.getByUserId(created.userId))
    }

    @Test
    fun execute_validStudent_returnsSuccessAndPersistsUserProfileAndCredential() = runBlocking {
        val idGen = fixedIdGenerator("u-student-1", "sp-1")
        val uc = createUseCase(idGen)

        val result = uc.execute(
            email = "aluno@setpoint.com",
            password = "senha123",
            name = "Ana Aluna",
            role = Role.STUDENT,
        )

        assertTrue(result.isSuccess)
        val created = result.getOrThrow()
        assertEquals("u-student-1", created.userId)
        assertEquals("aluno@setpoint.com", created.email)
        assertEquals("Ana Aluna", created.name)
        assertEquals(Role.STUDENT, created.role)

        assertEquals(created.userId, userRepo.getById(created.userId)?.id)
        val profile = profileRepo.getByUserId(created.userId)
        assertTrue(profile != null && profile.userId == created.userId && profile.displayName == null)
        val hash = local.getPasswordHash(created.userId)
        assertTrue(hash != null && passwordHasher.verify("senha123", hash))
    }

    @Test
    fun execute_validStudentWithDisplayName_returnsSuccessAndPersistsDisplayName() = runBlocking {
        val idGen = fixedIdGenerator("u-student-2", "sp-2")
        val uc = createUseCase(idGen)

        val result = uc.execute(
            email = "aluno2@setpoint.com",
            password = "minhasenha",
            name = "Bruno Silva",
            role = Role.STUDENT,
            displayName = "Bruno",
        )

        assertTrue(result.isSuccess)
        val created = result.getOrThrow()
        assertEquals(Role.STUDENT, created.role)
        val profile = profileRepo.getByUserId(created.userId)
        assertTrue(profile != null && profile.displayName == "Bruno")
    }

    @Test
    fun execute_invalidEmail_returnsFailureAndDoesNotPersist() = runBlocking {
        val idGen = fixedIdGenerator("u1")
        val uc = createUseCase(idGen)

        val result = uc.execute(
            email = "invalid-email",
            password = "123456",
            name = "João",
            role = Role.TRAINER,
        )

        assertTrue(result.isFailure)
        assertNull(userRepo.getById("u1"))
        assertNull(local.getPasswordHash("u1"))
    }

    @Test
    fun execute_passwordTooShort_returnsFailureAndDoesNotPersist() = runBlocking {
        val idGen = fixedIdGenerator("u1")
        val uc = createUseCase(idGen)

        val result = uc.execute(
            email = "ok@test.com",
            password = "12345",
            name = "João",
            role = Role.TRAINER,
        )

        assertTrue(result.isFailure)
        result.exceptionOrNull()?.message?.let { msg ->
            assertTrue(msg.contains("6") || msg.contains("mínimo"))
        }
        assertNull(userRepo.getById("u1"))
    }

    @Test
    fun execute_duplicateEmail_returnsFailureWithMessageAndDoesNotOverwrite() = runBlocking {
        val existing = User.create("u-existing", "existing@test.com", "Existing", Role.TRAINER).getOrThrow()
        userRepo.save(existing)
        local.setPasswordHash("u-existing", passwordHasher.hash("oldpass"))

        val idGen = fixedIdGenerator("u-new", "sp-new")
        val uc = createUseCase(idGen)

        val result = uc.execute(
            email = "existing@test.com",
            password = "123456",
            name = "New User",
            role = Role.STUDENT,
            displayName = "New",
        )

        assertTrue(result.isFailure)
        assertEquals("E-mail já cadastrado", result.exceptionOrNull()?.message)
        assertNull(userRepo.getById("u-new"))
        assertNull(profileRepo.getByUserId("u-new"))
        assertEquals(existing, userRepo.getById("u-existing"))
    }

    @Test
    fun execute_emptyName_returnsFailureAndDoesNotPersist() = runBlocking {
        val idGen = fixedIdGenerator("u1")
        val uc = createUseCase(idGen)

        val result = uc.execute(
            email = "a@b.com",
            password = "123456",
            name = "",
            role = Role.TRAINER,
        )

        assertTrue(result.isFailure)
        assertNull(userRepo.getById("u1"))
    }
}
