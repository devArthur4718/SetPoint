package com.devarthur.setpoint.data.repository

import com.devarthur.setpoint.data.local.InMemoryLocalDataSource
import com.devarthur.setpoint.domain.Exercise
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ExerciseRepositoryTest {

    private val local = InMemoryLocalDataSource()
    private val repo = ExerciseRepositoryImpl(local)

    @Test
    fun save_and_getById_returnsExercise() = runBlocking {
        val ex = Exercise.create("e1", "Supino reto").getOrThrow()
        repo.save(ex)
        assertEquals(ex, repo.getById("e1"))
    }

    @Test
    fun save_and_list_returnsAll() = runBlocking {
        val e1 = Exercise.create("e1", "Supino").getOrThrow()
        val e2 = Exercise.create("e2", "Agachamento", "Pernas").getOrThrow()
        repo.save(e1)
        repo.save(e2)
        val list = repo.list()
        assertEquals(2, list.size)
        assertTrue(list.any { it.id == "e1" })
        assertTrue(list.any { it.id == "e2" })
    }

    @Test
    fun getById_notFound_returnsNull() = runBlocking {
        assertNull(repo.getById("nonexistent"))
    }
}
