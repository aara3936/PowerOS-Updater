package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Deep Night-Field Canvas
val NightFieldBackground = Color(0xFF030712)
val NightFieldSurface = Color(0xFF0F172A)

// Liquid Glass Design Tokens (Section 1.2: #121827 with 65% opacity)
val LiquidGlassFill = Color(0xA6121827) // 65% frosted dark glass surface #121827
val LiquidGlassFillElevated = Color(0xD9121827) // 85% elevated glass surface
val LiquidGlassFillDark = Color(0xB3000000) // 70% dim backdrop overlay #B3000000

// Refraction Stroke Gradients (1.5dp frosted border stroke #ffffff with 15% opacity)
val LiquidGlassStrokeTop = Color(0x26FFFFFF) // 15% white frosted border stroke
val LiquidGlassStrokeBottom = Color(0x10FFFFFF) // 6% white soft highlight
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
