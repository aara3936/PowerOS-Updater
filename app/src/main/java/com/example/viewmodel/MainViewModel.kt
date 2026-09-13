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
import com.example.core.repository.SystemCoreRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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
        }
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
                }
                is OtaEngine.CheckResult.UpdateReady -> {
                    _userFeedback.tryEmit("Update ${result.release.version} downloaded & ready to install")
                }
            }
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

    // Dropdown Menu Option Handlers (Strict Vertical Order)
    fun onBetaSectionClicked() {
        _userFeedback.tryEmit("Beta Section: Channel set to Beta (v2.1.0-BETA)")
    }

    fun onNotificationsClicked() {
        _userFeedback.tryEmit("Notifications & Announcements: No unread release notices")
    }

    fun onPrivacyLegalClicked() {
        _userFeedback.tryEmit("Privacy Policy & Legal Notices: Local verification active")
    }

    fun onSettingsClicked() {
        _userFeedback.tryEmit("Settings: Background Auto-Check (15 min interval) active")
    }
}
