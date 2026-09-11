package com.example.ui.portal

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OtaConstants
import com.example.data.model.OtaRelease
import com.example.telemetry.AiTelemetryEngine
import com.example.ui.components.*
import com.example.ui.security.DeveloperAuthManager
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DeveloperPortalScreen(
    onExitPortal: () -> Unit,
    onPublishRelease: (OtaRelease) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current
    val coroutineScope = rememberCoroutineScope()

    var targetVersion by remember { mutableStateOf("3.7") }
    var versionCodeText by remember { mutableStateOf("3790") }
    var releaseDateText by remember { mutableStateOf("2026-09-03") }
    var packageSizeText by remember { mutableStateOf("15 MB") }
    var downloadZipUrl by remember { mutableStateOf("https://github.com/aara3936/Oppo-A6X-OTA/releases/download/v3.7.90/PowerOS_v3.7.90_OppoA6X.zip") }
    var changelogText by remember {
        mutableStateOf("Power OS V3.7 Stable Release:\n• Full transition to Liquid Glass UI architecture.\n• Improved CPU & GPU scheduling for Oppo A6X.\n• Enhanced system stability and adaptive smooth refresh rate.")
    }
    var selectedChannel by remember { mutableStateOf("Stable") }

    val recentLogs = remember { mutableStateOf(AiTelemetryEngine.getRecentEvents()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 48.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Portal Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Terminal, contentDescription = null, tint = GlassCyanAccent, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Developer Control Portal", style = Typography.titleLarge, color = LiquidGlassTextPrimary)
                    }
                    Text("Power OS Engineering & Payload Staging", style = Typography.labelMedium, color = LiquidGlassTextMuted)
                }

                LiquidGlassButton(
                    onClick = {
                        DeveloperAuthManager.purgeSession()
                        onExitPortal()
                    },
                    modifier = Modifier.height(44.dp),
                    isPrimary = false
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Lock & Exit", fontSize = 13.sp)
                }
            }
        }

        // Section 1: OTA Release Deployment Matrix
        item {
            LiquidGlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, tint = GlassCyanAccent)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Stage & Publish OTA Release", style = Typography.titleMedium, color = LiquidGlassTextPrimary)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    LiquidGlassTextField(
                        value = targetVersion,
                        onValueChange = { targetVersion = it },
                        label = "Version String",
                        placeholder = "e.g. 3.7",
                        modifier = Modifier.weight(1f)
                    )

                    LiquidGlassTextField(
                        value = versionCodeText,
                        onValueChange = { versionCodeText = it },
                        label = "Version Code",
                        placeholder = "e.g. 3790",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    LiquidGlassTextField(
                        value = releaseDateText,
                        onValueChange = { releaseDateText = it },
                        label = "Release Date",
                        placeholder = "2026-09-03",
                        modifier = Modifier.weight(1f)
                    )

                    LiquidGlassTextField(
                        value = packageSizeText,
                        onValueChange = { packageSizeText = it },
                        label = "Package Size",
                        placeholder = "1.8 GB or 15 MB",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LiquidGlassTextField(
                    value = downloadZipUrl,
                    onValueChange = { downloadZipUrl = it },
                    label = "Direct Binary Download ZIP URL",
                    placeholder = "https://github.com/.../release.zip"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Release Channel selector
                Text("Target Channel", style = Typography.labelMedium, color = LiquidGlassTextMuted)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Stable", "Beta", "Developer").forEach { ch ->
                        val active = selectedChannel.equals(ch, ignoreCase = true)
                        Button(
                            onClick = { selectedChannel = ch },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (active) GlassCyanAccent else LiquidGlassFill,
                                contentColor = if (active) PowerOnPrimary else LiquidGlassTextPrimary
                            )
                        ) {
                            Text(ch, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LiquidGlassTextField(
                    value = changelogText,
                    onValueChange = { changelogText = it },
                    label = "Markdown / Plaintext Changelog",
                    singleLine = false,
                    maxLines = 5,
                    placeholder = "Describe changes, optimizations, and new features..."
                )

                Spacer(modifier = Modifier.height(20.dp))

                LiquidGlassButton(
                    onClick = {
                        view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                        val code = versionCodeText.toIntOrNull() ?: 3790
                        val release = OtaRelease(
                            id = "dev_release_${selectedChannel.lowercase()}_$code",
                            deviceModel = OtaConstants.DEVICE_MODEL_NAME,
                            deviceCodename = OtaConstants.DEVICE_CODENAME,
                            versionName = targetVersion,
                            versionCode = code,
                            buildNumber = "POS-$targetVersion-${selectedChannel.uppercase()}-OppoA6X",
                            releaseChannel = selectedChannel,
                            releaseType = "Full",
                            packageSizeBytes = 15728640L,
                            downloadUrl = downloadZipUrl,
                            checksumSha256 = "",
                            androidVersion = "Android 14",
                            securityPatch = "2026-09-01",
                            changelog = changelogText,
                            releaseDate = System.currentTimeMillis(),
                            sourceUrl = OtaConstants.DEFAULT_RAW_JSON_URL,
                            targetLocalPath = OtaConstants.DEFAULT_TARGET_FILE_PATH
                        )

                        onPublishRelease(release)
                        onShowSnackbar("OTA Release $targetVersion ($selectedChannel) published to device database!")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isPrimary = true
                ) {
                    Icon(Icons.Default.Publish, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Publish OTA Update to Device", style = Typography.labelLarge)
                }
            }
        }

        // Section 2: AI Telemetry & Diagnostic Exporter
        item {
            LiquidGlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.BugReport, contentDescription = null, tint = GlassRose)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("System Diagnostic Logs & AI Healing", style = Typography.titleMedium, color = LiquidGlassTextPrimary)
                    }

                    IconButton(onClick = {
                        recentLogs.value = AiTelemetryEngine.getRecentEvents()
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = GlassCyanAccent)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Intercepted unhandled events, network timeouts, and JSON mismatches stored in encrypted ring buffer.",
                    style = Typography.bodyMedium,
                    color = LiquidGlassTextMuted
                )

                Spacer(modifier = Modifier.height(16.dp))

                // One-Click AI Diagnostic Prompt Exporter Button
                LiquidGlassButton(
                    onClick = {
                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        val prompt = AiTelemetryEngine.generateAiDiagnosticPrompt()
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("AI Diagnostic Prompt", prompt)
                        clipboard.setPrimaryClip(clip)

                        onShowSnackbar("Diagnostic log prompt copied to clipboard! Ready to paste to AI.")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isPrimary = false,
                    containerColor = Color(0x3300E5FF),
                    contentColor = GlassCyanAccent
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Copy Diagnostic Log Prompt", style = Typography.labelLarge)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Event List
                if (recentLogs.value.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(LiquidGlassFill)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Ring buffer clean. All systems nominal.", style = Typography.bodyMedium, color = GlassEmerald)
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        recentLogs.value.take(5).forEach { event ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(LiquidGlassFill)
                                    .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(event.type, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (event.type == "CRASH") GlassRose else GlassAmber)
                                    Text(if (event.isRecovered) "Self-Healed" else "Pending", fontSize = 11.sp, color = GlassEmerald)
                                }
                                Text(event.summary, style = Typography.bodyMedium, color = LiquidGlassTextPrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}
