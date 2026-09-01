package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.OtaConstants
import com.example.data.model.OtaRelease
import com.example.ui.AppSubstance
import com.example.ui.UpdaterViewModel
import com.example.ui.client.PowerOsClientScreen
import com.example.ui.dialogs.CreateReleaseDialog
import com.example.ui.dialogs.DeviceSpecsDialog
import com.example.ui.dialogs.LocalPackageInstallDialog
import com.example.ui.dialogs.RecoveryInstallDialog
import com.example.ui.dialogs.ServerApiInspectorDialog
import com.example.ui.dialogs.UpdateHistoryDialog
import com.example.ui.server.OtaServerScreen
import com.example.ui.theme.GlassEmerald
import com.example.ui.theme.GlassPrimary
import com.example.ui.theme.GlassSecondary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NaturalLightBackground
import com.example.ui.theme.NaturalLightTextMuted
import com.example.ui.theme.NaturalLightTextPrimary
import com.example.ui.theme.NaturalLightTextSecondary

class MainActivity : ComponentActivity() {
    private val viewModel: UpdaterViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val deviceInfo by viewModel.deviceInfo.collectAsStateWithLifecycle()
                val latestRelease by viewModel.latestRelease.collectAsStateWithLifecycle()
                val allReleases by viewModel.allReleases.collectAsStateWithLifecycle()
                val updateHistory by viewModel.updateHistory.collectAsStateWithLifecycle()
                val isNewUpdateAvailable by viewModel.isNewUpdateAvailable.collectAsStateWithLifecycle()
                val downloadProgress by viewModel.downloadProgress.collectAsStateWithLifecycle()
                val serverUrl by viewModel.serverUrl.collectAsStateWithLifecycle()

                val snackbarHostState = remember { SnackbarHostState() }
                var showAboutDialog by remember { mutableStateOf(false) }

                LaunchedEffect(uiState.snackbarMessage) {
                    uiState.snackbarMessage?.let { msg ->
                        snackbarHostState.showSnackbar(msg)
                        viewModel.clearSnackbar()
                    }
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    containerColor = NaturalLightBackground,
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(NaturalLightBackground)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            // Main App Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                Brush.linearGradient(
                                                    listOf(
                                                        GlassPrimary.copy(alpha = 0.16f),
                                                        GlassSecondary.copy(alpha = 0.12f)
                                                    )
                                                )
                                            )
                                            .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(12.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PowerSettingsNew,
                                            contentDescription = null,
                                            tint = GlassPrimary,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Power OS",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = NaturalLightTextPrimary
                                        )
                                        Text(
                                            text = "${OtaConstants.DEVICE_MODEL_NAME} · GitHub OTA",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                            color = GlassPrimary
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { showAboutDialog = true },
                                        modifier = Modifier.testTag("about_app_btn")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = "About Power OS",
                                            tint = NaturalLightTextSecondary
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Dual Substance Switcher (Client Updater vs GitHub Server Portal)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White.copy(alpha = 0.8f))
                                    .border(1.dp, Color(0x33CBD5E1), RoundedCornerShape(16.dp))
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                val isClient = uiState.selectedSubstance == AppSubstance.CLIENT_UPDATER
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isClient) {
                                                Brush.horizontalGradient(
                                                    listOf(Color(0xFF0284C7), Color(0xFF38BDF8))
                                                )
                                            } else {
                                                Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                                            }
                                        )
                                        .clickable { viewModel.selectSubstance(AppSubstance.CLIENT_UPDATER) }
                                        .padding(vertical = 10.dp)
                                        .testTag("substance_client_tab"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.PhoneAndroid,
                                            contentDescription = null,
                                            tint = if (isClient) Color.White else NaturalLightTextSecondary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Oppo A6X Updater",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = if (isClient) FontWeight.Bold else FontWeight.Medium
                                            ),
                                            color = if (isClient) Color.White else NaturalLightTextSecondary
                                        )
                                    }
                                }

                                val isServer = uiState.selectedSubstance == AppSubstance.OTA_SERVER
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isServer) {
                                                Brush.horizontalGradient(
                                                    listOf(Color(0xFF4F46E5), Color(0xFF818CF8))
                                                )
                                            } else {
                                                Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                                            }
                                        )
                                        .clickable { viewModel.selectSubstance(AppSubstance.OTA_SERVER) }
                                        .padding(vertical = 10.dp)
                                        .testTag("substance_server_tab"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.CloudDone,
                                            contentDescription = null,
                                            tint = if (isServer) Color.White else NaturalLightTextSecondary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "GitHub OTA Portal",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = if (isServer) FontWeight.Bold else FontWeight.Medium
                                            ),
                                            color = if (isServer) Color.White else NaturalLightTextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AnimatedContent(
                            targetState = uiState.selectedSubstance,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "substance_switch"
                        ) { substance ->
                            when (substance) {
                                AppSubstance.CLIENT_UPDATER -> {
                                    PowerOsClientScreen(
                                        deviceInfo = deviceInfo,
                                        latestRelease = latestRelease,
                                        isNewUpdateAvailable = isNewUpdateAvailable,
                                        downloadProgress = downloadProgress,
                                        isChecking = uiState.isCheckingForUpdate,
                                        lastCheckTime = uiState.lastCheckTime,
                                        selectedChannel = uiState.selectedChannel,
                                        onSelectChannel = { viewModel.selectChannel(it) },
                                        onCheckForUpdates = { viewModel.checkForUpdates() },
                                        onStartDownload = { viewModel.startDownload(it) },
                                        onPauseDownload = { viewModel.pauseDownload() },
                                        onResumeDownload = { viewModel.resumeDownload(it) },
                                        onCancelDownload = { viewModel.cancelDownload() },
                                        onOpenInstallDialog = { viewModel.openInstallDialog(it) },
                                        onOpenDeviceSpecs = { viewModel.setDeviceDetailOpen(true) },
                                        onOpenHistory = { viewModel.setHistoryOpen(true) },
                                        onOpenLocalInstall = { viewModel.setLocalInstallOpen(true) }
                                    )
                                }

                                AppSubstance.OTA_SERVER -> {
                                    OtaServerScreen(
                                        releases = allReleases,
                                        serverUrl = serverUrl,
                                        onOpenCreateRelease = { viewModel.setCreateReleaseOpen(true) },
                                        onOpenApiInspector = { viewModel.setServerApiInspectorOpen(true) },
                                        onToggleStatus = { viewModel.toggleReleaseStatus(it) },
                                        onDeleteRelease = { viewModel.deleteRelease(it) },
                                        onQuickPublishPreset = { name, code, ch, type, mb, notes ->
                                            viewModel.publishNewRelease(name, code, ch, type, mb, notes, false)
                                        },
                                        onUpdateServerUrl = { viewModel.updateRawJsonUrl(it) }
                                    )
                                }
                            }
                        }
                    }
                }

                // Modal Dialogs
                if (uiState.isDeviceDetailOpen) {
                    DeviceSpecsDialog(
                        deviceInfo = deviceInfo,
                        onDismiss = { viewModel.setDeviceDetailOpen(false) }
                    )
                }

                if (uiState.isHistoryOpen) {
                    UpdateHistoryDialog(
                        history = updateHistory,
                        onDismiss = { viewModel.setHistoryOpen(false) }
                    )
                }

                if (uiState.isLocalInstallOpen) {
                    LocalPackageInstallDialog(
                        onDismiss = { viewModel.setLocalInstallOpen(false) },
                        onFlashLocalZip = { path ->
                            viewModel.publishNewRelease(
                                versionName = "PowerOS 2.2.0 (Local Sideload)",
                                versionCode = 220,
                                channel = "Local",
                                type = "Full ROM Zip",
                                sizeMb = 1880L,
                                changelog = "• Sideloaded package from $path into Oppo A6X storage slot.\n• Verified checksum & signature.",
                                isMandatory = false
                            )
                        }
                    )
                }

                if (uiState.showRecoveryInstallDialog) {
                    RecoveryInstallDialog(
                        release = uiState.activeReleaseForInstall ?: latestRelease,
                        progress = downloadProgress,
                        onConfirmInstall = { viewModel.executeSystemUpdate() },
                        onReset = { viewModel.resetUpdateState() },
                        onDismiss = { viewModel.closeInstallDialog() }
                    )
                }

                if (uiState.isCreateReleaseOpen) {
                    CreateReleaseDialog(
                        onDismiss = { viewModel.setCreateReleaseOpen(false) },
                        onPublish = { name, code, ch, type, mb, changelog, isMandatory ->
                            viewModel.publishNewRelease(name, code, ch, type, mb, changelog, isMandatory)
                        }
                    )
                }

                if (uiState.isServerApiInspectorOpen) {
                    ServerApiInspectorDialog(
                        serverUrl = serverUrl,
                        releases = allReleases,
                        onDismiss = { viewModel.setServerApiInspectorOpen(false) }
                    )
                }

                if (showAboutDialog) {
                    AlertDialog(
                        onDismissRequest = { showAboutDialog = false },
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
                                    Icon(imageVector = Icons.Default.PowerSettingsNew, contentDescription = null, tint = GlassPrimary, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("About Power OS", color = NaturalLightTextPrimary, fontWeight = FontWeight.Bold)
                            }
                        },
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Power OS OTA Updater (Oppo A6X)",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = NaturalLightTextPrimary
                                )
                                Text(
                                    text = "Live parameters configured:\n• Raw JSON: ${OtaConstants.DEFAULT_RAW_JSON_URL}\n• Target Directory: ${OtaConstants.DEFAULT_TARGET_FILE_PATH}\n• Device Model: ${OtaConstants.DEVICE_MODEL_NAME}\n\nFeatures 1-click update checks, real streaming download to /sdcard/Download/OTA/rom.zip, SHA-256 integrity verification, and Oppo A6X recovery flashing.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NaturalLightTextSecondary
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = { showAboutDialog = false },
                                colors = ButtonDefaults.buttonColors(containerColor = GlassPrimary, contentColor = Color.White),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("OK", fontWeight = FontWeight.Bold)
                            }
                        }
                    )
                }
            }
        }
    }
}

