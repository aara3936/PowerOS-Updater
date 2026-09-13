package com.example.core.model

import androidx.annotation.Keep

@Keep
data class AppEngineState(
    val appName: String = "Power OS Updater",
    val versionName: String = "2.1.0-BETA",
    val versionCode: Int = 210,
    val isInitialized: Boolean = false,
    val renderPipelineActive: Boolean = true,
    val hardwareAccelerated: Boolean = true,
    val displayRefreshRateHz: Float = 60.0f,
    val targetFrameTimeBudgetMs: Float = 16.67f,
    val lifecycleStateDescription: String = "Created"
)

sealed interface AppEngineEvent {
    data class CoreInitialized(val timestampMs: Long) : AppEngineEvent
    data class DisplayRefreshRateUpdated(val refreshRateHz: Float, val frameBudgetMs: Float) : AppEngineEvent
    data class LifecycleUpdated(val stateName: String) : AppEngineEvent
}
