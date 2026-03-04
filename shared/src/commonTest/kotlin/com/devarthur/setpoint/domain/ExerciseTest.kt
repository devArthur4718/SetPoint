package com.devarthur.setpoint.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExerciseTest {

    @Test
    fun create_withValidName_returnsSuccess() {
        val result = Exercise.create(id = "e1", name = "Supino reto")
        assertTrue(result.isSuccess)
        val ex = result.getOrThrow()
        assertEquals("e1", ex.id)
        assertEquals("Supino reto", ex.name)
        assertEquals(null, ex.description)
    }

    @Test
    fun create_withValidNameAndDescription_returnsSuccess() {
        val result = Exercise.create(
            id = "e2",
            name = "Agachamento",
            description = "Pernas na linha dos ombros",
        )
        assertTrue(result.isSuccess)
        val ex = result.getOrThrow()
        assertEquals("e2", ex.id)
        assertEquals("Agachamento", ex.name)
        assertEquals("Pernas na linha dos ombros", ex.description)
    }

    @Test
    fun create_emptyName_returnsFailure() {
        val result = Exercise.create(id = "e1", name = "")
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("Nome"))
    }

    @Test
    fun create_blankName_returnsFailure() {
        val result = Exercise.create(id = "e1", name = "   \t  ")
        assertTrue(result.isFailure)
    }

    @Test
    fun create_nameOver120Characters_returnsFailure() {
        val result = Exercise.create(id = "e1", name = "a".repeat(121))
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("120"))
    }

    @Test
    fun create_nameExactly120Characters_returnsSuccess() {
        val name = "a".repeat(120)
        val result = Exercise.create(id = "e1", name = name)
        assertTrue(result.isSuccess)
        assertEquals(120, result.getOrThrow().name.length)
    }
}
