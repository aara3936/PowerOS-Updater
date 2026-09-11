package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

object OtaConstants {
    const val DEVICE_MODEL_NAME = "Oppo A6X"
    const val DEVICE_CODENAME = "oppo_a6x"
    const val CURRENT_BASE_VERSION_NAME = "Power OS v3.0.0"
    const val CURRENT_BASE_VERSION_CODE = 3000
    const val CURRENT_BUILD_TAG = "POS-3.0.0-STABLE-OppoA6X"

    const val DEFAULT_RAW_JSON_URL = "https://raw.githubusercontent.com/aara3936/Oppo-A6X-OTA/main/metadata.json"
    const val DEFAULT_FALLBACK_JSON_URL = "https://raw.githubusercontent.com/aara3936/Oppo-A6X-OTA/main/updater.json"

    const val DEFAULT_TARGET_DIRECTORY = "/sdcard/Download/OTA"
    const val DEFAULT_TARGET_FILENAME = "rom.zip"
    const val DEFAULT_TARGET_FILE_PATH = "/sdcard/Download/OTA/rom.zip"

    val AVAILABLE_CHANNELS = listOf("Stable", "Beta", "Developer")
}

@Entity(tableName = "ota_releases")
data class OtaRelease(
    @PrimaryKey
    val id: String,
    val deviceModel: String = OtaConstants.DEVICE_MODEL_NAME,
    val deviceCodename: String = OtaConstants.DEVICE_CODENAME,
    val versionName: String,
    val versionCode: Int,
    val buildNumber: String = "",
    val releaseChannel: String = "Stable",
    val releaseType: String = "Full",
    val packageSizeBytes: Long = 0L,
    val downloadUrl: String,
    val checksumSha256: String = "",
    val androidVersion: String = "Android 14",
    val securityPatch: String = "2026-09-01",
    val changelog: String = "",
    val releaseDate: Long = System.currentTimeMillis(),
    val isMandatory: Boolean = false,
    val minRequiredVersion: Int = 0,
    val status: String = "active",
    val rolloutPercentage: Int = 100,
    val sourceUrl: String = "",
    val targetLocalPath: String = OtaConstants.DEFAULT_TARGET_FILE_PATH
) : java.io.Serializable

@Entity(tableName = "update_history")
data class UpdateHistoryItem(
    @PrimaryKey(autoGenerate = true)
    val historyId: Long = 0L,
    val versionName: String,
    val versionCode: Int,
    val buildNumber: String,
    val installedTimestamp: Long = System.currentTimeMillis(),
    val packageSizeBytes: Long = 0L,
    val releaseChannel: String = "Stable",
    val installType: String = "Full OTA"
)

enum class DownloadStatus {
    IDLE,
    CHECKING,
    AVAILABLE,
    DOWNLOADING,
    PAUSED,
    VERIFYING,
    READY_TO_INSTALL,
    INSTALLING,
    SUCCESS,
    FAILED
}

data class DownloadProgress(
    val status: DownloadStatus = DownloadStatus.IDLE,
    val progress: Float = 0f,
    val downloadedBytes: Long = 0L,
    val totalBytes: Long = 0L,
    val speedBytesPerSec: Long = 0L,
    val etaSeconds: Long = 0L,
    val currentStep: String = "",
    val installProgress: Float = 0f,
    val errorMessage: String? = null,
    val destinationPath: String = OtaConstants.DEFAULT_TARGET_FILE_PATH,
    val actualFileSize: Long = 0L
)

data class SystemDeviceInfo(
    val deviceName: String = OtaConstants.DEVICE_MODEL_NAME,
    val deviceCodename: String = OtaConstants.DEVICE_CODENAME,
    val currentOsVersion: String = OtaConstants.CURRENT_BASE_VERSION_NAME,
    val currentVersionCode: Int = OtaConstants.CURRENT_BASE_VERSION_CODE,
    val currentBuildNumber: String = OtaConstants.CURRENT_BUILD_TAG,
    val androidVersion: String = "Android 14 (UpsideDownCake)",
    val securityPatch: String = "September 1, 2026",
    val kernelVersion: String = "5.10.198-poweros-android14+",
    val batteryLevel: Int = 88,
    val isCharging: Boolean = false,
    val storageFreeGb: Float = 42.4f,
    val storageTotalGb: Float = 128.0f,
    val networkType: String = "Wi-Fi (5GHz - 866 Mbps)",
    val cpuArch: String = "arm64-v8a (Octa-core)",
    val rawJsonSource: String = OtaConstants.DEFAULT_RAW_JSON_URL,
    val targetSavePath: String = OtaConstants.DEFAULT_TARGET_FILE_PATH
)
