package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.OtaRepository
import com.example.ui.security.DeveloperAuthManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UpdaterViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = OtaRepository(application)

    private val _uiState = MutableStateFlow(UpdaterUiState())
    val uiState: StateFlow<UpdaterUiState> = _uiState.asStateFlow()

    init {
        // Collect repository state flows
        viewModelScope.launch {
            repository.deviceInfo.collect { info ->
                _uiState.update { it.copy(deviceInfo = info) }
            }
        }

        viewModelScope.launch {
            repository.downloadProgress.collect { progress ->
                _uiState.update { it.copy(downloadProgress = progress) }
            }
        }

        viewModelScope.launch {
            repository.selectedChannel.collect { channel ->
                _uiState.update { it.copy(selectedChannel = channel) }
            }
        }

        viewModelScope.launch {
            repository.updateHistory.collect { history ->
                _uiState.update { it.copy(updateHistory = history) }
            }
        }

        viewModelScope.launch {
            repository.latestRelease.collect { release ->
                val isNewer = if (release != null) {
                    release.versionCode > _uiState.value.deviceInfo.currentVersionCode
                } else false

                _uiState.update {
                    it.copy(
                        latestRelease = release,
                        isUpdateAvailable = isNewer
                    )
                }
            }
        }

        viewModelScope.launch {
            DeveloperAuthManager.isDeveloperUnlocked.collect { unlocked ->
                _uiState.update { it.copy(isDeveloperModeActive = unlocked) }
            }
        }

        viewModelScope.launch {
            DeveloperAuthManager.isLockedOut.collect { lockedOut ->
                _uiState.update { it.copy(showLockoutDialog = lockedOut) }
            }
        }

        // Initial check for updates
        checkForUpdates()
    }

    fun checkForUpdates() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingForUpdates = true) }
            val currentSource = _uiState.value.deviceInfo.rawJsonSource
            val result = repository.checkForUpdates(currentSource)
            _uiState.update { it.copy(isCheckingForUpdates = false) }

            if (result.isSuccess) {
                val releases = result.getOrNull()
                if (releases.isNullOrEmpty()) {
                    showSnackbar("Your device is running the latest Power OS.")
                } else {
                    showSnackbar("Found ${releases.size} update package(s).")
                }
            } else {
                showSnackbar("Remote server unreachable. Local fallback cache active.")
            }
        }
    }

    fun startDownload(release: OtaRelease) {
        repository.startDownload(release, viewModelScope)
    }

    fun pauseDownload() {
        repository.pauseDownload()
    }

    fun resumeDownload(release: OtaRelease) {
        repository.resumeDownload(release, viewModelScope)
    }

    fun installUpdate(release: OtaRelease) {
        viewModelScope.launch {
            repository.installUpdate(
                release = release,
                onProgress = { _, _ -> },
                onComplete = { success ->
                    if (success) {
                        showSnackbar("Power OS ${release.versionName} installed successfully!")
                    }
                }
            )
        }
    }

    fun publishCustomRelease(release: OtaRelease) {
        viewModelScope.launch {
            repository.publishRelease(release)
        }
    }

    fun setChannel(channel: String) {
        repository.updateSelectedChannel(channel)
    }

    fun setCustomSourceUrl(url: String) {
        repository.updateCustomSourceUrl(url)
    }

    fun setTargetSavePath(path: String) {
        repository.updateTargetSavePath(path)
    }

    fun onVersionTapped() {
        if (DeveloperAuthManager.isLockedOut.value) {
            _uiState.update { it.copy(showLockoutDialog = true) }
        } else {
            _uiState.update { it.copy(showAuthDialog = true) }
        }
    }

    fun dismissAuthDialog() {
        _uiState.update { it.copy(showAuthDialog = false) }
    }

    fun onAuthenticated() {
        _uiState.update {
            it.copy(
                showAuthDialog = false,
                showSettingsDialog = false,
                isDeveloperModeActive = true
            )
        }
        showSnackbar("Developer Mode Enabled: Welcome, Engineer.")
    }

    fun exitDeveloperPortal() {
        DeveloperAuthManager.purgeSession()
        _uiState.update { it.copy(isDeveloperModeActive = false) }
    }

    fun setShowSettingsDialog(show: Boolean) {
        _uiState.update { it.copy(showSettingsDialog = show) }
    }

    fun setShowSpecsDialog(show: Boolean) {
        _uiState.update { it.copy(showSpecsDialog = show) }
    }

    fun setShowHistoryDialog(show: Boolean) {
        _uiState.update { it.copy(showHistoryDialog = show) }
    }

    fun setShowLockoutDialog(show: Boolean) {
        _uiState.update { it.copy(showLockoutDialog = show) }
    }

    fun showSnackbar(message: String) {
        _uiState.update { it.copy(snackbarMessage = message) }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun onAppSentToBackground() {
        DeveloperAuthManager.purgeSession()
        _uiState.update { it.copy(isDeveloperModeActive = false, showAuthDialog = false) }
    }
}
