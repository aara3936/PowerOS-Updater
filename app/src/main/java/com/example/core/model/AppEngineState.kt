package com.example.core.model

import androidx.annotation.Keep

@Keep
enum class DialogType {
    BETA_SECTION,
    NOTIFICATIONS,
    PRIVACY_LEGAL,
    SETTINGS
}

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
    val lifecycleStateDescription: String = "Created",
    val updateStatus: SystemUpdateStatus = SystemUpdateStatus.UP_TO_DATE,
    val currentChannel: ReleaseChannel = ReleaseChannel.BETA,
    val announcement: SystemAnnouncement? = null,
    val latestRelease: UpdateRelease? = null,
    val downloadProgress: Int = 0,
    val downloadedFilePath: String? = null,
    val autoDownloadWifiOnly: Boolean = true,
    val checkIntervalMinutes: Int = 15,
    val activeDialog: DialogType? = null
)

sealed interface AppEngineEvent {
    data class CoreInitialized(val timestampMs: Long) : AppEngineEvent
    data class DisplayRefreshRateUpdated(val refreshRateHz: Float, val frameBudgetMs: Float) : AppEngineEvent
    data class LifecycleUpdated(val stateName: String) : AppEngineEvent
    data class UpdateStatusChanged(val status: SystemUpdateStatus) : AppEngineEvent
    data class UpdateDownloadCompleted(val filePath: String) : AppEngineEvent
    data class ChannelChanged(val channel: ReleaseChannel) : AppEngineEvent
}
