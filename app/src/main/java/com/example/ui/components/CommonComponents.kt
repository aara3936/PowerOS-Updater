package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GlassEmerald
import com.example.ui.theme.GlassPrimary
import com.example.ui.theme.GlassSecondary
import com.example.ui.theme.NaturalLightTextPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Formatters {
    fun formatBytes(bytes: Long): String {
        if (bytes <= 0) return "0 MB"
        val gb = bytes.toDouble() / (1024 * 1024 * 1024)
        if (gb >= 1.0) {
            return String.format(Locale.US, "%.2f GB", gb)
        }
        val mb = bytes.toDouble() / (1024 * 1024)
        if (mb >= 1.0) {
            return String.format(Locale.US, "%.1f MB", mb)
        }
        val kb = bytes.toDouble() / 1024
        return String.format(Locale.US, "%.0f KB", kb)
    }

    fun formatSpeed(bytesPerSec: Long): String {
        if (bytesPerSec <= 0) return "0.0 MB/s"
        val mbps = (bytesPerSec.toDouble() / (1024 * 1024))
        return String.format(Locale.US, "%.1f MB/s", mbps)
    }

    fun formatEta(seconds: Long): String {
        if (seconds <= 0) return "Calculating..."
        val mins = seconds / 60
        val secs = seconds % 60
        return if (mins > 0) "${mins}m ${secs}s left" else "${secs}s left"
    }

    fun formatDate(timestamp: Long): String {
        if (timestamp <= 0) return "Never"
        val sdf = SimpleDateFormat("MMM dd, yyyy · HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}

@Composable
fun PulsingStatusDot(
    modifier: Modifier = Modifier,
    color: Color = GlassEmerald
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = modifier.size(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = alpha * 0.4f))
        )
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
    }
}

@Composable
fun StatusBadge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = GlassPrimary,
    isPulsing: Boolean = false
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(color.copy(alpha = 0.12f))
            .border(1.dp, color.copy(alpha = 0.30f), RoundedCornerShape(24.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isPulsing) {
            PulsingStatusDot(color = color)
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            ),
            color = color
        )
    }
}

/**
 * Liquid Glassmorphic Card Container.
 * Features true frosted glass visuals: 26dp rounded corners, semi-transparent white fills,
 * subtle atmospheric ambient shadows, and 1px delicate light border strokes.
 */
@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(26.dp),
    accentGradient: Brush? = null,
    borderStroke: BorderStroke? = BorderStroke(
        1.dp,
        Brush.verticalGradient(
            listOf(
                Color.White.copy(alpha = 0.85f),
                Color.White.copy(alpha = 0.45f),
                Color(0x2664748B)
            )
        )
    ),
    contentPadding: Dp = 20.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = shape,
                ambientColor = Color(0x140F172A),
                spotColor = Color(0x1A0284C7)
            ),
        shape = shape,
        color = Color.Transparent,
        border = borderStroke
    ) {
        Box(
            modifier = Modifier
                .background(
                    accentGradient ?: Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.78f),
                            Color.White.copy(alpha = 0.62f),
                            Color(0xEEF8FAFC)
                        )
                    )
                )
                .padding(contentPadding)
        ) {
            content()
        }
    }
}

/**
 * Translucent frosted interactive chip/pill with 24dp rounded corners.
 */
@Composable
fun GlassChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    activeColor: Color = GlassPrimary
) {
    val backgroundBrush = if (isSelected) {
        Brush.horizontalGradient(
            listOf(
                activeColor,
                activeColor.copy(alpha = 0.85f)
            )
        )
    } else {
        Brush.verticalGradient(
            listOf(
                Color.White.copy(alpha = 0.85f),
                Color.White.copy(alpha = 0.55f)
            )
        )
    }

    val textColor = if (isSelected) Color.White else NaturalLightTextPrimary
    val borderColor = if (isSelected) activeColor.copy(alpha = 0.6f) else Color(0x40CBD5E1)

    Box(
        modifier = modifier
            .shadow(
                elevation = if (isSelected) 4.dp else 1.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color(0x10000000)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundBrush)
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            color = textColor
        )
    }
}

/**
 * Frosted Glass Action Button with 26dp rounded corners.
 */
@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = GlassPrimary,
    leadingIcon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(26.dp),
                spotColor = color.copy(alpha = 0.4f)
            ),
        shape = RoundedCornerShape(26.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFCBD5E1),
            disabledContentColor = Color(0xFF64748B)
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.4.sp
                )
            )
        }
    }
}
