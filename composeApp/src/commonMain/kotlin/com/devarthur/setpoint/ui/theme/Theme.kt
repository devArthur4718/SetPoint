package com.devarthur.setpoint.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// SetPoint — mais colorido: primary teal vibrante, secondary laranja/energia, tertiary violeta
private val PrimaryLight = Color(0xFF00897B)      // teal vibrante (Material Teal 600)
private val OnPrimaryLight = Color(0xFFFFFFFF)
private val PrimaryContainerLight = Color(0xFF80CBC4)   // teal claro
private val OnPrimaryContainerLight = Color(0xFF004D40)
private val SecondaryLight = Color(0xFFE65100)   // laranja forte (energia, ação)
private val OnSecondaryLight = Color(0xFFFFFFFF)
private val TertiaryLight = Color(0xFF5C6BC0)     // índigo/violeta (destaque)
private val OnTertiaryLight = Color(0xFFFFFFFF)
private val ErrorLight = Color(0xFFC62828)
private val OnErrorLight = Color(0xFFFFFFFF)
private val SurfaceLight = Color(0xFFE8F5F5)     // fundo com leve tom teal
private val OnSurfaceLight = Color(0xFF1B2E2C)
private val OnSurfaceVariantLight = Color(0xFF3D5A57)
private val OutlineLight = Color(0xFF6B8784)
private val SurfaceContainerLowLight = Color(0xFFB2DFDB)  // cards com cor (teal suave)
private val SurfaceContainerHighLight = Color(0xFFE0F2F1)

private val PrimaryDark = Color(0xFF4DB6AC)      // teal claro vibrante
private val OnPrimaryDark = Color(0xFF003730)
private val PrimaryContainerDark = Color(0xFF005B50)
private val OnPrimaryContainerDark = Color(0xFF80CBC4)
private val SecondaryDark = Color(0xFFFF8A65)    // laranja claro
private val OnSecondaryDark = Color(0xFF5D2100)
private val TertiaryDark = Color(0xFF8C9EFF)     // violeta claro
private val OnTertiaryDark = Color(0xFF1A237E)
private val ErrorDark = Color(0xFFFF8A80)
private val OnErrorDark = Color(0xFF690005)
private val SurfaceDark = Color(0xFF0F1E1C)
private val OnSurfaceDark = Color(0xFFE0E7E6)
private val OnSurfaceVariantDark = Color(0xFFB8CCC9)
private val OutlineDark = Color(0xFF7A928E)
private val SurfaceContainerLowDark = Color(0xFF1E3D39)  // cards com tom teal
private val SurfaceContainerHighDark = Color(0xFF2A4A45)

private val SetPointLightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    error = ErrorLight,
    onError = OnErrorLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    surfaceContainerLow = SurfaceContainerLowLight,
    surfaceContainerHigh = SurfaceContainerHighLight,
)

private val SetPointDarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    error = ErrorDark,
    onError = OnErrorDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    surfaceContainerLow = SurfaceContainerLowDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
)

@Composable
fun SetPointTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) SetPointDarkColorScheme else SetPointLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
