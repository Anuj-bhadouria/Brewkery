package com.AB.brewkery.UI.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val Espresso = Color(0xFF4E342E)
private val Caramel = Color(0xFFC8863B)
private val Cream = Color(0xFFFFF8F0)
private val Latte = Color(0xFFF1E4D3)
private val Ink = Color(0xFF2B1B17)

private val BrewkeryColors = lightColorScheme(
    primary = Espresso,
    onPrimary = Color.White,
    primaryContainer = Latte,
    onPrimaryContainer = Espresso,
    secondary = Caramel,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE0B2),
    onSecondaryContainer = Espresso,
    background = Cream,
    onBackground = Ink,
    surface = Cream,
    onSurface = Ink,
    surfaceContainerHighest = Color.White   // card background
)

private val BrewkeryShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun BrewkeryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BrewkeryColors,
        shapes = BrewkeryShapes,
        content = content
    )
}