package com.example.ui.dialogs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.data.model.UpdateHistoryItem
import com.example.ui.components.Formatters
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.GlassAmber
import com.example.ui.theme.GlassEmerald
import com.example.ui.theme.GlassPrimary
import com.example.ui.theme.GlassRose
import com.example.ui.theme.GlassSecondary
import com.example.ui.theme.NaturalLightCardBackground
import com.example.ui.theme.NaturalLightTextMuted
import com.example.ui.theme.NaturalLightTextPrimary
import com.example.ui.theme.NaturalLightTextSecondary

/**
 * Oppo A6X Hardware and System Diagnostics Dialog with Natural Light & Liquid Glass.
 */
@Composable
fun DeviceSpecsDialog(
    deviceInfo: SystemDeviceInfo,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(GlassPrimary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Smartphone, contentDescription = null, tint = GlassPrimary, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Oppo A6X Specifications",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = NaturalLightTextPrimary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SpecRow(label = "Device Model", value = deviceInfo.deviceName)
                SpecRow(label = "Device Codename", value = deviceInfo.deviceCodename)
                SpecRow(label = "Installed OS", value = deviceInfo.currentOsVersion)
                SpecRow(label = "Build Fingerprint", value = deviceInfo.currentBuildNumber)
                SpecRow(label = "Android Version", value = deviceInfo.androidVersion)
                SpecRow(label = "Security Patch", value = deviceInfo.securityPatch)
                SpecRow(label = "Kernel", value = deviceInfo.kernelVersion)
                SpecRow(label = "SoC Architecture", value = deviceInfo.cpuArch)
                SpecRow(label = "Available Storage", value = "${deviceInfo.storageFreeGb} GB / ${deviceInfo.storageTotalGb} GB")
                SpecRow(label = "Battery & State", value = "${deviceInfo.batteryLevel}% (Charging: ${deviceInfo.isCharging})")
                SpecRow(label = "Target Directory", value = OtaConstants.DEFAULT_TARGET_FILE_PATH)
                SpecRow(label = "GitHub Raw Source", value = OtaConstants.DEFAULT_RAW_JSON_URL)
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = GlassPrimary, contentColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun SpecRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF1F5F9))
            .border(1.dp, Color(0x22CBD5E1), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = NaturalLightTextMuted)
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = NaturalLightTextPrimary
        )
    }
}

/**
 * Interactive Recovery Flashing / Reboot Dialog for Oppo A6X.
 */
@Composable
fun RecoveryInstallDialog(
    release: OtaRelease?,
    progress: DownloadProgress,
    onConfirmInstall: () -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    val isInstalling = progress.status == DownloadStatus.INSTALLING
    val isFinished = progress.status == DownloadStatus.INSTALLED

    AlertDialog(
        onDismissRequest = { if (!isInstalling) onDismiss() },
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isFinished) GlassEmerald.copy(alpha = 0.15f) else GlassPrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFinished) Icons.Default.CheckCircle else Icons.Default.RestartAlt,
                        contentDescription = null,
                        tint = if (isFinished) GlassEmerald else GlassPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = when {
                        isFinished -> "Update Installed"
                        isInstalling -> "Flashing Oppo A6X Recovery..."
                        else -> "Reboot & Apply Update"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = NaturalLightTextPrimary
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (isFinished) {
                    Text(
                        text = "The update to ${release?.versionName ?: "Power OS"} has been successfully installed on your Oppo A6X device.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NaturalLightTextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(GlassEmerald.copy(alpha = 0.12f))
                            .border(1.dp, GlassEmerald.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "System is rebooting into the updated OS slot...",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = GlassEmerald
                        )
                    }
                } else if (isInstalling) {
                    Text(
                        text = progress.currentStep,
                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold),
                        color = GlassPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { progress.installProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = GlassPrimary,
                        trackColor = Color(0xFFE2E8F0)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${(progress.installProgress * 100).toInt()}% completed",
                        style = MaterialTheme.typography.labelSmall,
                        color = NaturalLightTextSecondary
                    )
                } else {
                    Text(
                        text = "Are you ready to install ${release?.versionName ?: "the update"} onto your Oppo A6X?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NaturalLightTextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Source: /sdcard/Download/OTA/rom.zip",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold),
                        color = GlassPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "The device will reboot into dynamic fastbootd/recovery to flash the partitions.",
                        style = MaterialTheme.typography.bodySmall,
                        color = NaturalLightTextSecondary
                    )
                }
            }
        },
        confirmButton = {
            if (isFinished) {
                Button(
                    onClick = onReset,
                    colors = ButtonDefaults.buttonColors(containerColor = GlassEmerald, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Done", fontWeight = FontWeight.Bold)
                }
            } else if (!isInstalling) {
                Button(
                    onClick = onConfirmInstall,
                    colors = ButtonDefaults.buttonColors(containerColor = GlassEmerald, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Reboot & Install", fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            if (!isInstalling && !isFinished) {
                OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(12.dp)) {
                    Text("Cancel")
                }
            }
        }
    )
}

/**
 * Local ROM Zip Sideload Dialog for /sdcard/Download/OTA/rom.zip.
 */
@Composable
fun LocalPackageInstallDialog(
    onDismiss: () -> Unit,
    onFlashLocalZip: (String) -> Unit
) {
    var packagePath by remember { mutableStateOf(OtaConstants.DEFAULT_TARGET_FILE_PATH) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(GlassSecondary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.FolderZip, contentDescription = null, tint = GlassSecondary, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Local Package Sideload",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = NaturalLightTextPrimary
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Flash a pre-downloaded or customized Power OS ROM package from device storage directly onto Oppo A6X.",
                    style = MaterialTheme.typography.bodySmall,
                    color = NaturalLightTextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = packagePath,
                    onValueChange = { packagePath = it },
                    label = { Text("Local File Path") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Default target: /sdcard/Download/OTA/rom.zip",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = GlassPrimary
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onFlashLocalZip(packagePath)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = GlassPrimary, contentColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Select & Flash", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(12.dp)) {
                Text("Cancel")
            }
        }
    )
}

/**
 * System Update History Dialog.
 */
@Composable
fun UpdateHistoryDialog(
    history: List<UpdateHistoryItem>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(GlassEmerald.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.History, contentDescription = null, tint = GlassEmerald, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Oppo A6X Update History",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = NaturalLightTextPrimary
                )
            }
        },
        text = {
            if (history.isEmpty()) {
                Text(
                    text = "No previous update records found for this Oppo A6X device.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NaturalLightTextSecondary
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(history) { item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF8FAFC))
                                .border(1.dp, Color(0x33CBD5E1), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.versionName,
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                        color = NaturalLightTextPrimary
                                    )
                                    StatusBadge(text = item.releaseChannel, color = GlassPrimary)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Installed: ${Formatters.formatDate(item.installedTimestamp)}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NaturalLightTextSecondary
                                )
                                Text(
                                    text = "Build: ${item.buildNumber} · ${item.installType}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NaturalLightTextMuted
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = GlassPrimary, contentColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        }
    )
}

/**
 * Publish New Release Dialog for Oppo A6X OTA Server.
 */
@Composable
fun CreateReleaseDialog(
    onDismiss: () -> Unit,
    onPublish: (name: String, code: Int, channel: String, type: String, mb: Long, notes: String, isMandatory: Boolean) -> Unit
) {
    var versionName by remember { mutableStateOf("PowerOS 2.2.0 (Oppo A6X)") }
    var versionCodeText by remember { mutableStateOf("220") }
    var channel by remember { mutableStateOf("Stable") }
    var type by remember { mutableStateOf("Full OTA") }
    var sizeMbText by remember { mutableStateOf("1850") }
    var changelog by remember { mutableStateOf("• Official build for Oppo A6X\n• Enhanced MediaTek graphics performance\n• Android 15 August 2026 Security Bulletin") }
    var isMandatory by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "Publish OTA for Oppo A6X",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = NaturalLightTextPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = versionName,
                    onValueChange = { versionName = it },
                    label = { Text("Version Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = versionCodeText,
                        onValueChange = { versionCodeText = it },
                        label = { Text("Version Code") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = sizeMbText,
                        onValueChange = { sizeMbText = it },
                        label = { Text("Size (MB)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = channel,
                        onValueChange = { channel = it },
                        label = { Text("Channel (Stable/Beta)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = type,
                        onValueChange = { type = it },
                        label = { Text("Type (Full/Incremental)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = changelog,
                    onValueChange = { changelog = it },
                    label = { Text("Changelog Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isMandatory,
                        onCheckedChange = { isMandatory = it },
                        colors = CheckboxDefaults.colors(checkedColor = GlassPrimary)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Mark as Critical / Mandatory Update",
                        style = MaterialTheme.typography.bodySmall,
                        color = NaturalLightTextPrimary
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val code = versionCodeText.toIntOrNull() ?: 220
                    val mb = sizeMbText.toLongOrNull() ?: 1850L
                    onPublish(versionName, code, channel, type, mb, changelog, isMandatory)
                },
                colors = ButtonDefaults.buttonColors(containerColor = GlassPrimary, contentColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Publish to Repository", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(12.dp)) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Live GitHub API Inspector Dialog showing live endpoint schema and JSON payload.
 */
@Composable
fun ServerApiInspectorDialog(
    serverUrl: String,
    releases: List<OtaRelease>,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val jsonOutput = remember(releases, serverUrl) {
        val releasesJson = releases.joinToString(",\n    ") { r ->
            """
            {
              "datetime": ${r.releaseDate},
              "filename": "PowerOS-OppoA6X-v${r.versionCode}.zip",
              "id": "${r.id}",
              "romtype": "${r.releaseChannel.lowercase()}",
              "size": ${r.packageSizeBytes},
              "url": "${r.downloadUrl}",
              "version": "${r.versionName.substringAfter("PowerOS ").substringBefore(" ")}",
              "version_name": "${r.versionName}",
              "version_code": ${r.versionCode},
              "build_number": "${r.buildNumber}",
              "device": "${OtaConstants.DEVICE_MODEL_NAME}",
              "device_model": "${OtaConstants.DEVICE_MODEL_NAME}",
              "target_path": "${OtaConstants.DEFAULT_TARGET_FILE_PATH}",
              "checksum": "${r.checksumSha256}",
              "sha256": "${r.checksumSha256}",
              "security_patch": "${r.securityPatch}",
              "changelog": "${r.changelog.replace("\n", "\\n")}"
            }
            """.trimIndent()
        }

        """
        {
          "response": [
            $releasesJson
          ]
        }
        """.trimIndent()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(GlassSecondary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Code, contentDescription = null, tint = GlassSecondary, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "GitHub Raw API Inspector",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = NaturalLightTextPrimary
                    )
                }
                IconButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("JSON", jsonOutput))
                    }
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy JSON", tint = GlassSecondary)
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Live Endpoint: $serverUrl",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold),
                    color = GlassSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = jsonOutput,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        ),
                        color = Color(0xFF0F172A)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = GlassPrimary, contentColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    )
}

