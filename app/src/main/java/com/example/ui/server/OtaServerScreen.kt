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
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
    onOpenApiInspector: () -> Unit,
    onRefreshSync: () -> Unit,
    onDeleteRelease: (String) -> Unit,
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
        // 1. GitHub Server & Manifest Endpoint Header Card
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
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                GlassSecondary.copy(alpha = 0.20f),
                                                GlassPrimary.copy(alpha = 0.12f)
                                            )
                                        )
                                    )
                                    .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(16.dp)),
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
                                    text = "GitHub OTA Portal",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = NaturalLightTextPrimary
                                )
                                Text(
                                    text = "Target: ${OtaConstants.DEVICE_MODEL_NAME}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = GlassSecondary
                                )
                            }
                        }

                        StatusBadge(text = "LIVE SYNC", color = GlassEmerald, isPulsing = true)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Metadata Source Endpoint",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = NaturalLightTextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    if (!isEditingUrl) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.75f))
                                .border(1.dp, Color(0x33CBD5E1), RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = serverUrl,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = GlassPrimary,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        clipboard.setPrimaryClip(ClipData.newPlainText("OTA URL", serverUrl))
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy URL",
                                        tint = NaturalLightTextSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { isEditingUrl = true },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit URL",
                                        tint = GlassPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = currentInputUrl,
                                onValueChange = { currentInputUrl = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GlassPrimary,
                                    unfocusedBorderColor = Color(0x40CBD5E1),
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White.copy(alpha = 0.8f)
                                ),
                                textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                                maxLines = 3
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        currentInputUrl = serverUrl
                                        isEditingUrl = false
                                    },
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Text("Cancel")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        onUpdateServerUrl(currentInputUrl)
                                        isEditingUrl = false
                                    },
                                    shape = RoundedCornerShape(20.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = GlassPrimary)
                                ) {
                                    Text("Save & Sync", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onRefreshSync,
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GlassSecondary),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Sync GitHub", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onOpenApiInspector,
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Raw Payload")
                        }
                    }
                }
            }
        }

        // 2. Active Manifest Releases Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Live Manifest Releases",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = NaturalLightTextPrimary
                )
                Text(
                    text = "${releases.size} items",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = GlassSecondary
                )
            }
        }

        // 3. Live Release Cards or Clean Empty State (No Mock Lists!)
        if (releases.isEmpty()) {
            item {
                LiquidGlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDone,
                            contentDescription = null,
                            tint = NaturalLightTextMuted,
                            modifier = Modifier.size(42.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No Releases in Local Cache",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = NaturalLightTextPrimary
                        )
                        Text(
                            text = "Tap 'Sync GitHub' above to fetch the latest manifest from online metadata.json.",
                            style = MaterialTheme.typography.bodySmall,
                            color = NaturalLightTextSecondary
                        )
                    }
                }
            }
        } else {
            items(releases) { release ->
                LiquidGlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = release.versionName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = NaturalLightTextPrimary
                                )
                                Text(
                                    text = "Target: ${release.deviceModel} · vCode ${release.versionCode}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GlassPrimary
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                StatusBadge(
                                    text = release.releaseChannel.uppercase(),
                                    color = if (release.releaseChannel == "Stable") GlassEmerald else GlassSecondary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                IconButton(
                                    onClick = { onDeleteRelease(release.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Delete",
                                        tint = GlassRose,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Zip Download URL:",
                            style = MaterialTheme.typography.labelSmall,
                            color = NaturalLightTextMuted
                        )
                        Text(
                            text = release.downloadUrl,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Medium
                            ),
                            color = NaturalLightTextSecondary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Target Filepath: ${release.targetLocalPath}",
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                            color = GlassPrimary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.70f))
                                .border(1.dp, Color(0x26CBD5E1), RoundedCornerShape(16.dp))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = release.changelog,
                                style = MaterialTheme.typography.bodySmall,
                                color = NaturalLightTextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
