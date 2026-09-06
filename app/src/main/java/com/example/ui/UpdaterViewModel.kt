package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.DownloadProgress
import com.example.data.model.OtaConstants
import com.example.data.model.OtaRelease
import com.example.data.model.SystemDeviceInfo
import com.example.data.model.UpdateHistoryItem
import com.example.data.repository.OtaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UpdaterUiState(
    val selectedChannel: String = "Stable", // Stable, Early Access, Closed Beta
    val isCheckingForUpdate: Boolean = false,
    val lastCheckTime: Long = System.currentTimeMillis(),
    val isDeviceDetailOpen: Boolean = false,
    val isHistoryOpen: Boolean = false,
    val isLocalInstallOpen: Boolean = false,
    val showRecoveryInstallDialog: Boolean = false,
    val activeReleaseForInstall: OtaRelease? = null,
    val snackbarMessage: String? = null
)

class UpdaterViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = OtaRepository(db.otaReleaseDao(), db.updateHistoryDao())

    val deviceInfo: StateFlow<SystemDeviceInfo> = repository.deviceInfo
    val downloadProgress: StateFlow<DownloadProgress> = repository.downloadProgress

    private val _uiState = MutableStateFlow(UpdaterUiState())
    val uiState: StateFlow<UpdaterUiState> = _uiState.asStateFlow()

    // Flow of latest release matching Oppo A6X and active selected channel
    val latestRelease: StateFlow<OtaRelease?> = _uiState
        .flatMapLatest { state ->
            repository.getLatestRelease(OtaConstants.DEVICE_MODEL_NAME, state.selectedChannel)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Update history
    val updateHistory: StateFlow<List<UpdateHistoryItem>> = repository.getUpdateHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Check if new update is available or newer than current installed version
    val isNewUpdateAvailable: StateFlow<Boolean> = combine(deviceInfo, latestRelease) { device, release ->
        release != null && (release.versionCode > device.currentVersionCode || release.versionCode >= 3000)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        // Automatically check GitHub on start
        checkForUpdates(silent = true)
    }

    fun selectChannel(channel: String) {
        // Single stable channel enforced: always Stable
        _uiState.value = _uiState.value.copy(selectedChannel = "Stable")
        checkForUpdates(silent = false)
    }

    fun checkForUpdates(silent: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCheckingForUpdate = true)
            if (!silent) delay(800)

            val fetchResult = repository.fetchFromGitHubRaw(OtaConstants.DEFAULT_RAW_JSON_URL)
            _uiState.value = _uiState.value.copy(
                isCheckingForUpdate = false,
                lastCheckTime = System.currentTimeMillis()
            )

            if (!silent) {
                if (fetchResult.isSuccess) {
                    val count = fetchResult.getOrNull()?.size ?: 0
                    if (count > 0) {
                        showSnackbar("Synced with GitHub raw manifest ($count release found)")
                    } else {
                        showSnackbar("Your Power OS is up to date")
                    }
                } else {
                    showSnackbar("Checked online server (${fetchResult.exceptionOrNull()?.message ?: "Up to date"})")
                }
            }
        }
    }

    fun startDownload(release: OtaRelease) {
        repository.startDownload(release)
    }

    fun pauseDownload() {
        repository.pauseDownload()
    }

    fun resumeDownload(release: OtaRelease) {
        repository.resumeDownload(release)
    }

    fun cancelDownload() {
        repository.cancelDownload()
    }

    fun openInstallDialog(release: OtaRelease) {
        _uiState.value = _uiState.value.copy(
            showRecoveryInstallDialog = true,
            activeReleaseForInstall = release
        )
    }

    fun closeInstallDialog() {
        _uiState.value = _uiState.value.copy(
            showRecoveryInstallDialog = false,
            activeReleaseForInstall = null
        )
    }

    fun executeSystemUpdate() {
        val release = _uiState.value.activeReleaseForInstall ?: latestRelease.value
        if (release != null) {
            repository.installUpdate(release)
        }
    }

    fun resetUpdateState() {
        repository.resetUpdateState()
        _uiState.value = _uiState.value.copy(
            showRecoveryInstallDialog = false,
            activeReleaseForInstall = null
        )
    }

    // Dialog toggles
    fun setDeviceDetailOpen(open: Boolean) {
        _uiState.value = _uiState.value.copy(isDeviceDetailOpen = open)
    }

    fun setHistoryOpen(open: Boolean) {
        _uiState.value = _uiState.value.copy(isHistoryOpen = open)
    }

    fun setLocalInstallOpen(open: Boolean) {
        _uiState.value = _uiState.value.copy(isLocalInstallOpen = open)
    }

    fun showSnackbar(message: String) {
        _uiState.value = _uiState.value.copy(snackbarMessage = message)
    }

    fun clearSnackbar() {
        _uiState.value = _uiState.value.copy(snackbarMessage = null)
    }
}
