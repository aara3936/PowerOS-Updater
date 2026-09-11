package com.example.ui

import com.example.data.model.DownloadProgress
import com.example.data.model.OtaRelease
import com.example.data.model.SystemDeviceInfo
import com.example.data.model.UpdateHistoryItem

data class UpdaterUiState(
    val deviceInfo: SystemDeviceInfo = SystemDeviceInfo(),
    val latestRelease: OtaRelease? = null,
    val isUpdateAvailable: Boolean = false,
    val downloadProgress: DownloadProgress = DownloadProgress(),
    val updateHistory: List<UpdateHistoryItem> = emptyList(),
    val selectedChannel: String = "Stable",
    val isCheckingForUpdates: Boolean = false,
    val showSettingsDialog: Boolean = false,
    val showSpecsDialog: Boolean = false,
    val showHistoryDialog: Boolean = false,
    val showAuthDialog: Boolean = false,
    val showLockoutDialog: Boolean = false,
    val isDeveloperModeActive: Boolean = false,
    val snackbarMessage: String? = null
)
