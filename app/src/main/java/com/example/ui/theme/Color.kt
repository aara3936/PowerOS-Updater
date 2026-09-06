package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Night-Field Wallpaper & Liquid Glass Architecture Tokens
val NightFieldBackground = Color(0xFF070B14)          // Obsidian Void
val NightFieldSurface = Color(0xFF0F172A)             // Deep Slate
val LiquidGlassFill = Color(0x1AFFFFFF)               // Strict 10% pure white frosted fill (#1AFFFFFF)
val LiquidGlassFillElevated = Color(0x26FFFFFF)       // 15% translucent fill
val LiquidGlassStrokeTop = Color(0x4DFFFFFF)          // 30% white refractive rim (#4DFFFFFF)
val LiquidGlassStrokeBottom = Color(0x10FFFFFF)       // Soft light fade (#10FFFFFF)
val LiquidGlassTextPrimary = Color(0xFFFFFFFF)        // Crisp White
val LiquidGlassTextSecondary = Color(0xFFCBD5E1)      // Slate 300
val LiquidGlassTextMuted = Color(0xFF94A3B8)          // Slate 400
val LiquidGlassCyanAccent = Color(0xFF38BDF8)         // Radiant Cyan

// Natural Light and Liquid Glass Colors
val GlassPrimary = Color(0xFF38BDF8)       // Radiant Cyan / Sky Blue
val GlassPrimaryDark = Color(0xFF0284C7)
val GlassPrimaryLight = Color(0xFF0C4A6E)
val GlassSecondary = Color(0xFF818CF8)     // Luminous Indigo
val GlassSecondaryLight = Color(0xFF312E81)
val GlassEmerald = Color(0xFF34D399)       // Luminous Mint / Emerald
val GlassEmeraldLight = Color(0xFF064E3B)
val GlassAmber = Color(0xFFFBBF24)         // Luminous Amber
val GlassAmberLight = Color(0xFF78350F)
val GlassRose = Color(0xFFFB7185)          // Luminous Rose
val GlassRoseLight = Color(0xFF881337)

// Natural Light & Night-Field Glassmorphism Base Colors
val NaturalLightBackground = NightFieldBackground
val NaturalLightSurface = NightFieldSurface
val NaturalLightSurfaceTranslucent = LiquidGlassFill
val NaturalLightCardBackground = LiquidGlassFill
val NaturalLightCardBorder = LiquidGlassStrokeTop
val NaturalLightCardBorderSubtle = LiquidGlassStrokeBottom
val NaturalLightTextPrimary = LiquidGlassTextPrimary
val NaturalLightTextSecondary = LiquidGlassTextSecondary
val NaturalLightTextMuted = LiquidGlassTextMuted

// Legacy Aliases for backwards compatibility with OTA components
val PowerCyan = GlassPrimary
val PowerCyanDark = GlassPrimaryDark
val PowerBlue = GlassPrimary
val PowerIndigo = GlassSecondary
val PowerEmerald = GlassEmerald
val PowerAmber = GlassAmber
val PowerRose = GlassRose

val DarkBackground = NaturalLightBackground
val DarkSurface = NaturalLightSurface
val DarkSurfaceVariant = Color(0xFFF1F5F9)
val DarkSurfaceContainerHigh = Color(0xFFE2E8F0)
val DarkCardBorder = Color(0xFFCBD5E1)
val DarkTextPrimary = NaturalLightTextPrimary
val DarkTextSecondary = NaturalLightTextSecondary
val DarkTextTertiary = NaturalLightTextMuted

val LightBackground = NaturalLightBackground
val LightSurface = NaturalLightSurface
val LightSurfaceVariant = Color(0xFFF1F5F9)
val LightSurfaceContainerHigh = Color(0xFFE2E8F0)
val LightTextPrimary = NaturalLightTextPrimary
val LightTextSecondary = NaturalLightTextSecondary


