package com.example.projectforwebdevelopmentmasagoesmuhammadsounika.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GreenColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    primaryContainer = GreenPrimaryContainer,
    onPrimaryContainer = GreenOnPrimaryContainer,
    secondary = GreenSecondary,
    onSecondary = Color.White,
    secondaryContainer = GreenSecondaryContainer,
    background = GreenBackground,
    onBackground = GreenOnSurfaceVariant,
    surface = GreenSurface,
    onSurface = GreenOnSurfaceVariant,
    surfaceVariant = GreenSurfaceVariant,
    onSurfaceVariant = GreenOnSurfaceVariant
)

@Composable
fun ProjectForWebDevelopmentMasagoesMuhammadSounikaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GreenColorScheme,
        typography = Typography,
        content = content
    )
}
