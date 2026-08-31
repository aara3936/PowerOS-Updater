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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.ui.components.GradientGlowCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.PowerAmber
import com.example.ui.theme.PowerCyan
import com.example.ui.theme.PowerEmerald
import com.example.ui.theme.PowerIndigo
import com.example.ui.theme.PowerRose

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
        // Device Banner & System Status Card for Oppo A6X
        GradientGlowCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = if (isNewUpdateAvailable) PowerCyan else PowerEmerald.copy(alpha = 0.5f)
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
                                .clip(RoundedCornerShape(10.dp))
                                .background(PowerCyan.copy(alpha = 0.15f))
                                .border(1.dp, PowerCyan.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SystemUpdate,
                                contentDescription = null,
                                tint = PowerCyan,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Power OS",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Device: ${deviceInfo.deviceName}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = PowerCyan
                            )
                        }
                    }

                    if (isNewUpdateAvailable) {
                        StatusBadge(text = "UPDATE READY", color = PowerCyan, isPulsing = true)
                    } else {
                        StatusBadge(text = "UP TO DATE", color = PowerEmerald, isPulsing = false)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Installed System Version Display
                Text(
                    text = deviceInfo.currentOsVersion,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Build: ${deviceInfo.currentBuildNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Security Patch: ${deviceInfo.securityPatch}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Target Storage Directory Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF090E14))
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = null,
                        tint = PowerIndigo,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Target ROM Location:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = OtaConstants.DEFAULT_TARGET_FILE_PATH,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = PowerCyan
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Hardware Status Metrics (Battery, Storage, Wi-Fi)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Battery Metric
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (deviceInfo.isCharging) Icons.Default.BatteryChargingFull else Icons.Default.BatteryFull,
                            contentDescription = null,
                            tint = if (deviceInfo.batteryLevel > 30) PowerEmerald else PowerAmber,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${deviceInfo.batteryLevel}%",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }

                    Box(modifier = Modifier.size(1.dp, 16.dp).background(MaterialTheme.colorScheme.outlineVariant))

                    // Storage Metric
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = null,
                            tint = PowerCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${deviceInfo.storageFreeGb} GB Free",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }

                    Box(modifier = Modifier.size(1.dp, 16.dp).background(MaterialTheme.colorScheme.outlineVariant))

                    // Network Metric
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = null,
                            tint = PowerIndigo,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Wi-Fi 6",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }

        // Release Channel Selector Tabs (Stable / Beta / Dev)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Oppo A6X Release Stream",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    channels.forEach { ch ->
                        val isSelected = ch == selectedChannel
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) PowerCyan else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable { onSelectChannel(ch) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = ch,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                            )
                        }
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = Formatters.formatDate(lastCheckTime),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface
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
                colors = ButtonDefaults.buttonColors(containerColor = PowerCyan, contentColor = Color.Black),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("check_updates_button")
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

        // Update Details Card
        if (latestRelease != null && isNewUpdateAvailable) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("new_update_card"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(18.dp),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.verticalGradient(
                        listOf(PowerCyan.copy(alpha = 0.7f), PowerIndigo.copy(alpha = 0.4f))
                    )
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                StatusBadge(text = latestRelease.releaseChannel, color = PowerCyan)
                                Spacer(modifier = Modifier.width(6.dp))
                                StatusBadge(text = latestRelease.deviceModel, color = PowerIndigo)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = latestRelease.versionName,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Size badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(PowerCyan.copy(alpha = 0.15f))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = Formatters.formatBytes(latestRelease.packageSizeBytes),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = PowerCyan
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Build: ${latestRelease.buildNumber}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Android Security: ${latestRelease.securityPatch}",
                        style = MaterialTheme.typography.labelSmall,
                        color = PowerEmerald
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Changelog Section
                    Text(
                        text = "What's New in this Build:",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF090E14))
                            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = latestRelease.changelog,
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Destination Info & SHA-256
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SHA256: ${latestRelease.checksumSha256.take(16)}...",
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
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
                                tint = PowerCyan,
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
                                    .testTag("start_download_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = PowerCyan, contentColor = Color.Black),
                                shape = RoundedCornerShape(12.dp)
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
                                        color = PowerCyan
                                    )
                                    Text(
                                        text = "${Formatters.formatSpeed(downloadProgress.speedBytesPerSec)} · ${Formatters.formatEta(downloadProgress.etaSeconds)}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                LinearProgressIndicator(
                                    progress = { downloadProgress.progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = PowerCyan,
                                    trackColor = MaterialTheme.colorScheme.surface
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${Formatters.formatBytes(downloadProgress.downloadedBytes)} / ${Formatters.formatBytes(downloadProgress.totalBytes)}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Saved to: /sdcard/Download/OTA/rom.zip",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = PowerCyan
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = onPauseDownload,
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Pause, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Pause")
                                    }
                                    OutlinedButton(
                                        onClick = onCancelDownload,
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PowerRose),
                                        shape = RoundedCornerShape(10.dp)
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
                                    style = MaterialTheme.typography.labelMedium,
                                    color = PowerAmber
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick = { onResumeDownload(latestRelease) },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = PowerCyan, contentColor = Color.Black),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Resume")
                                    }
                                    OutlinedButton(
                                        onClick = onCancelDownload,
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PowerRose),
                                        shape = RoundedCornerShape(10.dp)
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
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = PowerCyan)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = downloadProgress.currentStep,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = PowerCyan
                                )
                            }
                        }

                        DownloadStatus.READY_TO_INSTALL, DownloadStatus.INSTALLING, DownloadStatus.INSTALLED -> {
                            Button(
                                onClick = { onOpenInstallDialog(latestRelease) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reboot_install_trigger_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = PowerEmerald, contentColor = Color.Black),
                                shape = RoundedCornerShape(12.dp)
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
            // Up to date card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(PowerEmerald.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = PowerEmerald)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Oppo A6X is Up to Date",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Your device has the latest Power OS build and security definitions.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Quick System Tools & Actions Grid
        Text(
            text = "Oppo A6X Utilities",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickToolCard(
                title = "Device Specs",
                subtitle = "Oppo A6X specs",
                icon = Icons.Default.Memory,
                iconColor = PowerCyan,
                modifier = Modifier.weight(1f),
                onClick = onOpenDeviceSpecs,
                testTag = "quick_tool_specs"
            )
            QuickToolCard(
                title = "Local Package",
                subtitle = "Flash rom.zip",
                icon = Icons.Default.FolderZip,
                iconColor = PowerIndigo,
                modifier = Modifier.weight(1f),
                onClick = onOpenLocalInstall,
                testTag = "quick_tool_local_zip"
            )
            QuickToolCard(
                title = "OTA History",
                subtitle = "Build logs",
                icon = Icons.Default.History,
                iconColor = PowerEmerald,
                modifier = Modifier.weight(1f),
                onClick = onOpenHistory,
                testTag = "quick_tool_history"
            )
        }
    }
}

@Composable
private fun QuickToolCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
