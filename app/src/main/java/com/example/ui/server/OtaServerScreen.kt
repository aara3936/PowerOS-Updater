package com.example.ui.server

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OtaConstants
import com.example.data.model.OtaRelease
import com.example.ui.components.Formatters
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
fun OtaServerScreen(
    releases: List<OtaRelease>,
    serverUrl: String,
    onOpenCreateRelease: () -> Unit,
    onOpenApiInspector: () -> Unit,
    onToggleStatus: (OtaRelease) -> Unit,
    onDeleteRelease: (String) -> Unit,
    onQuickPublishPreset: (name: String, code: Int, channel: String, type: String, mb: Long, notes: String) -> Unit,
    onUpdateServerUrl: (String) -> Unit
) {
    val context = LocalContext.current
    var isEditingUrl by remember { mutableStateOf(false) }
    var currentInputUrl by remember { mutableStateOf(serverUrl) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Server Header & Live GitHub API Endpoint Card (Liquid Glass)
        item {
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
                                                GlassSecondary.copy(alpha = 0.18f),
                                                GlassPrimary.copy(alpha = 0.12f)
                                            )
                                        )
                                    )
                                    .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudDone,
                                    contentDescription = null,
                                    tint = GlassSecondary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Oppo A6X GitHub Server",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = NaturalLightTextPrimary
                                )
                                Text(
                                    text = "Target: ${OtaConstants.DEVICE_MODEL_NAME} (${OtaConstants.DEVICE_CODENAME})",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = GlassSecondary
                                )
                            }
                        }

                        StatusBadge(text = "LIVE SYNC", color = GlassEmerald, isPulsing = true)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Raw GitHub Updater Manifest URL:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = NaturalLightTextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    if (isEditingUrl) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = currentInputUrl,
                                onValueChange = { currentInputUrl = it },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace)
                            )
                            Button(
                                onClick = {
                                    onUpdateServerUrl(currentInputUrl)
                                    isEditingUrl = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GlassPrimary, contentColor = Color.White),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Save")
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.7f))
                                .border(1.dp, Color(0x33CBD5E1), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = serverUrl,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = GlassSecondary,
                                modifier = Modifier.weight(1f)
                            )
                            Row {
                                IconButton(
                                    onClick = { isEditingUrl = true },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit URL", tint = GlassSecondary, modifier = Modifier.size(16.dp))
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        clipboard.setPrimaryClip(ClipData.newPlainText("OTA URL", serverUrl))
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy URL", tint = GlassSecondary, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Target Save Path Note
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Folder, contentDescription = null, tint = GlassSecondary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Target download: /sdcard/Download/OTA/rom.zip",
                            style = MaterialTheme.typography.labelSmall,
                            color = NaturalLightTextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onOpenCreateRelease,
                            modifier = Modifier
                                .weight(1f)
                                .shadow(
                                    elevation = 3.dp,
                                    shape = RoundedCornerShape(12.dp),
                                    ambientColor = Color(0x150284C7),
                                    spotColor = GlassPrimary.copy(alpha = 0.3f)
                                )
                                .testTag("publish_new_release_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = GlassPrimary, contentColor = Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Publish Release", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onOpenApiInspector,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("inspect_api_btn"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Code, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("API Inspector")
                        }
                    }
                }
            }
        }

        // Quick Deploy Presets for Oppo A6X (Liquid Glass)
        item {
            LiquidGlassCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = 14.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Quick Deploy Presets for Oppo A6X",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = NaturalLightTextPrimary
                        )
                        Icon(imageVector = Icons.Default.RocketLaunch, contentDescription = null, tint = GlassSecondary, modifier = Modifier.size(18.dp))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PresetGlassChip(
                            title = "v2.2.0 Stable",
                            subtitle = "Oppo A6X Full",
                            color = GlassPrimary,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onQuickPublishPreset(
                                    "PowerOS 2.2.0 (Oppo A6X Ultra)",
                                    220,
                                    "Stable",
                                    "Full OTA",
                                    1920L,
                                    "• Next-Gen PowerOS 2.2 for Oppo A6X\n• 90Hz frame pacing & thermal optimizations\n• Android 15 August 2026 Security Patch"
                                )
                            }
                        )

                        PresetGlassChip(
                            title = "v2.3.0 Beta",
                            subtitle = "Incremental 450MB",
                            color = GlassAmber,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onQuickPublishPreset(
                                    "PowerOS 2.3.0-Beta (Nova)",
                                    230,
                                    "Beta",
                                    "Incremental Patch",
                                    450L,
                                    "• Beta test of new MediaTek GPU drivers\n• Spatial Audio & dynamic lockscreen"
                                )
                            }
                        )

                        PresetGlassChip(
                            title = "Sec Patch",
                            subtitle = "Sept 2026",
                            color = GlassEmerald,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onQuickPublishPreset(
                                    "PowerOS 2.1.1 Security Update",
                                    211,
                                    "Stable",
                                    "Incremental Patch",
                                    180L,
                                    "• Critical security bulletin fixes\n• Bluetooth LE stability fix"
                                )
                            }
                        )
                    }
                }
            }
        }

        // Active Repository Releases List
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Published Releases (${releases.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = NaturalLightTextPrimary
                )
                Text(
                    text = "Oppo A6X",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = GlassPrimary
                )
            }
        }

        items(releases, key = { it.id }) { release ->
            OtaReleaseItemGlassCard(
                release = release,
                onToggleStatus = { onToggleStatus(release) },
                onDelete = { onDeleteRelease(release.id) }
            )
        }
    }
}

@Composable
private fun PresetGlassChip(
    title: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.10f))
            .border(1.dp, color.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = color)
            Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = NaturalLightTextMuted)
        }
    }
}

@Composable
private fun OtaReleaseItemGlassCard(
    release: OtaRelease,
    onToggleStatus: () -> Unit,
    onDelete: () -> Unit
) {
    val isPublished = release.status == "PUBLISHED"
    LiquidGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("release_card_${release.versionCode}"),
        shape = RoundedCornerShape(18.dp),
        contentPadding = 14.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusBadge(text = release.releaseChannel, color = if (release.releaseChannel == "Stable") GlassPrimary else GlassAmber)
                    Spacer(modifier = Modifier.width(6.dp))
                    StatusBadge(text = release.releaseType, color = GlassSecondary)
                    Spacer(modifier = Modifier.width(6.dp))
                    StatusBadge(text = release.deviceModel, color = GlassEmerald)
                }

                StatusBadge(
                    text = release.status,
                    color = if (isPublished) GlassEmerald else GlassRose
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = release.versionName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = NaturalLightTextPrimary
            )

            Text(
                text = "Build: ${release.buildNumber}",
                style = MaterialTheme.typography.bodySmall,
                color = NaturalLightTextSecondary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Size: ${Formatters.formatBytes(release.packageSizeBytes)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = NaturalLightTextSecondary
                )
                Text(
                    text = "Released: ${Formatters.formatDate(release.releaseDate)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = NaturalLightTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SHA256: ${release.checksumSha256.take(12)}...",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = FontFamily.Monospace,
                    color = NaturalLightTextSecondary
                )

                Row {
                    IconButton(
                        onClick = onToggleStatus,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = if (isPublished) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                            contentDescription = "Toggle status",
                            tint = if (isPublished) GlassAmber else GlassEmerald
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete release",
                            tint = GlassRose
                        )
                    }
                }
            }
        }
    }
}

