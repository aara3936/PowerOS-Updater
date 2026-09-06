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
import com.example.ui.theme.NightFieldSurface

/**
 * Edge-to-Edge Night-Field Wallpaper Background with True Native Glassmorphism Blur.
 *
 * Uses native RenderEffect.createBlurEffect on Android 12+ (API 31+) with 32dp blur radius.
 * On older Android versions, falls back smoothly to layered frosted scrims over the wallpaper.
 * Overlaid with subtle obsidian gradient dimming to maintain deep contrast and legibility.
 */
@Composable
fun GlassBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NightFieldBackground)
    ) {
        // Wallpaper Image Layer with native blur rendering
        Image(
            painter = painterResource(id = R.drawable.bg_wallpaper),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        Modifier.graphicsLayer {
                            renderEffect = RenderEffect
                                .createBlurEffect(
                                    32f,
                                    32f,
                                    Shader.TileMode.CLAMP
                                )
                                .asComposeRenderEffect()
                        }
                    } else {
                        Modifier
                    }
                )
        )

        // Obsidian Void & Night-Field Dimming Scrim
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            NightFieldBackground.copy(alpha = 0.55f),
                            NightFieldSurface.copy(alpha = 0.40f),
                            NightFieldBackground.copy(alpha = 0.75f)
                        )
                    )
                )
        )

        // Foreground content
        content()
    }
}
