package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.ui.UpdaterViewModel
import com.example.ui.client.PowerOsClientScreen
import com.example.ui.components.GlassBackground
import com.example.ui.dialogs.DeviceSpecsDialog
import com.example.ui.dialogs.LocalPackageInstallDialog
import com.example.ui.dialogs.RecoveryInstallDialog
import com.example.ui.dialogs.UpdateHistoryDialog
import com.example.ui.theme.GlassEmerald
import com.example.ui.theme.GlassPrimary
import com.example.ui.theme.GlassSecondary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NaturalLightBackground
import com.example.ui.theme.NaturalLightCardBackground
import com.example.ui.theme.NaturalLightTextPrimary
import com.example.ui.theme.NaturalLightTextSecondary

class MainActivity : ComponentActivity() {
    private val viewModel: UpdaterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val deviceInfo by viewModel.deviceInfo.collectAsStateWithLifecycle()
                val latestRelease by viewModel.latestRelease.collectAsStateWithLifecycle()
                val updateHistory by viewModel.updateHistory.collectAsStateWithLifecycle()
                val isNewUpdateAvailable by viewModel.isNewUpdateAvailable.collectAsStateWithLifecycle()
                val downloadProgress by viewModel.downloadProgress.collectAsStateWithLifecycle()

                val snackbarHostState = remember { SnackbarHostState() }
                var showAboutDialog by remember { mutableStateOf(false) }

                LaunchedEffect(uiState.snackbarMessage) {
                    uiState.snackbarMessage?.let { msg ->
                        snackbarHostState.showSnackbar(msg)
                        viewModel.clearSnackbar()
                    }
                }

                // Night-Field Wallpaper & Liquid Glass Architecture
                GlassBackground(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .navigationBarsPadding(),
                        containerColor = Color.Transparent,
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        topBar = {
                            // Clean Glass Header
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(RoundedCornerShape(28.dp))
                                            .background(
                                                Brush.linearGradient(
                                                    listOf(
                                                        GlassPrimary.copy(alpha = 0.22f),
                                                        GlassSecondary.copy(alpha = 0.16f)
                                                    )
                                                )
                                            )
                                            .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(28.dp)),
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
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 0.4.sp
                                            ),
                                            color = NaturalLightTextPrimary
                                        )
                                        Text(
                                            text = "${OtaConstants.DEVICE_MODEL_NAME} · System Updater",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                            color = GlassPrimary
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { viewModel.checkForUpdates(silent = false) },
                                        modifier = Modifier.testTag("top_refresh_btn")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Refresh,
                                            contentDescription = "Refresh",
                                            tint = NaturalLightTextSecondary
                                        )
                                    }

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
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
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
                                onOpenLocalInstall = { viewModel.setLocalInstallOpen(true) },
                                onResetUpdateState = { viewModel.resetUpdateState() }
                            )
                        }
                    }
                }

                // Modal Dialogs (All with 28dp rounded shapes & Glassmorphism)
                if (uiState.isDeviceDetailOpen) {
                    DeviceSpecsDialog(
                        deviceInfo = deviceInfo,
                        onDismiss = { viewModel.setDeviceDetailOpen(false) }
                    )
                }

                if (uiState.isHistoryOpen) {
                    UpdateHistoryDialog(
                        historyItems = updateHistory,
                        onDismiss = { viewModel.setHistoryOpen(false) }
                    )
                }

                if (uiState.isLocalInstallOpen) {
                    LocalPackageInstallDialog(
                        targetPath = OtaConstants.DEFAULT_TARGET_FILE_PATH,
                        onDismiss = { viewModel.setLocalInstallOpen(false) },
                        onInstallLocalFile = {
                            viewModel.showSnackbar("Local package queued at ${OtaConstants.DEFAULT_TARGET_FILE_PATH}")
                        }
                    )
                }

                if (uiState.showRecoveryInstallDialog) {
                    RecoveryInstallDialog(
                        release = uiState.activeReleaseForInstall ?: latestRelease,
                        onConfirmInstall = { viewModel.executeSystemUpdate() },
                        onDismiss = { viewModel.closeInstallDialog() }
                    )
                }

                if (showAboutDialog) {
                    AlertDialog(
                        onDismissRequest = { showAboutDialog = false },
                        containerColor = NaturalLightCardBackground,
                        shape = RoundedCornerShape(28.dp),
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(GlassPrimary.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PowerSettingsNew,
                                        contentDescription = null,
                                        tint = GlassPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "About Power OS",
                                    color = NaturalLightTextPrimary,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        },
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Power OS System Updater for Oppo A6X",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = NaturalLightTextPrimary
                                )
                                Text(
                                    text = "• Channel: Power OS Stable\n• Device Model: ${OtaConstants.DEVICE_MODEL_NAME}\n• Local Target: ${OtaConstants.DEFAULT_TARGET_FILE_PATH}\n\nFeatures live manifest synchronization, real HTTP streaming ROM downloads with SHA-256 verification, and Oppo recovery package flashing.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NaturalLightTextSecondary
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = { showAboutDialog = false },
                                colors = ButtonDefaults.buttonColors(containerColor = GlassPrimary),
                                shape = RoundedCornerShape(28.dp)
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
