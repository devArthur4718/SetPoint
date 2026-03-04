package com.devarthur.setpoint.ui.motion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember

/**
 * Quando true, animações devem ser desativadas ou reduzidas (preferência de acessibilidade).
 * Consumir em composables que animam para respeitar "reduzir movimento".
 */
val LocalReduceMotion = compositionLocalOf { false }

/**
 * Retorna se a preferência "reduzir movimento" está ativa.
 * Em commonMain retorna false; plataformas (Android/iOS) podem sobrescrever via expect/actual
 * lendo a configuração do sistema.
 */
@Composable
fun reduceMotionEnabled(): Boolean = remember { false }
