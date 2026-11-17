package com.sebas.destinoscolombia.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppColors = lightColorScheme(
    primary = Color(0xFF2563EB),
    secondary = Color(0xFF0F766E),
    background = Color(0xFFF4F6FB),
    surface = Color.White,
    onPrimary = Color.White,
    onSurface = Color(0xFF14213D)
)

@Composable
fun DestinosColombiaTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = AppColors, content = content)
}
