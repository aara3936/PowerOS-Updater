package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.UpdaterViewModel
import com.example.ui.client.PowerOsClientScreen
import com.example.ui.components.GlassBackground
import com.example.ui.dialogs.*
import com.example.ui.portal.DeveloperPortalScreen
import com.example.ui.security.DeveloperAuthManager
import com.example.ui.theme.PowerOSTheme

class MainActivity : ComponentActivity() {
    private val viewModel: UpdaterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        DeveloperAuthManager.init(applicationContext)

        // Session state is preserved when app pauses or stops.
        // We removed onAppSentToBackground() to keep the developer portal open.

        setContent {
            PowerOSTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val lockoutRemaining by DeveloperAuthManager.lockoutRemainingSeconds.collectAsStateWithLifecycle()
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(uiState.snackbarMessage) {
                    uiState.snackbarMessage?.let { msg ->
                        snackbarHostState.showSnackbar(msg)
                        viewModel.clearSnackbar()
                    }
                }

                val isBlurred = uiState.isDeveloperModeActive || uiState.showAuthDialog || uiState.showLockoutDialog || uiState.showSettingsDialog || uiState.showSpecsDialog || uiState.showHistoryDialog
                GlassBackground(isBlurred = isBlurred) {
                    Scaffold(
                        containerColor = Color.Transparent,
                        snackbarHost = { SnackbarHost(snackbarHostState) }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            if (uiState.isDeveloperModeActive) {
                                DeveloperPortalScreen(
                                    onExitPortal = { viewModel.exitDeveloperPortal() },
                                    onPublishRelease = { release -> viewModel.publishCustomRelease(release) },
                                    onPurgeReleases = { viewModel.purgeStagedReleases() },
                                    onStageLocalFile = { name, uri, size -> viewModel.stageLocalFile(name, uri, size) },
                                    onShowSnackbar = { msg -> viewModel.showSnackbar(msg) }
                                )
                            } else {
                                PowerOsClientScreen(
                                    deviceInfo = uiState.deviceInfo,
                                    latestRelease = uiState.latestRelease,
                                    isUpdateAvailable = uiState.isUpdateAvailable,
                                    downloadProgress = uiState.downloadProgress,
                                    selectedChannel = uiState.selectedChannel,
                                    isChecking = uiState.isCheckingForUpdates,
                                    onCheckForUpdates = { viewModel.checkForUpdates() },
                                    onStartDownload = { release -> viewModel.startDownload(release) },
                                    onPauseDownload = { viewModel.pauseDownload() },
                                    onResumeDownload = { release -> viewModel.resumeDownload(release) },
                                    onInstallUpdate = { release -> viewModel.installUpdate(release) },
                                    onOpenSettings = { viewModel.setShowSettingsDialog(true) },
                                    onOpenSpecs = { viewModel.setShowSpecsDialog(true) },
                                    onOpenHistory = { viewModel.setShowHistoryDialog(true) },
                                    onShowSnackbar = { msg -> viewModel.showSnackbar(msg) }
                                )
                            }

                            // Dialogs
                            if (uiState.showAuthDialog) {
                                DeveloperAuthDialog(
                                    onDismiss = { viewModel.dismissAuthDialog() },
                                    onAuthenticated = { viewModel.onAuthenticated() }
                                )
                            }

                            if (uiState.showLockoutDialog) {
                                SecurityLockoutDialog(
                                    remainingSeconds = lockoutRemaining,
                                    onDismiss = { viewModel.setShowLockoutDialog(false) }
                                )
                            }

                            if (uiState.showSettingsDialog) {
                                SettingsDialog(
                                    currentVersion = uiState.deviceInfo.currentOsVersion,
                                    currentChannel = uiState.selectedChannel,
                                    rawJsonUrl = uiState.deviceInfo.rawJsonSource,
                                    targetSavePath = uiState.deviceInfo.targetSavePath,
                                    onChannelSelected = { channel -> viewModel.setChannel(channel) },
                                    onSaveUrlChanged = { url -> viewModel.setCustomSourceUrl(url) },
                                    onSavePathChanged = { path -> viewModel.setTargetSavePath(path) },
                                    onDeveloperTriggered = { viewModel.onVersionTapped() },
                                    onDismiss = { viewModel.setShowSettingsDialog(false) }
                                )
                            }

                            if (uiState.showSpecsDialog) {
                                DeviceSpecsDialog(
                                    deviceInfo = uiState.deviceInfo,
                                    onDismiss = { viewModel.setShowSpecsDialog(false) }
                                )
                            }

                            if (uiState.showHistoryDialog) {
                                UpdateHistoryDialog(
                                    history = uiState.updateHistory,
                                    onDismiss = { viewModel.setShowHistoryDialog(false) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
