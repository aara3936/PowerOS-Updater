package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.dispatcher.DefaultDispatcherProvider
import com.example.core.dispatcher.DispatcherProvider
import com.example.core.model.AppEngineEvent
import com.example.core.model.AppEngineState
import com.example.core.repository.SystemCoreRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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

    init {
        initializeEngineCore()
    }

    fun initializeEngineCore() {
        viewModelScope.launch(dispatchers.main) {
            val initialState = repository.loadInitialCoreState()
            _uiState.value = initialState
            _events.tryEmit(AppEngineEvent.CoreInitialized(System.currentTimeMillis()))
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
}
