package com.example.projectforwebdevelopmentmasagoesmuhammadsounika.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF1C1B1E),
    surface = Color(0xFF25232A),
    surfaceVariant = Color(0xFF36343B),
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5),
    onSurfaceVariant = Color(0xFFCAC4D0)
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFFEF7FF),
    surface = Color(0xFFF7F2FA),
    surfaceVariant = Color(0xFFE7E0EC),
    onBackground = Color(0xFF1D1B20),
    onSurface = Color(0xFF1D1B20),
    onSurfaceVariant = Color(0xFF49454F)
)

@Composable
fun ProjectForWebDevelopmentMasagoesMuhammadSounikaTheme(
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