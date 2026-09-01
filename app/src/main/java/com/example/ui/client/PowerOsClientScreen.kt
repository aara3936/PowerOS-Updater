package com.example.ui.client

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.ui.components.GlassChip
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
    onOpenLocalInstall: () -> Unit
) {
    val context = LocalContext.current
    val channels = listOf("Stable", "Beta", "Developer Preview")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Device Banner & System Status Card for Oppo A6X (Liquid Glass)
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
                                .size(46.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            GlassPrimary.copy(alpha = 0.18f),
                                            GlassSecondary.copy(alpha = 0.12f)
                                        )
                                    )
                                )
                                .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(14.dp)),
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
                                    letterSpacing = 0.6.sp
                                ),
                                color = NaturalLightTextPrimary
                            )
                            Text(
                                text = "Device: ${deviceInfo.deviceName}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
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

                // Installed System Version Display
                Text(
                    text = deviceInfo.currentOsVersion,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = NaturalLightTextPrimary
                )
                Text(
                    text = "Build Fingerprint: ${deviceInfo.currentBuildNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = NaturalLightTextSecondary
                )
                Text(
                    text = "Security Patch: ${deviceInfo.securityPatch}",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                    color = GlassEmerald
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Target Storage Directory Card (Glass styling)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.7f))
                        .border(1.dp, Color(0x33CBD5E1), RoundedCornerShape(12.dp))
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
                            text = "Target ROM Filepath:",
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

                // Hardware Status Metrics (Battery, Storage, Wi-Fi) with Frosted Pill container
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.65f))
                        .border(1.dp, Color(0x26CBD5E1), RoundedCornerShape(14.dp))
                        .padding(vertical = 10.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Battery Metric
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (deviceInfo.isCharging) Icons.Default.BatteryChargingFull else Icons.Default.BatteryFull,
                            contentDescription = null,
                            tint = if (deviceInfo.batteryLevel > 30) GlassEmerald else GlassAmber,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${deviceInfo.batteryLevel}%",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = NaturalLightTextPrimary
                        )
                    }

                    Box(modifier = Modifier.size(1.dp, 16.dp).background(Color(0x33CBD5E1)))

                    // Storage Metric
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = null,
                            tint = GlassPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${deviceInfo.storageFreeGb} GB Free",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = NaturalLightTextPrimary
                        )
                    }

                    Box(modifier = Modifier.size(1.dp, 16.dp).background(Color(0x33CBD5E1)))

                    // Network Metric
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = null,
                            tint = GlassSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Wi-Fi 6",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = NaturalLightTextPrimary
                        )
                    }
                }
            }
        }

        // Release Channel Selector Tabs (Stable / Beta / Dev)
        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = 14.dp
        ) {
            Column {
                Text(
                    text = "Release Stream",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = NaturalLightTextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    channels.forEach { ch ->
                        val isSelected = ch == selectedChannel
                        GlassChip(
                            text = ch,
                            isSelected = isSelected,
                            onClick = { onSelectChannel(ch) },
                            modifier = Modifier.weight(1f),
                            activeColor = if (ch == "Stable") GlassPrimary else if (ch == "Beta") GlassAmber else GlassSecondary
                        )
                    }
                }
            }
        }

        // Check for updates Action Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "GitHub Raw Sync:",
                    style = MaterialTheme.typography.labelSmall,
                    color = NaturalLightTextMuted
                )
                Text(
                    text = Formatters.formatDate(lastCheckTime),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = NaturalLightTextPrimary
                )
            }

            val infiniteTransition = rememberInfiniteTransition(label = "check_spin")
            val spinAngle by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "spin_angle"
            )

            Button(
                onClick = onCheckForUpdates,
                enabled = !isChecking,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GlassPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(14.dp),
                        ambientColor = Color(0x150284C7),
                        spotColor = GlassPrimary.copy(alpha = 0.35f)
                    )
                    .testTag("check_updates_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp)
                        .then(if (isChecking) Modifier.rotate(spinAngle) else Modifier)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isChecking) "Checking GitHub..." else "Check for Updates",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Update Details Card (Liquid Glass with frosted sheen)
        if (latestRelease != null && isNewUpdateAvailable) {
            LiquidGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("new_update_card"),
                borderStroke = BorderStroke(
                    1.dp,
                    Brush.verticalGradient(
                        listOf(
                            GlassPrimary.copy(alpha = 0.6f),
                            GlassSecondary.copy(alpha = 0.3f),
                            Color.White.copy(alpha = 0.8f)
                        )
                    )
                )
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                StatusBadge(text = latestRelease.releaseChannel, color = GlassPrimary)
                                Spacer(modifier = Modifier.width(6.dp))
                                StatusBadge(text = latestRelease.deviceModel, color = GlassSecondary)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = latestRelease.versionName,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = NaturalLightTextPrimary
                            )
                        }

                        // Size badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(GlassPrimary.copy(alpha = 0.12f))
                                .border(1.dp, GlassPrimary.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = Formatters.formatBytes(latestRelease.packageSizeBytes),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = GlassPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Build: ${latestRelease.buildNumber}",
                        style = MaterialTheme.typography.bodySmall,
                        color = NaturalLightTextSecondary
                    )
                    Text(
                        text = "Android Security: ${latestRelease.securityPatch}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = GlassEmerald
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Changelog Section
                    Text(
                        text = "What's New in this Build:",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = NaturalLightTextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.85f))
                            .border(1.dp, Color(0x33CBD5E1), RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = latestRelease.changelog,
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 19.sp),
                            color = NaturalLightTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Destination Info & SHA-256
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White.copy(alpha = 0.6f))
                            .border(1.dp, Color(0x26CBD5E1), RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SHA256: ${latestRelease.checksumSha256.take(16)}...",
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = FontFamily.Monospace,
                            color = NaturalLightTextSecondary
                        )
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("SHA256", latestRelease.checksumSha256))
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy SHA-256",
                                tint = GlassPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Download / Installation Lifecycle Controls
                    when (downloadProgress.status) {
                        DownloadStatus.IDLE -> {
                            Button(
                                onClick = { onStartDownload(latestRelease) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 4.dp,
                                        shape = RoundedCornerShape(14.dp),
                                        ambientColor = Color(0x150284C7),
                                        spotColor = GlassPrimary.copy(alpha = 0.4f)
                                    )
                                    .testTag("start_download_btn"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GlassPrimary,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Download, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Download ROM (${Formatters.formatBytes(latestRelease.packageSizeBytes)})",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        DownloadStatus.DOWNLOADING -> {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${(downloadProgress.progress * 100).toInt()}% Downloaded",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = GlassPrimary
                                    )
                                    Text(
                                        text = "${Formatters.formatSpeed(downloadProgress.speedBytesPerSec)} · ${Formatters.formatEta(downloadProgress.etaSeconds)}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = NaturalLightTextSecondary
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                LinearProgressIndicator(
                                    progress = { downloadProgress.progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = GlassPrimary,
                                    trackColor = Color(0xFFE2E8F0)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${Formatters.formatBytes(downloadProgress.downloadedBytes)} / ${Formatters.formatBytes(downloadProgress.totalBytes)}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = NaturalLightTextSecondary
                                    )
                                    Text(
                                        text = "Saved: /sdcard/Download/OTA/rom.zip",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                        color = GlassPrimary
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = onPauseDownload,
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Pause, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Pause")
                                    }
                                    OutlinedButton(
                                        onClick = onCancelDownload,
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GlassRose),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Cancel")
                                    }
                                }
                            }
                        }

                        DownloadStatus.PAUSED -> {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Download paused at ${(downloadProgress.progress * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = GlassAmber
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick = { onResumeDownload(latestRelease) },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = GlassPrimary,
                                            contentColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Resume")
                                    }
                                    OutlinedButton(
                                        onClick = onCancelDownload,
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GlassRose),
                                        shape = RoundedCornerShape(12.dp)
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
                                    .padding(vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = GlassPrimary)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = downloadProgress.currentStep,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                    color = GlassPrimary
                                )
                            }
                        }

                        DownloadStatus.READY_TO_INSTALL, DownloadStatus.INSTALLING, DownloadStatus.INSTALLED -> {
                            Button(
                                onClick = { onOpenInstallDialog(latestRelease) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 4.dp,
                                        shape = RoundedCornerShape(14.dp),
                                        ambientColor = Color(0x1510B981),
                                        spotColor = GlassEmerald.copy(alpha = 0.4f)
                                    )
                                    .testTag("reboot_install_trigger_btn"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GlassEmerald,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (downloadProgress.status == DownloadStatus.INSTALLED) "Reboot Oppo A6X" else "Reboot & Apply Update",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        else -> {}
                    }
                }
            }
        } else if (!isChecking) {
            // Up to date card (Liquid Glass)
            LiquidGlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(GlassEmerald.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = GlassEmerald)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Oppo A6X is Up to Date",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = NaturalLightTextPrimary
                        )
                        Text(
                            text = "Your device has the latest Power OS build and security definitions.",
                            style = MaterialTheme.typography.bodySmall,
                            color = NaturalLightTextSecondary
                        )
                    }
                }
            }
        }

        // Quick System Tools & Actions Grid
        Text(
            text = "Oppo A6X Utilities",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = NaturalLightTextSecondary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickToolGlassCard(
                title = "Device Specs",
                subtitle = "Hardware details",
                icon = Icons.Default.Memory,
                iconColor = GlassPrimary,
                modifier = Modifier.weight(1f),
                onClick = onOpenDeviceSpecs,
                testTag = "quick_tool_specs"
            )
            QuickToolGlassCard(
                title = "Local Package",
                subtitle = "Flash rom.zip",
                icon = Icons.Default.FolderZip,
                iconColor = GlassSecondary,
                modifier = Modifier.weight(1f),
                onClick = onOpenLocalInstall,
                testTag = "quick_tool_local_zip"
            )
            QuickToolGlassCard(
                title = "OTA History",
                subtitle = "Update records",
                icon = Icons.Default.History,
                iconColor = GlassEmerald,
                modifier = Modifier.weight(1f),
                onClick = onOpenHistory,
                testTag = "quick_tool_history"
            )
        }
    }
}

@Composable
private fun QuickToolGlassCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    testTag: String
) {
    LiquidGlassCard(
        modifier = modifier
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        contentPadding = 12.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = NaturalLightTextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = NaturalLightTextMuted
            )
        }
    }
}

