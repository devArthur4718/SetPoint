package com.devarthur.setpoint.application.usecase

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

class CreateStudentUseCaseTest {

    private val local = InMemoryLocalDataSource()
    private val userRepo = UserRepositoryImpl(local)
    private val profileRepo = StudentProfileRepositoryImpl(local)

    private fun fixedIdGenerator(userId: String, profileId: String) = object : IdGenerator {
        private var callCount = 0
        override fun generate(): String = if (callCount++ == 0) userId else profileId
    }

    @Test
    fun execute_withValidData_returnsSuccessAndPersists() = runBlocking {
        val idGen = fixedIdGenerator("u-new", "sp-new")
        val uc = CreateStudentUseCase(userRepo, profileRepo, idGen)

        val result = uc.execute(
            email = "aluno@test.com",
            name = "Maria Silva",
            displayName = "Maria",
            trainerId = "trainer-1",
        )

        assertTrue(result.isSuccess)
        val created = result.getOrThrow()
        assertEquals("u-new", created.user.id)
        assertEquals("aluno@test.com", created.user.email)
        assertEquals("Maria Silva", created.user.name)
        assertEquals(Role.STUDENT, created.user.role)
        assertEquals("sp-new", created.studentProfile.id)
        assertEquals("u-new", created.studentProfile.userId)
        assertEquals("Maria", created.studentProfile.displayName)

        assertEquals(created.user, userRepo.getById("u-new"))
        assertEquals(created.studentProfile, profileRepo.getByUserId("u-new"))
    }

    @Test
    fun execute_invalidEmail_returnsFailureAndDoesNotPersist() = runBlocking {
        val idGen = fixedIdGenerator("u1", "sp1")
        val uc = CreateStudentUseCase(userRepo, profileRepo, idGen)

        val result = uc.execute(
            email = "not-an-email",
            name = "João",
            trainerId = "t1",
        )

        assertTrue(result.isFailure)
        assertNull(userRepo.getById("u1"))
        assertNull(profileRepo.getByUserId("u1"))
    }

    @Test
    fun execute_emptyName_returnsFailureAndDoesNotPersist() = runBlocking {
        val idGen = fixedIdGenerator("u2", "sp2")
        val uc = CreateStudentUseCase(userRepo, profileRepo, idGen)

        val result = uc.execute(
            email = "a@b.com",
            name = "",
            trainerId = "t1",
        )

        assertTrue(result.isFailure)
        assertNull(userRepo.getById("u2"))
    }

    @Test
    fun execute_duplicateEmail_returnsFailureAndDoesNotCreateProfile() = runBlocking {
        val existingUser = User.create("u-existing", "duplicate@test.com", "Existing", Role.STUDENT).getOrThrow()
        userRepo.save(existingUser)

        val idGen = fixedIdGenerator("u-new", "sp-new")
        val uc = CreateStudentUseCase(userRepo, profileRepo, idGen)

        val result = uc.execute(
            email = "duplicate@test.com",
            name = "Another",
            trainerId = "t1",
        )

        assertTrue(result.isFailure)
        assertNull(profileRepo.getByUserId("u-new"))
        assertEquals(existingUser, userRepo.getById("u-existing"))
    }
}
