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
    primary = PrimaryNavyLight,
    onPrimary = Color.White,
    primaryContainer = PrimaryNavy,
    onPrimaryContainer = Color(0xFFD6E4FF),
    secondary = AmberAccentLight,
    onSecondary = Color.Black,
    secondaryContainer = AmberAccent,
    onSecondaryContainer = Color(0xFFFEF3C7),
    tertiary = ReviewPurpleLight,
    onTertiary = Color.White,
    tertiaryContainer = ReviewPurple,
    onTertiaryContainer = Color(0xFFEDE9FE),
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = BorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryNavy,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0ECFD),
    onPrimaryContainer = Color(0xFF0A2540),
    secondary = AmberAccent,
    onSecondary = Color.White,
    secondaryContainer = AmberAccentBg,
    onSecondaryContainer = Color(0xFF78350F),
    tertiary = ReviewPurple,
    onTertiary = Color.White,
    tertiaryContainer = ReviewPurpleBg,
    onTertiaryContainer = Color(0xFF4C1D95),
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondary,
    outline = BorderLight
)

@Composable
fun LibrarianTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent branding colors
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

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    LibrarianTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
