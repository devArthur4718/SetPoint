package com.devarthur.setpoint.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme

/**
 * Símbolo da marca SetPoint: anel + ponto central (spec-19).
 * Usa [MaterialTheme.colorScheme.primary]; escalável por [modifier] ou [size].
 */
@Composable
fun SetPointLogo(
    modifier: Modifier = Modifier,
    size: Dp = 96.dp,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Canvas(modifier = modifier.size(size)) {
        val side = size.toPx()
        val centerX = side / 2f
        val centerY = side / 2f
        // Viewport 24x24 → escala para side
        val scale = side / 24f
        // Anel: raio 10, stroke 2 (viewport)
        val ringRadius = 10f * scale
        val strokeWidth = 2f * scale
        // Ponto: raio 3 (viewport)
        val dotRadius = 3f * scale

        // Anel externo (só traço)
        drawCircle(
            color = color,
            radius = ringRadius,
            center = Offset(centerX, centerY),
            style = Stroke(width = strokeWidth),
        )
        // Ponto central
        drawCircle(
            color = color,
            radius = dotRadius,
            center = Offset(centerX, centerY),
        )
    }
}
