package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NovyraGoldAccent,
    onPrimary = NovyraNavyDark,
    primaryContainer = NovyraNavyPrimary,
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF93C5FD),
    onSecondary = NovyraNavyDark,
    tertiary = NovyraVerifiedGreen,
    background = DarkCanvas,
    onBackground = DarkTextPrimary,
    surface = DarkSurfaceCard,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    error = NovyraBreakingRed
)

private val LightColorScheme = lightColorScheme(
    primary = NovyraNavyPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8EEF8),
    onPrimaryContainer = NovyraNavyDark,
    secondary = NovyraGoldAccent,
    onSecondary = NovyraNavyDark,
    tertiary = NovyraVerifiedGreen,
    background = EditorialOffWhite,
    onBackground = EditorialTextPrimary,
    surface = EditorialCardLight,
    onSurface = EditorialTextPrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = EditorialTextSecondary,
    outline = EditorialBorder,
    error = NovyraBreakingRed
)

@Composable
fun NovyraNewsTheme(
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
