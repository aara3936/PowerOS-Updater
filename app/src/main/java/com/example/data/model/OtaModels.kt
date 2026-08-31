package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Constants defining the live GitHub OTA repository and target device parameters.
 */
object OtaConstants {
    const val DEFAULT_RAW_JSON_URL = "https://raw.githubusercontent.com/aara3936/Oppo-A6X-OTA/main/updater.json"
    const val DEFAULT_TARGET_FILE_PATH = "/sdcard/Download/OTA/rom.zip"
    const val DEFAULT_TARGET_DIRECTORY = "/sdcard/Download/OTA"
    const val DEFAULT_TARGET_FILENAME = "rom.zip"
    const val DEVICE_MODEL_NAME = "Oppo A6X"
    const val DEVICE_CODENAME = "oppo_a6x"
}

/**
 * Represents an OTA Software Update package released for Oppo A6X.
 */
@Entity(tableName = "ota_releases")
data class OtaRelease(
    @PrimaryKey
    val id: String,
    val deviceModel: String = OtaConstants.DEVICE_MODEL_NAME,
    val deviceCodename: String = OtaConstants.DEVICE_CODENAME,
    val versionName: String,
    val versionCode: Int,
    val buildNumber: String,
    val releaseChannel: String = "Stable", // Stable, Beta, Developer Preview
    val releaseType: String = "Full OTA", // Full OTA, Incremental Patch
    val packageSizeBytes: Long = 1887436800L, // ~1.75 GB
    val downloadUrl: String = "https://github.com/aara3936/Oppo-A6X-OTA/releases/download/v2.1.0/rom.zip",
    val checksumSha256: String = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
    val androidVersion: String = "Android 15 (VanillaIceCream)",
    val securityPatch: String = "2026-08-05",
    val changelog: String,
    val releaseDate: Long = System.currentTimeMillis(),
    val isMandatory: Boolean = false,
    val minRequiredVersion: Int = 100,
    val status: String = "PUBLISHED", // PUBLISHED, STAGED, PAUSED, DEPRECATED
    val rolloutPercentage: Int = 100,
    val sourceUrl: String = OtaConstants.DEFAULT_RAW_JSON_URL,
    val targetLocalPath: String = OtaConstants.DEFAULT_TARGET_FILE_PATH
)

/**
 * System hardware and firmware state for the target device (Oppo A6X).
 */
data class SystemDeviceInfo(
    val deviceName: String = OtaConstants.DEVICE_MODEL_NAME,
    val deviceCodename: String = OtaConstants.DEVICE_CODENAME,
    val currentOsVersion: String = "PowerOS 1.2.0 (Oppo A6X)",
    val currentVersionCode: Int = 120,
    val currentBuildNumber: String = "POS-1.2.0-STABLE-20260515-OppoA6X",
    val androidVersion: String = "Android 15 (VanillaIceCream)",
    val securityPatch: String = "2026-05-01",
    val kernelVersion: String = "5.15.118-PowerOS-OppoA6X-v1.2",
    val batteryLevel: Int = 88,
    val isCharging: Boolean = true,
    val storageFreeGb: Float = 54.2f,
    val storageTotalGb: Float = 128.0f,
    val networkType: String = "Wi-Fi 6 (5 GHz)",
    val cpuArch: String = "MediaTek Dimensity / ARM64-v8a (Octa-Core 2.4 GHz)",
    val rawJsonSource: String = OtaConstants.DEFAULT_RAW_JSON_URL,
    val targetSavePath: String = OtaConstants.DEFAULT_TARGET_FILE_PATH
)

/**
 * Download and Installation Lifecycle State.
 */
enum class DownloadStatus {
    IDLE,
    CHECKING,
    DOWNLOADING,
    PAUSED,
    VERIFYING,
    READY_TO_INSTALL,
    INSTALLING,
    INSTALLED,
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

/**
 * History of installed system updates on the Oppo A6X device.
 */
@Entity(tableName = "update_history")
data class UpdateHistoryItem(
    @PrimaryKey(autoGenerate = true)
    val historyId: Long = 0L,
    val versionName: String,
    val versionCode: Int,
    val buildNumber: String,
    val installedTimestamp: Long = System.currentTimeMillis(),
    val packageSizeBytes: Long,
    val releaseChannel: String,
    val installType: String
)
