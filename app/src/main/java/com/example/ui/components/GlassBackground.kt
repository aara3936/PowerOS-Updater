package com.example.ui.components

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.ui.theme.NightFieldBackground
import com.example.ui.theme.DayFieldBackground

@Composable
fun GlassBackground(
    modifier: Modifier = Modifier,
    isBlurred: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    val bgColor = if (isDark) NightFieldBackground else DayFieldBackground

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        // Wallpaper visual layer with conditional RenderEffect blur
        val wallpaperModifier = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Modifier
                .fillMaxSize()
                .graphicsLayer {
                    try {
                        val blurRadius = if (isBlurred) 25f else 0.1f // High blur for dialogs
                        val blur = RenderEffect.createBlurEffect(blurRadius, blurRadius, Shader.TileMode.CLAMP)
                        renderEffect = blur.asComposeRenderEffect()
                    } catch (_: Throwable) {
                        // Fallback safely
                    }
                }
        } else {
            Modifier.fillMaxSize()
        }

        Box(modifier = wallpaperModifier) {
            Image(
                painter = painterResource(id = R.drawable.bg_wallpaper),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Frosted ambient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                if (isDark) Color(0x990A0D14) else Color(0x33FFFFFF),
                                if (isDark) Color(0xDD0A0D14) else Color(0x99FFFFFF),
                                bgColor
                            )
                        )
                    )
            )
        }

        // Additional dim layer for dialogs to prevent text bleed-through
        if (isBlurred) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isDark) com.example.ui.theme.LiquidGlassFillDark else com.example.ui.theme.LiquidGlassBackdropLight)
            )
        }

        content()
    }
}
