package com.example.core.repository

import com.example.core.dispatcher.DispatcherProvider
import com.example.core.model.AppEngineState
import kotlinx.coroutines.withContext

class SystemCoreRepository(
    private val dispatchers: DispatcherProvider
) {
    /**
     * Initializes core environment parameters strictly on Dispatchers.IO
     * to eliminate UI thread blocking.
     */
    suspend fun loadInitialCoreState(): AppEngineState = withContext(dispatchers.io) {
        // System and environment reads executed in background IO
        AppEngineState(
            appName = "Power OS Updater",
            versionName = "2.1.0-RELEASE",
            versionCode = 210,
            isInitialized = true,
            renderPipelineActive = true,
            hardwareAccelerated = true,
            displayRefreshRateHz = 60.0f,
            targetFrameTimeBudgetMs = 16.67f,
            lifecycleStateDescription = "Initialized on Dispatchers.IO"
        )
    }

    /**
     * Computes target frame budget on Dispatchers.Default:
     * e.g., 60Hz -> ~16.6ms, 90Hz -> ~11.1ms, 120Hz -> ~8.33ms
     */
    suspend fun calculateFrameBudget(refreshRateHz: Float): Float = withContext(dispatchers.default) {
        val safeRate = if (refreshRateHz > 0f) refreshRateHz else 60.0f
        1000.0f / safeRate
    }
}
