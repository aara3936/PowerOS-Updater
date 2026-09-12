package com.example.ui.portal
import androidx.compose.ui.text.style.TextAlign

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.view.HapticFeedbackConstants
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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

@Composable
fun DeveloperPortalScreen(
    onExitPortal: () -> Unit,
    onPublishRelease: (OtaRelease) -> Unit,
    onPurgeReleases: () -> Unit,
    onStageLocalFile: (String, String, Long) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current

    var targetVersion by remember { mutableStateOf("2.0.0-BETA") }
    var versionCodeText by remember { mutableStateOf("200") }
    var releaseDateText by remember { mutableStateOf("2026-09-11") }
    var packageSizeText by remember { mutableStateOf("18 MB") }
    var downloadZipUrl by remember { mutableStateOf("https://github.com/aara3936/Oppo-A6X-OTA/releases/download/v3.7.90/PowerOS_v3.7.90_OppoA6X.zip") }
    var changelogText by remember {
        mutableStateOf("Power OS V2.0-BETA Release:\n• Liquid Glass UI overhaul with spring physics.\n• Real foreground download manager with resume support.\n• Native SHA-256 and AES-256 GCM security core.\n• Sideload local package staging enabled.")
    }
    var selectedChannel by remember { mutableStateOf("Stable") }

    val recentLogs = remember { mutableStateOf(AiTelemetryEngine.getRecentEvents()) }

    // System File Picker for local ZIP / APK staging
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            var fileName = "local_rom.zip"
            var fileSize = 18_874_368L
            try {
                context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    if (cursor.moveToFirst()) {
                        if (nameIndex != -1) fileName = cursor.getString(nameIndex)
                        if (sizeIndex != -1) fileSize = cursor.getLong(sizeIndex)
                    }
                }
            } catch (_: Exception) {}

            onStageLocalFile(fileName, uri.toString(), fileSize)
            onShowSnackbar("Staged '$fileName' ($fileSize bytes) directly to local OTA matrix!")
        }
    }

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

        // Section 1: Local ZIP / APK Staging
        item {
            LiquidGlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.FolderZip, contentDescription = null, tint = GlassCyanAccent)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Stage Local ROM / APK File", style = Typography.titleMedium, color = LiquidGlassTextPrimary)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Select a local .zip or .apk update package directly from device storage to test installation without uploading to a server.",
                    style = Typography.bodyMedium,
                    color = LiquidGlassTextMuted
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    LiquidGlassButton(
                        onClick = {
                            view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                            filePickerLauncher.launch("*/*")
                        },
                        modifier = Modifier.weight(1f),
                        isPrimary = true
                    ) {
                        Icon(Icons.Default.FileOpen, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Pick Local ROM File", style = Typography.labelMedium)
                    }

                    LiquidGlassButton(
                        onClick = {
                            view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                            onPurgeReleases()
                        },
                        modifier = Modifier.weight(1f),
                        isPrimary = false,
                        containerColor = Color(0x33FF3B30),
                        contentColor = GlassRose
                    ) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Purge Staged", style = Typography.labelMedium)
                    }
                }
            }
        }

        // Section 2: OTA Release Deployment Matrix
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
                        placeholder = "e.g. 2.0.0-BETA",
                        modifier = Modifier.weight(1f)
                    )

                    LiquidGlassTextField(
                        value = versionCodeText,
                        onValueChange = { versionCodeText = it },
                        label = "Version Code",
                        placeholder = "e.g. 200",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    LiquidGlassTextField(
                        value = releaseDateText,
                        onValueChange = { releaseDateText = it },
                        label = "Release Date",
                        placeholder = "2026-09-11",
                        modifier = Modifier.weight(1f)
                    )

                    LiquidGlassTextField(
                        value = packageSizeText,
                        onValueChange = { packageSizeText = it },
                        label = "Package Size",
                        placeholder = "1.8 GB or 18 MB",
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
                        val code = versionCodeText.toIntOrNull() ?: 200
                        val release = OtaRelease(
                            id = "dev_release_${selectedChannel.lowercase()}_$code",
                            deviceModel = OtaConstants.DEVICE_MODEL_NAME,
                            deviceCodename = OtaConstants.DEVICE_CODENAME,
                            versionName = targetVersion,
                            versionCode = code,
                            buildNumber = "POS-$targetVersion-${selectedChannel.uppercase()}-OppoA6X",
                            releaseChannel = selectedChannel,
                            releaseType = "Full",
                            packageSizeBytes = 18874368L,
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

        // Section 3: Custom Credential Management
        item {
            var newUsername by remember { mutableStateOf("") }
            var newPassword by remember { mutableStateOf("") }

            LiquidGlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VpnKey, contentDescription = null, tint = GlassCyanAccent)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Developer Credential Management", style = Typography.titleMedium, color = LiquidGlassTextPrimary)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Update master developer credentials securely stored with hardware-backed AES-256 GCM encryption.",
                    style = Typography.bodyMedium,
                    color = LiquidGlassTextMuted
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    LiquidGlassTextField(
                        value = newUsername,
                        onValueChange = { newUsername = it },
                        label = "New ID",
                        placeholder = "New Username",
                        modifier = Modifier.weight(1f)
                    )

                    LiquidGlassTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = "New Key",
                        placeholder = "New Password",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    LiquidGlassButton(
                        onClick = {
                            if (newUsername.isNotBlank() && newPassword.isNotBlank()) {
                                DeveloperAuthManager.updateCredentials(newUsername, newPassword)
                                newUsername = ""
                                newPassword = ""
                                onShowSnackbar("Developer credentials updated and encrypted successfully.")
                            } else {
                                onShowSnackbar("Please enter both a valid Username and Password.")
                            }
                        },
                        modifier = Modifier.weight(1f),
                        isPrimary = true
                    ) {
                        Text("Save Credentials", style = Typography.labelMedium)
                    }

                    LiquidGlassButton(
                        onClick = {
                            DeveloperAuthManager.resetCredentialsToDefault()
                            onShowSnackbar("Credentials reset to default: abx12 / abx12")
                        },
                        modifier = Modifier.weight(1f),
                        isPrimary = false
                    ) {
                        Text("Reset to Default", style = Typography.labelMedium)
                    }
                }
            }
        }

        // Section 4: AI Telemetry & Diagnostic Exporter
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
