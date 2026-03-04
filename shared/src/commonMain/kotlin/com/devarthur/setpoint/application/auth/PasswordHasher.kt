package com.devarthur.setpoint.application.auth

/**
 * Hashing de senha para autenticação (MVP local).
 * Não armazena senha em texto plano; usa SHA-256 para comparação.
 */
interface PasswordHasher {
    /** Gera hash hexadecimal da senha (para persistência). */
    fun hash(plainPassword: String): String
    /** Verifica se a senha em texto plano corresponde ao hash armazenado. */
    fun verify(plainPassword: String, storedHash: String): Boolean
}
