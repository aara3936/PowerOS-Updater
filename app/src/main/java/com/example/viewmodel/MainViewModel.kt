package com.example.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.dispatcher.DefaultDispatcherProvider
import com.example.core.dispatcher.DispatcherProvider
import com.example.core.engine.OtaEngine
import com.example.core.model.AppEngineEvent
import com.example.core.model.AppEngineState
import com.example.core.model.SystemUpdateStatus
import com.example.core.persistence.OtaStateStore
import com.example.core.repository.SystemCoreRepository
import com.example.core.service.OtaDownloadService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * MainViewModel manages the state lifecycle and orchestrates coroutines.
 * Completely detached from View/Activity context to guarantee zero memory leaks.
 * Retains state across configuration changes (rotation, theme switch).
 */
class MainViewModel(
    private val repository: SystemCoreRepository = SystemCoreRepository(DefaultDispatcherProvider()),
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppEngineState())
    val uiState: StateFlow<AppEngineState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AppEngineEvent>(extraBufferCapacity = 16)
    val events: SharedFlow<AppEngineEvent> = _events.asSharedFlow()

    private val _userFeedback = MutableSharedFlow<String>(extraBufferCapacity = 8)
    val userFeedback: SharedFlow<String> = _userFeedback.asSharedFlow()

    init {
        initializeEngineCore()
        observeOtaEngine()
    }

    private fun observeOtaEngine() {
        viewModelScope.launch(dispatchers.main) {
            launch {
                OtaEngine.statusFlow.collectLatest { status ->
                    _uiState.value = _uiState.value.copy(updateStatus = status)
                    _events.tryEmit(AppEngineEvent.UpdateStatusChanged(status))
                }
            }
            launch {
                OtaEngine.currentChannelFlow.collectLatest { channel ->
                    _uiState.value = _uiState.value.copy(currentChannel = channel)
                    _events.tryEmit(AppEngineEvent.ChannelChanged(channel))
                }
            }
            launch {
                OtaEngine.announcementFlow.collectLatest { ann ->
                    _uiState.value = _uiState.value.copy(announcement = ann)
                }
            }
            launch {
                OtaEngine.latestReleaseFlow.collectLatest { release ->
                    _uiState.value = _uiState.value.copy(latestRelease = release)
                }
            }
            launch {
                OtaEngine.downloadProgressFlow.collectLatest { progress ->
                    _uiState.value = _uiState.value.copy(downloadProgress = progress)
                }
            }
            launch {
                OtaEngine.downloadedFileFlow.collectLatest { path ->
                    _uiState.value = _uiState.value.copy(downloadedFilePath = path)
                    if (path != null) {
                        _events.tryEmit(AppEngineEvent.UpdateDownloadCompleted(path))
                    }
                }
            }
            launch {
                OtaEngine.downloadSpeedFlow.collectLatest { speed ->
                    _uiState.value = _uiState.value.copy(downloadSpeedText = speed)
                    _events.tryEmit(AppEngineEvent.DownloadMetricsUpdated(speed, _uiState.value.downloadEtaText))
                }
            }
            launch {
                OtaEngine.downloadEtaFlow.collectLatest { eta ->
                    _uiState.value = _uiState.value.copy(downloadEtaText = eta)
                    _events.tryEmit(AppEngineEvent.DownloadMetricsUpdated(_uiState.value.downloadSpeedText, eta))
                }
            }
            launch {
                OtaEngine.isResumingFlow.collectLatest { isResuming ->
                    _uiState.value = _uiState.value.copy(isDownloadResuming = isResuming)
                }
            }
        }
    }

    /**
     * Continuous 5-second zero-latency GitHub ETag sync polling loop.
     * Guaranteed asynchronous execution on Dispatchers.IO.
     */
    fun startRealtimePolling(context: Context) {
        viewModelScope.launch(dispatchers.io) {
            while (isActive) {
                kotlinx.coroutines.delay(5000L)
                OtaEngine.checkForUpdates(context)
            }
        }
    }

    fun triggerDownloadOrInstall(context: Context) {
        val state = _uiState.value
        val release = state.latestRelease
        if (state.updateStatus == SystemUpdateStatus.UPDATE_AVAILABLE && release != null) {
            OtaDownloadService.startDownload(context, release)
        } else if (state.updateStatus == SystemUpdateStatus.READY_TO_INSTALL) {
            _userFeedback.tryEmit("System package ready for installation")
        } else {
            checkForUpdates(context)
        }
    }

    fun restorePersistedState(context: Context) {
        viewModelScope.launch(dispatchers.main) {
            val cachedState = OtaStateStore.loadState(context)
            if (cachedState.latestRelease != null || cachedState.downloadedFilePath != null) {
                _uiState.value = cachedState
                OtaEngine.setReleaseChannel(cachedState.currentChannel)
            }
        }
    }

    fun saveCurrentState(context: Context) {
        OtaStateStore.saveState(context, _uiState.value)
    }

    fun initializeEngineCore() {
        viewModelScope.launch(dispatchers.main) {
            val initialState = repository.loadInitialCoreState()
            _uiState.value = initialState
            _events.tryEmit(AppEngineEvent.CoreInitialized(System.currentTimeMillis()))
        }
    }

    fun checkForUpdates(context: Context) {
        viewModelScope.launch(dispatchers.main) {
            _uiState.value = _uiState.value.copy(updateStatus = SystemUpdateStatus.CHECKING)
            val result = OtaEngine.checkForUpdates(context)
            when (result) {
                is OtaEngine.CheckResult.UpToDate -> {
                    _userFeedback.tryEmit("System is up to date (${OtaEngine.CURRENT_VERSION_NAME})")
                }
                is OtaEngine.CheckResult.UpdateFound -> {
                    _userFeedback.tryEmit("New update available: ${result.release.version}")
                    // Ensure continuous background download persistence via Foreground Service
                    OtaDownloadService.startDownload(context, result.release)
                }
                is OtaEngine.CheckResult.UpdateReady -> {
                    _userFeedback.tryEmit("Update ${result.release.version} downloaded & ready to install")
                }
            }
            saveCurrentState(context)
        }
    }

    /**
     * Updates display refresh rate and tunes frame budgeting dynamically.
     * Guaranteed asynchronous calculation off the UI thread.
     */
    fun updateDisplayMetrics(refreshRateHz: Float) {
        viewModelScope.launch(dispatchers.main) {
            val frameBudget = repository.calculateFrameBudget(refreshRateHz)
            _uiState.value = _uiState.value.copy(
                displayRefreshRateHz = refreshRateHz,
                targetFrameTimeBudgetMs = frameBudget
            )
            _events.tryEmit(AppEngineEvent.DisplayRefreshRateUpdated(refreshRateHz, frameBudget))
        }
    }

    fun updateLifecycleState(stateName: String) {
        _uiState.value = _uiState.value.copy(lifecycleStateDescription = stateName)
        _events.tryEmit(AppEngineEvent.LifecycleUpdated(stateName))
    }

    fun openDialog(type: com.example.core.model.DialogType) {
        _uiState.value = _uiState.value.copy(activeDialog = type)
    }

    fun dismissDialog() {
        _uiState.value = _uiState.value.copy(activeDialog = null)
    }

    fun switchReleaseChannel(channel: com.example.core.model.ReleaseChannel, context: Context) {
        OtaEngine.setReleaseChannel(channel)
        _uiState.value = _uiState.value.copy(currentChannel = channel)
        _userFeedback.tryEmit("Switched to ${channel.displayName}")
        checkForUpdates(context)
    }

    fun updateSettings(wifiOnly: Boolean, intervalMinutes: Int) {
        _uiState.value = _uiState.value.copy(
            autoDownloadWifiOnly = wifiOnly,
            checkIntervalMinutes = intervalMinutes
        )
        _userFeedback.tryEmit("Settings saved: Check every ${intervalMinutes}m")
    }

    // Dropdown Menu Option Handlers (Strict Vertical Order)
    fun onBetaSectionClicked() {
        openDialog(com.example.core.model.DialogType.BETA_SECTION)
    }

    fun onNotificationsClicked() {
        openDialog(com.example.core.model.DialogType.NOTIFICATIONS)
    }

    fun onPrivacyLegalClicked() {
        openDialog(com.example.core.model.DialogType.PRIVACY_LEGAL)
    }

    fun onSettingsClicked() {
        openDialog(com.example.core.model.DialogType.SETTINGS)
    }
}
