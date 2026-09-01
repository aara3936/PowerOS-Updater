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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GlassEmerald
import com.example.ui.theme.GlassPrimary
import com.example.ui.theme.GlassSecondary
import com.example.ui.theme.NaturalLightTextMuted
import com.example.ui.theme.NaturalLightTextPrimary
import com.example.ui.theme.NaturalLightTextSecondary
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
 * True Frosted Liquid Glassmorphism Container with 28dp Global Rounded Styling.
 * Uses translucent white fills, fine 1px light border stroke, and soft drop-shadow.
 */
@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(28.dp),
    accentGradient: Brush? = null,
    borderStroke: BorderStroke? = BorderStroke(
        1.dp,
        Brush.verticalGradient(
            listOf(
                Color.White.copy(alpha = 0.85f),
                Color.White.copy(alpha = 0.45f),
                Color(0x33CBD5E1)
            )
        )
    ),
    contentPadding: Dp = 20.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 10.dp,
                shape = shape,
                ambientColor = Color(0x1A0F172A),
                spotColor = Color(0x1F0284C7)
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
                            Color.White.copy(alpha = 0.60f),
                            Color(0xE6F8FAFC)
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
 * 3-Channel Segmented Control with true Glassmorphism and 28dp rounded pill styling.
 * Channels: [ Stable ] | [ Early Access ] | [ Closed Beta ]
 */
@Composable
fun GlassChannelSegmentedBar(
    channels: List<String>,
    selectedChannel: String,
    onSelectChannel: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = Color(0x120F172A),
                spotColor = Color(0x180284C7)
            )
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.80f),
                        Color.White.copy(alpha = 0.60f)
                    )
                )
            )
            .border(
                1.dp,
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.90f),
                        Color(0x33CBD5E1)
                    )
                ),
                RoundedCornerShape(28.dp)
            )
            .padding(5.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        channels.forEach { channel ->
            val isSelected = channel.equals(selectedChannel, ignoreCase = true)
            val channelColor = when (channel) {
                "Stable" -> GlassPrimary
                "Early Access" -> GlassSecondary
                "Closed Beta" -> Color(0xFFE11D48) // Rose Red
                else -> GlassPrimary
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (isSelected) {
                            Modifier.shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(24.dp),
                                spotColor = channelColor.copy(alpha = 0.35f)
                            )
                        } else Modifier
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        if (isSelected) {
                            Brush.horizontalGradient(
                                listOf(
                                    channelColor,
                                    channelColor.copy(alpha = 0.85f)
                                )
                            )
                        } else {
                            Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                        }
                    )
                    .clickable { onSelectChannel(channel) }
                    .padding(vertical = 10.dp)
                    .testTag("channel_tab_${channel.lowercase().replace(" ", "_")}"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = channel,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        letterSpacing = 0.2.sp
                    ),
                    color = if (isSelected) Color.White else NaturalLightTextSecondary,
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * Frosted Glass Action Button with 28dp rounded corners.
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
                shape = RoundedCornerShape(28.dp),
                spotColor = color.copy(alpha = 0.45f)
            ),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFCBD5E1),
            disabledContentColor = Color(0xFF64748B)
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
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
