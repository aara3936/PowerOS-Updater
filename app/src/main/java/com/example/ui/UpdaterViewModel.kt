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

enum class AppSubstance {
    CLIENT_UPDATER, // Power OS System Updater for Oppo A6X
    OTA_SERVER      // GitHub OTA Release Portal for Oppo A6X
}

data class UpdaterUiState(
    val selectedSubstance: AppSubstance = AppSubstance.CLIENT_UPDATER,
    val selectedChannel: String = "Stable", // Stable, Beta, Developer Preview
    val isCheckingForUpdate: Boolean = false,
    val lastCheckTime: Long = System.currentTimeMillis(),
    val rawJsonUrl: String = OtaConstants.DEFAULT_RAW_JSON_URL,
    val targetRomPath: String = OtaConstants.DEFAULT_TARGET_FILE_PATH,
    val isDeviceDetailOpen: Boolean = false,
    val isHistoryOpen: Boolean = false,
    val isLocalInstallOpen: Boolean = false,
    val isCreateReleaseOpen: Boolean = false,
    val isServerApiInspectorOpen: Boolean = false,
    val showRecoveryInstallDialog: Boolean = false,
    val installSuccessBanner: Boolean = false,
    val activeReleaseForInstall: OtaRelease? = null,
    val snackbarMessage: String? = null
)

class UpdaterViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = OtaRepository(db.otaReleaseDao(), db.updateHistoryDao())

    val deviceInfo: StateFlow<SystemDeviceInfo> = repository.deviceInfo
    val downloadProgress: StateFlow<DownloadProgress> = repository.downloadProgress
    val serverUrl: StateFlow<String> = repository.serverUrl

    private val _uiState = MutableStateFlow(UpdaterUiState())
    val uiState: StateFlow<UpdaterUiState> = _uiState.asStateFlow()

    // Flow of latest release matching Oppo A6X and the selected channel
    val latestRelease: StateFlow<OtaRelease?> = _uiState
        .flatMapLatest { state ->
            repository.getLatestRelease(OtaConstants.DEVICE_MODEL_NAME, state.selectedChannel)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // All releases for Oppo A6X
    val allReleases: StateFlow<List<OtaRelease>> = repository.getReleasesForDevice(OtaConstants.DEVICE_MODEL_NAME)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Update history
    val updateHistory: StateFlow<List<UpdateHistoryItem>> = repository.getUpdateHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Check if new update is newer than current installed version
    val isNewUpdateAvailable: StateFlow<Boolean> = combine(deviceInfo, latestRelease) { device, release ->
        release != null && release.versionCode > device.currentVersionCode
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        // Automatically check GitHub on start
        checkForUpdates(silent = true)
    }

    fun selectSubstance(substance: AppSubstance) {
        _uiState.value = _uiState.value.copy(selectedSubstance = substance)
    }

    fun selectChannel(channel: String) {
        _uiState.value = _uiState.value.copy(selectedChannel = channel)
        checkForUpdates(silent = false)
    }

    fun checkForUpdates(silent: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCheckingForUpdate = true)
            if (!silent) delay(1000)

            val fetchResult = repository.fetchFromGitHubRaw(_uiState.value.rawJsonUrl)
            _uiState.value = _uiState.value.copy(
                isCheckingForUpdate = false,
                lastCheckTime = System.currentTimeMillis()
            )

            if (!silent) {
                if (fetchResult.isSuccess) {
                    showSnackbar("Synced with GitHub raw updater.json for Oppo A6X")
                } else {
                    showSnackbar("Checked GitHub (${fetchResult.exceptionOrNull()?.message ?: "Using local database"})")
                }
            }
        }
    }

    fun updateRawJsonUrl(newUrl: String) {
        if (newUrl.isNotBlank()) {
            _uiState.value = _uiState.value.copy(rawJsonUrl = newUrl.trim())
            repository.setServerUrl(newUrl.trim())
            checkForUpdates(silent = false)
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

    // OTA Server Substance Actions
    fun publishNewRelease(
        versionName: String,
        versionCode: Int,
        channel: String,
        type: String,
        sizeMb: Long,
        changelog: String,
        isMandatory: Boolean
    ) {
        viewModelScope.launch {
            val newRelease = repository.createSampleRelease(
                versionName = versionName,
                versionCode = versionCode,
                channel = channel,
                type = type,
                sizeMb = sizeMb,
                changelog = changelog,
                isMandatory = isMandatory
            )
            repository.publishRelease(newRelease)
            showSnackbar("Published ${newRelease.versionName} for Oppo A6X successfully!")
            _uiState.value = _uiState.value.copy(isCreateReleaseOpen = false)
        }
    }

    fun deleteRelease(releaseId: String) {
        viewModelScope.launch {
            repository.deleteRelease(releaseId)
            showSnackbar("Release removed from Oppo A6X OTA repository.")
        }
    }

    fun toggleReleaseStatus(release: OtaRelease) {
        viewModelScope.launch {
            val newStatus = if (release.status == "PUBLISHED") "PAUSED" else "PUBLISHED"
            repository.updateRelease(release.copy(status = newStatus))
            showSnackbar("Release status changed to $newStatus")
        }
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

    fun setCreateReleaseOpen(open: Boolean) {
        _uiState.value = _uiState.value.copy(isCreateReleaseOpen = open)
    }

    fun setServerApiInspectorOpen(open: Boolean) {
        _uiState.value = _uiState.value.copy(isServerApiInspectorOpen = open)
    }

    fun showSnackbar(message: String) {
        _uiState.value = _uiState.value.copy(snackbarMessage = message)
    }

    fun clearSnackbar() {
        _uiState.value = _uiState.value.copy(snackbarMessage = null)
    }
}

/**
 * Typealias for OtaViewModel to maintain backward compatibility.
 */
typealias OtaViewModel = UpdaterViewModel
