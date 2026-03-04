package com.devarthur.setpoint.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape

// Identidade fitness: dark-first, verde-limão (destaques/progresso) e roxo claro (cards/nav)
// Tema escuro — paleta principal (referência: app de treino)
private val LimeGreen = Color(0xFFC6FF00)
private val LimeGreenDim = Color(0xFF9BCB00)
private val LightPurple = Color(0xFFCE93D8)
private val LightPurpleDim = Color(0xFFB39DDB)
private val PurpleCard = Color(0xFF7E57C2)
private val PurpleCardDim = Color(0xFF5E35B1)
private val DarkBackground = Color(0xFF0D0D0F)
private val DarkSurface = Color(0xFF121214)
private val DarkSurfaceVariant = Color(0xFF1E1E22)
private val OnDark = Color(0xFFE8E8E8)
private val OnDarkVariant = Color(0xFFB0B0B4)
private val ErrorRed = Color(0xFFCF6679)
private val OnErrorRed = Color(0xFF000000)

private val PrimaryDark = LimeGreen
private val OnPrimaryDark = Color(0xFF000000)
private val PrimaryContainerDark = LimeGreenDim
private val OnPrimaryContainerDark = Color(0xFF1A1A00)
private val SecondaryDark = LightPurple
private val OnSecondaryDark = Color(0xFF1A0A1E)
private val SecondaryContainerDark = PurpleCard
private val OnSecondaryContainerDark = Color(0xFFFFFFFF)
private val TertiaryDark = Color(0xFF81D4FA)
private val OnTertiaryDark = Color(0xFF003547)
private val TertiaryContainerDark = Color(0xFF004D65)
private val OnTertiaryContainerDark = Color(0xFFB6EAFF)
private val ErrorDark = ErrorRed
private val OnErrorDark = OnErrorRed
private val ErrorContainerDark = Color(0xFF93000A)
private val OnErrorContainerDark = Color(0xFFFFDAD6)
private val SurfaceDark = DarkSurface
private val OnSurfaceDark = OnDark
private val SurfaceVariantDark = DarkSurfaceVariant
private val OnSurfaceVariantDark = OnDarkVariant
private val OutlineDark = Color(0xFF5C5C62)
private val OutlineVariantDark = Color(0xFF3C3C42)
private val SurfaceContainerDark = Color(0xFF1A1A1E)
private val SurfaceContainerLowDark = Color(0xFF161618)
private val SurfaceContainerHighDark = Color(0xFF252528)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    background = DarkBackground,
    onBackground = OnSurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerLow = SurfaceContainerLowDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
)

// Tema claro — mesma identidade (lime + roxo) adaptada a fundo claro
private val PrimaryLight = Color(0xFF5C7C00)
private val OnPrimaryLight = Color(0xFFFFFFFF)
private val PrimaryContainerLight = Color(0xFFD4F07A)
private val OnPrimaryContainerLight = Color(0xFF1A2400)
private val SecondaryLight = Color(0xFF6B5B71)
private val OnSecondaryLight = Color(0xFFFFFFFF)
private val SecondaryContainerLight = Color(0xFFF5DAFC)
private val OnSecondaryContainerLight = Color(0xFF25192A)
private val TertiaryLight = Color(0xFF006685)
private val OnTertiaryLight = Color(0xFFFFFFFF)
private val TertiaryContainerLight = Color(0xFFB6EAFF)
private val OnTertiaryContainerLight = Color(0xFF001F29)
private val ErrorLight = Color(0xFFBA1A1A)
private val OnErrorLight = Color(0xFFFFFFFF)
private val ErrorContainerLight = Color(0xFFFFDAD6)
private val OnErrorContainerLight = Color(0xFF410002)
private val SurfaceLight = Color(0xFFF8F9FA)
private val OnSurfaceLight = Color(0xFF1A1C1E)
private val SurfaceVariantLight = Color(0xFFE8E8EC)
private val OnSurfaceVariantLight = Color(0xFF45464A)
private val OutlineLight = Color(0xFF75767A)
private val OutlineVariantLight = Color(0xFFC5C6CA)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    background = SurfaceLight,
    onBackground = OnSurfaceLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
)

// Escala tipográfica: Display, Headline, Title, Body, Label — hierarquia clara
private val SetPointTypography = Typography(
    displayLarge = TextStyle(fontSize = 57.sp, fontWeight = FontWeight.Normal, lineHeight = 64.sp),
    displayMedium = TextStyle(fontSize = 45.sp, fontWeight = FontWeight.Normal, lineHeight = 52.sp),
    displaySmall = TextStyle(fontSize = 36.sp, fontWeight = FontWeight.Normal, lineHeight = 44.sp),
    headlineLarge = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, lineHeight = 40.sp),
    headlineMedium = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Normal, lineHeight = 36.sp),
    headlineSmall = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Normal, lineHeight = 32.sp),
    titleLarge = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Medium, lineHeight = 28.sp),
    titleMedium = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium, lineHeight = 24.sp),
    titleSmall = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, lineHeight = 20.sp),
    bodyLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal, lineHeight = 20.sp),
    bodySmall = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal, lineHeight = 16.sp),
    labelLarge = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, lineHeight = 20.sp),
    labelMedium = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium, lineHeight = 16.sp),
    labelSmall = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Medium, lineHeight = 16.sp),
)

// Cantos bem arredondados (estilo fitness/moderno)
private val SetPointShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

@Composable
fun SetPointTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = SetPointTypography,
        shapes = SetPointShapes,
        content = content,
    )
}
