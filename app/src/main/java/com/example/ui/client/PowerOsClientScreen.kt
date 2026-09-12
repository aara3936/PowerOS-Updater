package com.example.ui.client
import androidx.compose.ui.text.style.TextAlign

import android.content.Intent
import android.net.Uri
import android.view.HapticFeedbackConstants
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.DownloadProgress
import com.example.data.model.DownloadStatus
import com.example.data.model.OtaRelease
import com.example.data.model.SystemDeviceInfo
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun PowerOsClientScreen(
    deviceInfo: SystemDeviceInfo,
    latestRelease: OtaRelease?,
    isUpdateAvailable: Boolean,
    downloadProgress: DownloadProgress,
    selectedChannel: String,
    isChecking: Boolean,
    onCheckForUpdates: () -> Unit,
    onStartDownload: (OtaRelease) -> Unit,
    onPauseDownload: () -> Unit,
    onResumeDownload: (OtaRelease) -> Unit,
    onInstallUpdate: (OtaRelease) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenSpecs: () -> Unit,
    onOpenHistory: () -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val view = LocalView.current
    val context = LocalContext.current

    // Local file picker for local ROM installation
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            onShowSnackbar("Local ROM selected: ${uri.lastPathSegment ?: "rom.zip"}")
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 48.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // App Header Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_power_os_icon),
                        contentDescription = "Power OS Logo",
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.dp, LiquidGlassStrokeTop, RoundedCornerShape(14.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Power OS",
                            style = Typography.titleLarge,
                            color = LiquidGlassTextPrimary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(GlassEmerald)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${deviceInfo.deviceName} • $selectedChannel",
                                style = Typography.labelMedium,
                                color = LiquidGlassTextMuted
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    LiquidGlassIconButton(
                        icon = Icons.Default.History,
                        contentDescription = "Update History",
                        onClick = onOpenHistory
                    )

                    LiquidGlassIconButton(
                        icon = Icons.Default.Memory,
                        contentDescription = "Device Specs",
                        onClick = onOpenSpecs
                    )

                    LiquidGlassIconButton(
                        icon = Icons.Default.Settings,
                        contentDescription = "Settings",
                        onClick = onOpenSettings
                    )
                }
            }
        }

        // Hero Banner Card
        item {
            LiquidGlassCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = LiquidGlassFill
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.power_os_banner),
                        contentDescription = "Power OS Hero",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0xCC0A0D14)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = deviceInfo.currentOsVersion,
                            style = Typography.headlineMedium,
                            color = LiquidGlassTextPrimary
                        )
                        Text(
                            text = "Build: ${deviceInfo.currentBuildNumber}",
                            style = Typography.labelMedium,
                            color = GlassCyanAccent
                        )
                    }
                }
            }
        }

        // OS Update Status Card
        item {
            LiquidGlassCard(modifier = Modifier.fillMaxWidth()) {
                val isAvailable = isUpdateAvailable && latestRelease != null

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isAvailable) "System Update Available" else "Your System is Up to Date",
                            style = Typography.titleLarge,
                            color = if (isAvailable) GlassCyanAccent else LiquidGlassTextPrimary
                        )
                        Text(
                            text = if (isAvailable) "Version ${latestRelease?.versionName} is ready for installation" else "Current patch: ${deviceInfo.securityPatch}",
                            style = Typography.bodyMedium,
                            color = LiquidGlassTextMuted
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(if (isAvailable) Color(0x3300E5FF) else Color(0x2200E676)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isAvailable) Icons.Default.SystemUpdate else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (isAvailable) GlassCyanAccent else GlassEmerald,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // If update is available, show details and changelog
                if (isAvailable && latestRelease != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = LiquidGlassStrokeTop, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Package Size", style = Typography.labelMedium, color = LiquidGlassTextMuted)
                            Text(formatBytes(latestRelease.packageSizeBytes), style = Typography.bodyLarge, color = LiquidGlassTextPrimary, fontWeight = FontWeight.SemiBold)
                        }
                        Column {
                            Text("Release Channel", style = Typography.labelMedium, color = LiquidGlassTextMuted)
                            Text(latestRelease.releaseChannel, style = Typography.bodyLarge, color = GlassCyanAccent, fontWeight = FontWeight.SemiBold)
                        }
                        Column {
                            Text("Release Type", style = Typography.labelMedium, color = LiquidGlassTextMuted)
                            Text(latestRelease.releaseType, style = Typography.bodyLarge, color = LiquidGlassTextPrimary, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("What's New:", style = Typography.labelLarge, color = LiquidGlassTextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(LiquidGlassFill)
                            .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = latestRelease.changelog.ifBlank { "General bug fixes, security enhancements, and performance optimizations for Oppo A6X." },
                            style = Typography.bodyMedium,
                            color = LiquidGlassTextSecondary
                        )
                    }
                }

                // Active Download / Install Progress Bar
                AnimatedVisibility(
                    visible = downloadProgress.status == DownloadStatus.DOWNLOADING ||
                            downloadProgress.status == DownloadStatus.PAUSED ||
                            downloadProgress.status == DownloadStatus.VERIFYING ||
                            downloadProgress.status == DownloadStatus.INSTALLING,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column(modifier = Modifier.padding(top = 16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = downloadProgress.currentStep,
                                style = Typography.labelMedium,
                                color = GlassCyanAccent,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            val pct = if (downloadProgress.status == DownloadStatus.INSTALLING) {
                                (downloadProgress.installProgress * 100).toInt()
                            } else {
                                (downloadProgress.progress * 100).toInt()
                            }
                            Text(
                                text = "$pct%",
                                style = Typography.labelLarge,
                                color = LiquidGlassTextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        val progressValue = if (downloadProgress.status == DownloadStatus.INSTALLING) {
                            downloadProgress.installProgress
                        } else {
                            downloadProgress.progress
                        }

                        LinearProgressIndicator(
                            progress = { progressValue },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (downloadProgress.status == DownloadStatus.PAUSED) GlassAmber else GlassCyanAccent,
                            trackColor = Color(0x33FFFFFF)
                        )

                        if (downloadProgress.status == DownloadStatus.DOWNLOADING && downloadProgress.speedBytesPerSec > 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                            val speedMb = downloadProgress.speedBytesPerSec.toFloat() / (1024 * 1024)
                            val etaText = if (downloadProgress.etaSeconds > 0) "${downloadProgress.etaSeconds}s remaining" else "Calculating..."
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = String.format("%.2f MB/s", speedMb),
                                    style = Typography.labelSmall,
                                    color = LiquidGlassTextMuted
                                )
                                Text(
                                    text = "ETA: $etaText",
                                    style = Typography.labelSmall,
                                    color = LiquidGlassTextMuted
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Primary CTA Action Button with Spring Physics
                when (downloadProgress.status) {
                    DownloadStatus.DOWNLOADING -> {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            LiquidGlassButton(
                                onClick = onPauseDownload,
                                modifier = Modifier.weight(1f),
                                isPrimary = false
                            ) {
                                Icon(Icons.Default.Pause, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Pause", style = Typography.labelLarge)
                            }
                        }
                    }
                    DownloadStatus.PAUSED -> {
                        if (latestRelease != null) {
                            LiquidGlassButton(
                                onClick = { onResumeDownload(latestRelease) },
                                modifier = Modifier.fillMaxWidth(),
                                isPrimary = true
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Resume Download", style = Typography.labelLarge, maxLines = 2, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 16.dp))
                            }
                        }
                    }
                    DownloadStatus.READY_TO_INSTALL -> {
                        if (latestRelease != null) {
                            LiquidGlassButton(
                                onClick = { onInstallUpdate(latestRelease) },
                                modifier = Modifier.fillMaxWidth(),
                                isPrimary = true,
                                containerColor = GlassEmerald
                            ) {
                                Icon(Icons.Default.FlashOn, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Reboot & Install Now", style = Typography.labelLarge)
                            }
                        }
                    }
                    DownloadStatus.INSTALLING -> {
                        LiquidGlassButton(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = PowerOnPrimary, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Flashing Power OS Partitions...", style = Typography.labelLarge)
                        }
                    }
                    else -> {
                        if (isAvailable && latestRelease != null) {
                            LiquidGlassButton(
                                onClick = { onStartDownload(latestRelease) },
                                modifier = Modifier.fillMaxWidth(),
                                isPrimary = true
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Download & Install Update", style = Typography.labelLarge)
                            }
                        } else {
                            LiquidGlassButton(
                                onClick = onCheckForUpdates,
                                modifier = Modifier.fillMaxWidth(),
                                isPrimary = true
                            ) {
                                if (isChecking) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = PowerOnPrimary, strokeWidth = 2.dp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text("Checking Servers...", style = Typography.labelLarge)
                                } else {
                                    Icon(Icons.Default.Refresh, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Check for Updates", style = Typography.labelLarge, maxLines = 2, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Secondary Action Tiles Grid
        item {
            Text("Quick Management", style = Typography.titleMedium, color = LiquidGlassTextPrimary)
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Local Package Install Tile
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, LiquidGlassStrokeTop, RoundedCornerShape(20.dp))
                        .clickable {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            filePickerLauncher.launch(arrayOf("application/zip", "application/octet-stream", "*/*"))
                        },
                    color = LiquidGlassFill
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Icon(Icons.Default.FolderZip, contentDescription = null, tint = GlassAmber, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Install from Storage", style = Typography.titleMedium, fontSize = 14.sp, color = LiquidGlassTextPrimary)
                        Text("Select local .zip ROM", style = Typography.labelMedium, color = LiquidGlassTextMuted)
                    }
                }

                // Device Diagnostics Tile
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, LiquidGlassStrokeTop, RoundedCornerShape(20.dp))
                        .clickable {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            onOpenSpecs()
                        },
                    color = LiquidGlassFill
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Icon(Icons.Default.Hardware, contentDescription = null, tint = GlassCyanAccent, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Hardware Info", style = Typography.titleMedium, fontSize = 14.sp, color = LiquidGlassTextPrimary)
                        Text("${deviceInfo.cpuArch}", style = Typography.labelMedium, color = LiquidGlassTextMuted)
                    }
                }
            }
        }
    }
}

private fun formatBytes(bytes: Long): String {
    if (bytes <= 0) return "15 MB"
    val mb = bytes / (1024 * 1024)
    return if (mb >= 1024) {
        String.format("%.1f GB", mb / 1024.0)
    } else {
        "$mb MB"
    }
}
