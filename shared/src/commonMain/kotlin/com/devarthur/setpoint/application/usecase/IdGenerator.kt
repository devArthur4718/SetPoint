package com.devarthur.setpoint.application.usecase

/**
 * Geração de identificadores únicos (ex.: para User e StudentProfile).
 * A implementação pode usar UUID por plataforma (expect/actual ou lib).
 */
fun interface IdGenerator {
    fun generate(): String
}
