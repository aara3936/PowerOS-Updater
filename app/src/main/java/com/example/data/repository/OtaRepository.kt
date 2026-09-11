package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.network.NetworkUtils
import com.example.telemetry.AiTelemetryEngine
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File

class OtaRepository(private val context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val releaseDao = database.otaReleaseDao()
    private val historyDao = database.updateHistoryDao()

    private val _deviceInfo = MutableStateFlow(SystemDeviceInfo())
    val deviceInfo: StateFlow<SystemDeviceInfo> = _deviceInfo.asStateFlow()

    private val _downloadProgress = MutableStateFlow(DownloadProgress())
    val downloadProgress: StateFlow<DownloadProgress> = _downloadProgress.asStateFlow()

    private val _selectedChannel = MutableStateFlow("Stable")
    val selectedChannel: StateFlow<String> = _selectedChannel.asStateFlow()

    private var downloadJob: Job? = null

    val allReleases: Flow<List<OtaRelease>> = releaseDao.getAllReleases()
    val updateHistory: Flow<List<UpdateHistoryItem>> = historyDao.getAllHistory()

    val latestRelease: Flow<OtaRelease?> = _selectedChannel.flatMapLatest { channel ->
        releaseDao.getLatestRelease(OtaConstants.DEVICE_MODEL_NAME, channel)
    }

    suspend fun checkForUpdates(rawJsonUrl: String = OtaConstants.DEFAULT_RAW_JSON_URL): Result<List<OtaRelease>> = withContext(Dispatchers.IO) {
        try {
            _downloadProgress.update { it.copy(status = DownloadStatus.CHECKING, currentStep = "Querying Power OS release server...") }
            val result = NetworkUtils.fetchOtaManifest(rawJsonUrl)
            if (result.isSuccess) {
                val releases = result.getOrNull() ?: emptyList()
                if (releases.isNotEmpty()) {
                    releaseDao.insertReleases(releases)
                }
                _downloadProgress.update { it.copy(status = DownloadStatus.AVAILABLE, currentStep = "Found ${releases.size} available update package(s).") }
                Result.success(releases)
            } else {
                val fallback = AiTelemetryEngine.getSafeFallbackRelease()
                releaseDao.insertRelease(fallback)
                _downloadProgress.update { it.copy(status = DownloadStatus.AVAILABLE, currentStep = "Cached release available.") }
                Result.success(listOf(fallback))
            }
        } catch (e: Exception) {
            AiTelemetryEngine.logCrash(e)
            _downloadProgress.update { it.copy(status = DownloadStatus.FAILED, errorMessage = e.localizedMessage) }
            Result.failure(e)
        }
    }

    fun startDownload(release: OtaRelease, scope: CoroutineScope) {
        downloadJob?.cancel()
        val targetFile = NetworkUtils.resolveTargetRomFile(release.targetLocalPath, context)

        downloadJob = scope.launch {
            NetworkUtils.downloadRomStream(release, targetFile).collect { progress ->
                _downloadProgress.value = progress
            }
        }
    }

    fun pauseDownload() {
        downloadJob?.cancel()
        _downloadProgress.update { it.copy(status = DownloadStatus.PAUSED, currentStep = "Download paused by user.") }
    }

    fun resumeDownload(release: OtaRelease, scope: CoroutineScope) {
        startDownload(release, scope)
    }

    fun cancelDownload() {
        downloadJob?.cancel()
        _downloadProgress.value = DownloadProgress(status = DownloadStatus.IDLE)
    }

    suspend fun publishRelease(release: OtaRelease) = withContext(Dispatchers.IO) {
        releaseDao.insertRelease(release)
    }

    suspend fun deleteRelease(release: OtaRelease) = withContext(Dispatchers.IO) {
        releaseDao.deleteRelease(release)
    }

    fun updateSelectedChannel(channel: String) {
        _selectedChannel.value = channel
    }

    fun updateCustomSourceUrl(url: String) {
        _deviceInfo.update { it.copy(rawJsonSource = url) }
    }

    fun updateTargetSavePath(path: String) {
        _deviceInfo.update { it.copy(targetSavePath = path) }
    }

    /**
     * Executes the flash/installation simulation sequence:
     * - Verifying system partition integrity
     * - Unpacking payload image
     * - Patching system block devices
     * - Writing to update history
     * - Reboot prompt
     */
    suspend fun installUpdate(
        release: OtaRelease,
        onProgress: (Float, String) -> Unit,
        onComplete: (Boolean) -> Unit
    ) = withContext(Dispatchers.IO) {
        _downloadProgress.update { it.copy(status = DownloadStatus.INSTALLING) }

        val steps = listOf(
            0.15f to "Backing up boot and recovery bootloader partitions...",
            0.35f to "Mounting dynamic system_a partition...",
            0.60f to "Writing Power OS Liquid Glass system payload blocks...",
            0.85f to "Executing post-install dexopt and SELinux relabeling...",
            1.00f to "Finalizing partition swap (A/B seamless active switch)..."
        )

        for ((progress, stepText) in steps) {
            delay(1200)
            onProgress(progress, stepText)
            _downloadProgress.update { it.copy(installProgress = progress, currentStep = stepText) }
        }

        // Record to history
        val historyItem = UpdateHistoryItem(
            versionName = release.versionName,
            versionCode = release.versionCode,
            buildNumber = release.buildNumber,
            installedTimestamp = System.currentTimeMillis(),
            packageSizeBytes = release.packageSizeBytes,
            releaseChannel = release.releaseChannel,
            installType = release.releaseType
        )
        historyDao.insertHistory(historyItem)

        // Update local device info to reflect installed version
        _deviceInfo.update {
            it.copy(
                currentOsVersion = "Power OS ${release.versionName}",
                currentVersionCode = release.versionCode,
                currentBuildNumber = release.buildNumber
            )
        }

        _downloadProgress.update {
            it.copy(
                status = DownloadStatus.SUCCESS,
                currentStep = "Installation complete! Reboot required to finish update."
            )
        }
        onComplete(true)
    }
}
