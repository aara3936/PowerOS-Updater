package com.example.core.model

import androidx.annotation.Keep

@Keep
data class UpdateRelease(
    val version: String,
    val versionCode: Int,
    val releaseDate: String,
    val size: String,
    val zipUrl: String,
    val changelog: String,
    val channel: String = "stable"
)

@Keep
data class SystemAnnouncement(
    val title: String,
    val date: String,
    val message: String
)

@Keep
enum class SystemUpdateStatus {
    CHECKING,
    UP_TO_DATE,
    UPDATE_AVAILABLE,
    DOWNLOADING,
    READY_TO_INSTALL,
    ERROR
}

@Keep
enum class ReleaseChannel(val displayName: String, val tag: String) {
    STABLE("Stable Channel", "stable"),
    BETA("Beta Channel", "beta")
}

