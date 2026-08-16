package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldSecondary,
    onPrimary = Color(0xFF003915),
    primaryContainer = Color(0xFF166534),
    onPrimaryContainer = Color(0xFF86EFAC),
    secondary = EmeraldAccent,
    onSecondary = Color(0xFF003822),
    secondaryContainer = EmeraldDarkCardSecondary,
    onSecondaryContainer = Color(0xFFA7F3D0),
    tertiary = GoldAccent,
    onTertiary = Color(0xFF452B00),
    background = EmeraldDarkBackground,
    onBackground = Color(0xFFE2FCEB),
    surface = EmeraldDarkCard,
    onSurface = Color(0xFFE2FCEB),
    surfaceVariant = EmeraldDarkCardSecondary,
    onSurfaceVariant = Color(0xFF86EFAC),
    outline = EmeraldDarkBorder,
    outlineVariant = Color(0xFF1B3D2B),
    error = RedAccent
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color.White,
    primaryContainer = EmeraldLightCardSecondary,
    onPrimaryContainer = Color(0xFF14532D),
    secondary = EmeraldSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE6F9EE),
    onSecondaryContainer = Color(0xFF052E16),
    tertiary = GoldAccent,
    onTertiary = Color.White,
    background = EmeraldLightBackground,
    onBackground = Color(0xFF052E16),
    surface = EmeraldLightCard,
    onSurface = Color(0xFF092314),
    surfaceVariant = Color(0xFFE8F5E9),
    onSurfaceVariant = Color(0xFF1E4620),
    outline = EmeraldLightBorder,
    outlineVariant = Color(0xFFD1FAE5),
    error = RedAccent
)

@Composable
fun TakaEarnTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
