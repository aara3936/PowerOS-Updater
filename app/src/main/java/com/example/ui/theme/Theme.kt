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

private val NaturalLightColorScheme =
  lightColorScheme(
    primary = GlassPrimary,
    onPrimary = Color.White,
    primaryContainer = GlassPrimaryLight,
    onPrimaryContainer = GlassPrimaryDark,
    secondary = GlassSecondary,
    onSecondary = Color.White,
    secondaryContainer = GlassSecondaryLight,
    onSecondaryContainer = Color(0xFF3730A3),
    tertiary = GlassEmerald,
    onTertiary = Color.White,
    tertiaryContainer = GlassEmeraldLight,
    onTertiaryContainer = Color(0xFF065F46),
    background = NaturalLightBackground,
    onBackground = NaturalLightTextPrimary,
    surface = NaturalLightSurface,
    onSurface = NaturalLightTextPrimary,
    surfaceVariant = NaturalLightSurfaceTranslucent,
    onSurfaceVariant = NaturalLightTextSecondary,
    outline = Color(0xFFE2E8F0),
    outlineVariant = Color(0xFFF1F5F9),
    error = GlassRose,
    onError = Color.White
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false, // Pure Natural Light and Liquid Glass theme
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        dynamicLightColorScheme(context)
      }
      else -> NaturalLightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

