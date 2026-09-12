package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Deep Night-Field Canvas (Dark Mode)
val NightFieldBackground = Color(0xFF030712)
val NightFieldSurface = Color(0xFF0F172A)

// Daylight Pearl Canvas (Light Mode)
val DayFieldBackground = Color(0xFFF1F5F9)
val DayFieldSurface = Color(0xFFE2E8F0)

// Liquid Glass Design Tokens (Dark Mode: #121827 with 65% opacity)
val LiquidGlassFill = Color(0xA6121827) // 65% frosted dark glass
val LiquidGlassFillElevated = Color(0xD9121827) // 85% elevated glass
val LiquidGlassFillDark = Color(0xCC000000) // 80% dim backdrop

// Liquid Glass Design Tokens (Light Mode: #F8FAFC with 75% opacity)
val LiquidGlassFillLight = Color(0xBFF8FAFC) // 75% frosted pearl glass
val LiquidGlassFillElevatedLight = Color(0xE6F8FAFC) // 90% elevated glass
val LiquidGlassBackdropLight = Color(0x99000000) // 60% dim backdrop

// Refraction Stroke Gradients
val LiquidGlassStrokeTop = Color(0x26FFFFFF) // 15% white frosted border stroke
val LiquidGlassStrokeBottom = Color(0x10FFFFFF) // 6% white soft highlight

val LiquidGlassStrokeTopLight = Color(0x1A000000) // 10% black frosted border
val LiquidGlassStrokeBottomLight = Color(0x0D000000) // 5% black soft highlight

val LiquidGlassErrorGlow = Color(0xFFFF5252)

val LiquidGlassBorderBrush = Brush.linearGradient(
    colors = listOf(LiquidGlassStrokeTop, LiquidGlassStrokeBottom)
)

val LiquidGlassErrorBorderBrush = Brush.linearGradient(
    colors = listOf(LiquidGlassErrorGlow, Color(0x40FF5252))
)

// Typography & Accents (Dark)
val LiquidGlassTextPrimary = Color(0xFFFFFFFF)
val LiquidGlassTextSecondary = Color(0xCCFFFFFF) // 80% opacity
val LiquidGlassTextMuted = Color(0x80FFFFFF) // 50% opacity

// Typography & Accents (Light)
val LiquidGlassTextPrimaryLight = Color(0xFF0F172A)
val LiquidGlassTextSecondaryLight = Color(0xCC0F172A)
val LiquidGlassTextMutedLight = Color(0x800F172A)

val GlassCyanAccent = Color(0xFF00E5FF)
val GlassCyanDark = Color(0xFF00B0FF)
val GlassEmerald = Color(0xFF00E676)
val GlassAmber = Color(0xFFFFAB00)
val GlassRose = Color(0xFFFF4081)

val PowerPrimary = GlassCyanAccent
val PowerOnPrimary = Color(0xFF002533)
