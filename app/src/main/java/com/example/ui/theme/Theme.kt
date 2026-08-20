package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ChemCyan,
    onPrimary = Color(0xFF04101A),
    primaryContainer = Color(0xFF003845),
    onPrimaryContainer = ChemCyan,
    secondary = ChemOrange,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF4A1F0D),
    onSecondaryContainer = Color(0xFFFFD4C2),
    tertiary = ChemPurple,
    onTertiary = Color.White,
    background = ChemBackgroundDark,
    onBackground = ChemTextPrimaryDark,
    surface = ChemSurfaceDark,
    onSurface = ChemTextPrimaryDark,
    surfaceVariant = ChemCardDark,
    onSurfaceVariant = ChemTextSecondaryDark,
    outline = ChemBorderDark,
    error = ChemRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0891B2),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCFFAFE),
    onPrimaryContainer = Color(0xFF164E63),
    secondary = ChemOrange,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFEDD5),
    onSecondaryContainer = Color(0xFF7C2D12),
    tertiary = ChemPurple,
    onTertiary = Color.White,
    background = ChemBackgroundLight,
    onBackground = ChemTextPrimaryLight,
    surface = ChemSurfaceLight,
    onSurface = ChemTextPrimaryLight,
    surfaceVariant = ChemCardLight,
    onSurfaceVariant = ChemTextSecondaryLight,
    outline = ChemBorderLight,
    error = ChemRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
