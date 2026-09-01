package com.example.ui.client

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DownloadProgress
import com.example.data.model.DownloadStatus
import com.example.data.model.OtaConstants
import com.example.data.model.OtaRelease
import com.example.data.model.SystemDeviceInfo
import com.example.ui.components.Formatters
import com.example.ui.components.GlassButton
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.GlassAmber
import com.example.ui.theme.GlassEmerald
import com.example.ui.theme.GlassPrimary
import com.example.ui.theme.GlassRose
import com.example.ui.theme.GlassSecondary
import com.example.ui.theme.NaturalLightTextMuted
import com.example.ui.theme.NaturalLightTextPrimary
import com.example.ui.theme.NaturalLightTextSecondary

@Composable
fun PowerOsClientScreen(
    deviceInfo: SystemDeviceInfo,
    latestRelease: OtaRelease?,
    isNewUpdateAvailable: Boolean,
    downloadProgress: DownloadProgress,
    isChecking: Boolean,
    lastCheckTime: Long,
    selectedChannel: String,
    onSelectChannel: (String) -> Unit,
    onCheckForUpdates: () -> Unit,
    onStartDownload: (OtaRelease) -> Unit,
    onPauseDownload: () -> Unit,
    onResumeDownload: (OtaRelease) -> Unit,
    onCancelDownload: () -> Unit,
    onOpenInstallDialog: (OtaRelease) -> Unit,
    onOpenDeviceSpecs: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenLocalInstall: () -> Unit,
    onOpenSettings: () -> Unit,
    onResetUpdateState: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Device Profile Glass Card (Oppo A6X & Hardware State)
        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            GlassPrimary.copy(alpha = 0.18f),
                                            GlassSecondary.copy(alpha = 0.12f)
                                        )
                                    )
                                )
                                .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SystemUpdate,
                                contentDescription = null,
                                tint = GlassPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Power OS",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = NaturalLightTextPrimary
                            )
                            Text(
                                text = deviceInfo.deviceName,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = GlassPrimary
                            )
                        }
                    }

                    if (isNewUpdateAvailable) {
                        StatusBadge(text = "UPDATE READY", color = GlassPrimary, isPulsing = true)
                    } else {
                        StatusBadge(text = "UP TO DATE", color = GlassEmerald, isPulsing = false)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = deviceInfo.currentOsVersion,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = NaturalLightTextPrimary
                )
                Text(
                    text = "Build: ${deviceInfo.currentBuildNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = NaturalLightTextSecondary
                )
                Text(
                    text = "Security Patch: ${deviceInfo.securityPatch}",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                    color = GlassEmerald
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Target Storage Directory Info
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.70f))
                        .border(1.dp, Color(0x33CBD5E1), RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = null,
                        tint = GlassSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Download Target:",
                            style = MaterialTheme.typography.labelSmall,
                            color = NaturalLightTextMuted
                        )
                        Text(
                            text = OtaConstants.DEFAULT_TARGET_FILE_PATH,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = GlassPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Hardware Summary Pill
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.65f))
                        .border(1.dp, Color(0x26CBD5E1), RoundedCornerShape(16.dp))
                        .padding(vertical = 10.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (deviceInfo.isCharging) Icons.Default.BatteryChargingFull else Icons.Default.BatteryFull,
                            contentDescription = null,
                            tint = if (deviceInfo.batteryLevel > 20) GlassEmerald else GlassAmber,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${deviceInfo.batteryLevel}%",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = NaturalLightTextPrimary
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = null,
                            tint = GlassPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${String.format("%.1f", deviceInfo.storageFreeGb)} GB Free",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = NaturalLightTextPrimary
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = null,
                            tint = GlassSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = deviceInfo.networkType,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = NaturalLightTextPrimary
                        )
                    }
                }
            }
        }

        // 2. Main Live OTA State Glass Card
        if (isChecking) {
            // Checking State Animation
            LiquidGlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val infiniteTransition = rememberInfiniteTransition(label = "spin")
                    val angle by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1200, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "spin_angle"
                    )

                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(GlassPrimary.copy(alpha = 0.12f))
                            .border(1.dp, GlassPrimary.copy(alpha = 0.25f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Checking",
                            tint = GlassPrimary,
                            modifier = Modifier
                                .size(32.dp)
                                .rotate(angle)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Checking for Updates...",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = NaturalLightTextPrimary
                    )
                    Text(
                        text = "Querying live GitHub metadata.json...",
                        style = MaterialTheme.typography.bodySmall,
                        color = NaturalLightTextSecondary
                    )
                }
            }
        } else if (latestRelease != null && isNewUpdateAvailable) {
            // Update Available State
            LiquidGlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(GlassPrimary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SystemUpdate,
                                    contentDescription = null,
                                    tint = GlassPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Update Available",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = NaturalLightTextPrimary
                                )
                                Text(
                                    text = latestRelease.releaseType,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GlassPrimary
                                )
                            }
                        }

                        StatusBadge(text = "NEW", color = GlassPrimary, isPulsing = true)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = latestRelease.versionName,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = NaturalLightTextPrimary
                    )
                    if (latestRelease.buildNumber.isNotBlank()) {
                        Text(
                            text = "Build: ${latestRelease.buildNumber}",
                            style = MaterialTheme.typography.bodySmall,
                            color = NaturalLightTextSecondary
                        )
                    }
                    if (latestRelease.packageSizeBytes > 0) {
                        Text(
                            text = "Package Size: ${Formatters.formatBytes(latestRelease.packageSizeBytes)}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = GlassSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Changelog Glass Box
                    Text(
                        text = "What's New:",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = NaturalLightTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color.White.copy(alpha = 0.75f))
                            .border(1.dp, Color(0x33CBD5E1), RoundedCornerShape(18.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = latestRelease.changelog,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                            color = NaturalLightTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Download & Installation Lifecycle Handlers
                    when (downloadProgress.status) {
                        DownloadStatus.IDLE -> {
                            GlassButton(
                                text = "Download & Install Update",
                                onClick = { onStartDownload(latestRelease) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("download_ota_btn"),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Download,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            )
                        }

                        DownloadStatus.DOWNLOADING -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White.copy(alpha = 0.85f))
                                    .border(1.dp, Color(0x33CBD5E1), RoundedCornerShape(20.dp))
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Downloading ROM package...",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = NaturalLightTextPrimary
                                    )
                                    Text(
                                        text = "${(downloadProgress.progress * 100).toInt()}%",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = GlassPrimary
                                    )
                                }

                                LinearProgressIndicator(
                                    progress = { downloadProgress.progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = GlassPrimary,
                                    trackColor = Color(0xFFE2E8F0)
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = Formatters.formatSpeed(downloadProgress.speedBytesPerSec),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                        color = GlassSecondary
                                    )
                                    Text(
                                        text = Formatters.formatEta(downloadProgress.etaSeconds),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = NaturalLightTextMuted
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = onPauseDownload,
                                        shape = RoundedCornerShape(20.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("pause_download_btn")
                                    ) {
                                        Icon(Icons.Default.Pause, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Pause")
                                    }

                                    OutlinedButton(
                                        onClick = onCancelDownload,
                                        shape = RoundedCornerShape(20.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("cancel_download_btn")
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Cancel")
                                    }
                                }
                            }
                        }

                        DownloadStatus.PAUSED -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White.copy(alpha = 0.85f))
                                    .border(1.dp, Color(0x33CBD5E1), RoundedCornerShape(20.dp))
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "Download Paused (${(downloadProgress.progress * 100).toInt()}%)",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = GlassAmber
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { onResumeDownload(latestRelease) },
                                        shape = RoundedCornerShape(20.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = GlassPrimary),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("resume_download_btn")
                                    ) {
                                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Resume")
                                    }
                                    OutlinedButton(
                                        onClick = onCancelDownload,
                                        shape = RoundedCornerShape(20.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Cancel")
                                    }
                                }
                            }
                        }

                        DownloadStatus.VERIFYING -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White.copy(alpha = 0.85f))
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = GlassPrimary,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(14.dp))
                                Text(
                                    text = "Verifying package integrity...",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = NaturalLightTextPrimary
                                )
                            }
                        }

                        DownloadStatus.READY_TO_INSTALL -> {
                            GlassButton(
                                text = "Reboot & Flash Update",
                                onClick = { onOpenInstallDialog(latestRelease) },
                                color = GlassEmerald,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reboot_flash_btn"),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.RestartAlt,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            )
                        }

                        DownloadStatus.INSTALLING -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White.copy(alpha = 0.85f))
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = downloadProgress.currentStep,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = GlassEmerald
                                )
                                LinearProgressIndicator(
                                    progress = { downloadProgress.installProgress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = GlassEmerald,
                                    trackColor = Color(0xFFE2E8F0)
                                )
                            }
                        }

                        DownloadStatus.INSTALLED -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(GlassEmerald.copy(alpha = 0.12f))
                                    .border(1.dp, GlassEmerald.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = GlassEmerald,
                                    modifier = Modifier.size(36.dp)
                                )
                                Text(
                                    text = "System Successfully Updated!",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = GlassEmerald
                                )
                                Button(
                                    onClick = onResetUpdateState,
                                    shape = RoundedCornerShape(20.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = GlassEmerald)
                                ) {
                                    Text("Done", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        } else {
            // Up to date state - Clean Frosted Shield Card
            LiquidGlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(GlassEmerald.copy(alpha = 0.15f))
                            .border(1.dp, GlassEmerald.copy(alpha = 0.35f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = GlassEmerald,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Your System is Up to Date",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = NaturalLightTextPrimary
                    )
                    Text(
                        text = "${deviceInfo.deviceName} is running the latest Power OS build.",
                        style = MaterialTheme.typography.bodySmall,
                        color = NaturalLightTextSecondary
                    )
                    Text(
                        text = "Last checked: ${Formatters.formatDate(lastCheckTime)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = NaturalLightTextMuted
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    GlassButton(
                        text = "Check for Updates",
                        onClick = onCheckForUpdates,
                        modifier = Modifier.testTag("check_updates_btn"),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                }
            }
        }

        // 3. Quick Action Glass Tiles (Diagnostics, History, Endpoint Settings, Sideload)
        Text(
            text = "System Tools & Actions",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = NaturalLightTextPrimary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionTile(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Memory,
                title = "Hardware Info",
                subtitle = "Oppo A6X Specs",
                color = GlassPrimary,
                onClick = onOpenDeviceSpecs,
                testTag = "action_specs_tile"
            )

            ActionTile(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.History,
                title = "Update History",
                subtitle = "Installed Logs",
                color = GlassSecondary,
                onClick = onOpenHistory,
                testTag = "action_history_tile"
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionTile(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Folder,
                title = "Local Sideload",
                subtitle = "Manual ROM Zip",
                color = GlassAmber,
                onClick = onOpenLocalInstall,
                testTag = "action_sideload_tile"
            )

            ActionTile(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Settings,
                title = "OTA Endpoint",
                subtitle = "GitHub Raw URL",
                color = GlassEmerald,
                onClick = onOpenSettings,
                testTag = "action_settings_tile"
            )
        }
    }
}

@Composable
private fun ActionTile(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color(0x10000000)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.85f))
            .border(1.dp, Color.White.copy(alpha = 0.95f), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(16.dp)
            .then(if (testTag.isNotBlank()) Modifier.testTag(testTag) else Modifier)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = NaturalLightTextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = NaturalLightTextSecondary
            )
        }
    }
}
