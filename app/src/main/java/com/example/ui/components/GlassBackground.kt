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
        // Wallpaper / Night-field visual layer with RenderEffect blur
        val wallpaperModifier = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Modifier
                .fillMaxSize()
                .graphicsLayer {
                    try {
                        val blur = RenderEffect.createBlurEffect(28f, 28f, Shader.TileMode.CLAMP)
                        renderEffect = blur.asComposeRenderEffect()
                    } catch (_: Throwable) {
                        // Fallback safely
                    }
                }
        } else {
            Modifier.fillMaxSize()
        }

        // Try to draw wallpaper resource or dark ambient fallback
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
                                Color(0x990A0D14),
                                Color(0xDD0A0D14),
                                NightFieldBackground
                            )
                        )
                    )
            )
        }

        // Foreground content
        content()
    }
}
