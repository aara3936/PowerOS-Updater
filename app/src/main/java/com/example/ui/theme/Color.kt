package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Deep Night-Field Canvas
val NightFieldBackground = Color(0xFF0A0D14)
val NightFieldSurface = Color(0xFF101522)

// Liquid Glass Design Tokens
val LiquidGlassFill = Color(0x1AFFFFFF) // 10% alpha white frosted canvas
val LiquidGlassFillElevated = Color(0x28FFFFFF) // 16% alpha white elevated canvas
val LiquidGlassFillDark = Color(0x33000000)

// Refraction Stroke Gradients
val LiquidGlassStrokeTop = Color(0x50FFFFFF) // Top-left highlight
val LiquidGlassStrokeBottom = Color(0x0DFFFFFF) // Bottom-right shadow
val LiquidGlassErrorGlow = Color(0xFFFF5252) // Security lockout / error red glow

// Refraction Stroke Gradient Brush
val LiquidGlassBorderBrush = Brush.linearGradient(
    colors = listOf(LiquidGlassStrokeTop, LiquidGlassStrokeBottom)
)

val LiquidGlassErrorBorderBrush = Brush.linearGradient(
    colors = listOf(LiquidGlassErrorGlow, Color(0x40FF5252))
)

// Typography & Accents
val LiquidGlassTextPrimary = Color(0xFFFFFFFF)
val LiquidGlassTextSecondary = Color(0xCCFFFFFF) // 80% opacity
val LiquidGlassTextMuted = Color(0x80FFFFFF) // 50% opacity

val GlassCyanAccent = Color(0xFF00E5FF)
val GlassCyanDark = Color(0xFF00B0FF)
val GlassEmerald = Color(0xFF00E676)
val GlassAmber = Color(0xFFFFAB00)
val GlassRose = Color(0xFFFF4081)

val PowerPrimary = GlassCyanAccent
val PowerOnPrimary = Color(0xFF002533)
val PowerSurface = NightFieldSurface
