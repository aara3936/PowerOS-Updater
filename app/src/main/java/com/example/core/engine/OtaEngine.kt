package com.example.core.engine

import android.content.Context
import android.util.Base64
import com.example.core.dispatcher.DefaultDispatcherProvider
import com.example.core.dispatcher.DispatcherProvider
import com.example.core.model.ReleaseChannel
import com.example.core.model.SystemAnnouncement
import com.example.core.model.SystemUpdateStatus
import com.example.core.model.UpdateRelease
import com.example.core.security.NativeSecurityBridge
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import java.util.concurrent.TimeUnit

object OtaEngine {
    private const val GITHUB_CONTENTS_API_URL = "https://api.github.com/repos/aara3936/PowerOS-OTA/contents/updater.json"
    private const val DEFAULT_MANIFEST_URL = "https://raw.githubusercontent.com/aara3936/PowerOS-OTA/main/updater.json"
    const val CURRENT_VERSION_CODE = 210
    const val CURRENT_VERSION_NAME = "2.1.0-RELEASE"

    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
    private val downloadMutex = Mutex()
    private var isCurrentlyDownloading: Boolean = false

    private var lastEtag: String? = null
    private var cachedManifestJson: String = ""

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val _statusFlow = MutableStateFlow(SystemUpdateStatus.UP_TO_DATE)
    val statusFlow: StateFlow<SystemUpdateStatus> = _statusFlow.asStateFlow()

    private val _currentChannelFlow = MutableStateFlow(ReleaseChannel.STABLE)
    val currentChannelFlow: StateFlow<ReleaseChannel> = _currentChannelFlow.asStateFlow()

    private val _announcementFlow = MutableStateFlow<SystemAnnouncement?>(null)
    val announcementFlow: StateFlow<SystemAnnouncement?> = _announcementFlow.asStateFlow()

    private val _latestReleaseFlow = MutableStateFlow<UpdateRelease?>(null)
    val latestReleaseFlow: StateFlow<UpdateRelease?> = _latestReleaseFlow.asStateFlow()

    private val _downloadProgressFlow = MutableStateFlow(0)
    val downloadProgressFlow: StateFlow<Int> = _downloadProgressFlow.asStateFlow()

    private val _downloadedFileFlow = MutableStateFlow<String?>(null)
    val downloadedFileFlow: StateFlow<String?> = _downloadedFileFlow.asStateFlow()

    private val _downloadSpeedFlow = MutableStateFlow("")
    val downloadSpeedFlow: StateFlow<String> = _downloadSpeedFlow.asStateFlow()

    private val _downloadEtaFlow = MutableStateFlow("")
    val downloadEtaFlow: StateFlow<String> = _downloadEtaFlow.asStateFlow()

    private val _isResumingFlow = MutableStateFlow(false)
    val isResumingFlow: StateFlow<Boolean> = _isResumingFlow.asStateFlow()

    fun setReleaseChannel(channel: ReleaseChannel) {
        _currentChannelFlow.value = channel
    }

    fun setStatus(status: SystemUpdateStatus) {
        _statusFlow.value = status
    }

    fun setLiveAnnouncement(announcement: SystemAnnouncement) {
        _announcementFlow.value = announcement
    }

    fun setLiveRelease(channel: ReleaseChannel, release: UpdateRelease) {
        _latestReleaseFlow.value = release
        if (_currentChannelFlow.value == channel && release.versionCode > CURRENT_VERSION_CODE) {
            _statusFlow.value = SystemUpdateStatus.UPDATE_AVAILABLE
        }
    }

    /**
     * Checks for updates from GitHub using dynamic ETag / Header validation.
     * Detects remote changes instantly with minimal network overhead.
     * When a newer release is detected, AUTO-TRIGGERS the background download pipeline
     * with zero manual user interaction.
     */
    suspend fun checkForUpdates(
        context: Context,
        targetChannel: ReleaseChannel = _currentChannelFlow.value
    ): CheckResult = withContext(dispatchers.io) {
        if (!isCurrentlyDownloading && _statusFlow.value != SystemUpdateStatus.READY_TO_INSTALL) {
            _statusFlow.value = SystemUpdateStatus.CHECKING
        }

        try {
            val manifestJson = fetchManifestWithEtag()

            if (manifestJson.isNotBlank()) {
                val announcement = parseAnnouncementJson(manifestJson)
                if (announcement != null && announcement != _announcementFlow.value) {
                    _announcementFlow.value = announcement
                }

                val release = parseReleaseJson(manifestJson, targetChannel)

                if (release != null) {
                    if (release.freeze || release.versionCode < CURRENT_VERSION_CODE) {
                        isCurrentlyDownloading = false
                        _statusFlow.value = SystemUpdateStatus.UP_TO_DATE
                        _latestReleaseFlow.value = null
                        _downloadProgressFlow.value = 0
                        _downloadSpeedFlow.value = ""
                        _downloadEtaFlow.value = ""
                        File(context.filesDir, "updates").deleteRecursively()
                        return@withContext CheckResult.UpToDate
                    } else if (release.versionCode > CURRENT_VERSION_CODE) {
                        _latestReleaseFlow.value = release
    
                        // If already downloaded and verified, keep READY_TO_INSTALL
                        val updatesDir = File(context.filesDir, "updates")
                        val targetFile = File(updatesDir, "PowerOS_update_${release.versionCode}.zip")
                        if (targetFile.exists() && targetFile.length() > 0 &&
                            NativeSecurityBridge.verifyPayloadHeader(targetFile, dispatchers.io)) {
                            _downloadProgressFlow.value = 100
                            _downloadedFileFlow.value = targetFile.absolutePath
                            _statusFlow.value = SystemUpdateStatus.READY_TO_INSTALL
                            return@withContext CheckResult.UpdateReady(release, targetFile.absolutePath)
                        }
    
                        // Auto-Trigger Download Pipeline: zero manual interaction required
                        if (!isCurrentlyDownloading) {
                            _statusFlow.value = SystemUpdateStatus.UPDATE_AVAILABLE
                            val downloadedPath = downloadUpdatePackage(context, release)
                            if (downloadedPath != null) {
                                _downloadedFileFlow.value = downloadedPath
                                _statusFlow.value = SystemUpdateStatus.READY_TO_INSTALL
                                return@withContext CheckResult.UpdateReady(release, downloadedPath)
                            } else {
                                _statusFlow.value = SystemUpdateStatus.UPDATE_AVAILABLE
                                return@withContext CheckResult.UpdateFound(release)
                            }
                        } else {
                            return@withContext CheckResult.UpdateFound(release)
                        }
                    } else {
                        if (!isCurrentlyDownloading && _statusFlow.value != SystemUpdateStatus.READY_TO_INSTALL) {
                            _statusFlow.value = SystemUpdateStatus.UP_TO_DATE
                        }
                        return@withContext CheckResult.UpToDate
                    }
                } else {
                    if (!isCurrentlyDownloading && _statusFlow.value != SystemUpdateStatus.READY_TO_INSTALL) {
                        _statusFlow.value = SystemUpdateStatus.UP_TO_DATE
                    }
                    return@withContext CheckResult.UpToDate
                }
            } else {
                return@withContext fallbackStorageCheck(context, targetChannel)
            }
        } catch (_: Exception) {
            return@withContext fallbackStorageCheck(context, targetChannel)
        }
    }

    private suspend fun fallbackStorageCheck(context: Context, targetChannel: ReleaseChannel): CheckResult {
        val updatesDir = File(context.filesDir, "updates")
        val existing = updatesDir.listFiles()?.firstOrNull { it.isFile && it.length() > 0 && !it.name.endsWith(".part") }
        return if (existing != null) {
            _downloadedFileFlow.value = existing.absolutePath
            _statusFlow.value = SystemUpdateStatus.READY_TO_INSTALL
            CheckResult.UpdateReady(
                UpdateRelease(
                    version = "2.2.0-RELEASE",
                    versionCode = 220,
                    releaseDate = "2026-09-15",
                    size = "${existing.length() / 1024} KB",
                    zipUrl = "",
                    changelog = "Downloaded update ready",
                    channel = targetChannel.tag
                ),
                existing.absolutePath
            )
        } else {
            if (!isCurrentlyDownloading && _statusFlow.value != SystemUpdateStatus.READY_TO_INSTALL) {
                _statusFlow.value = SystemUpdateStatus.UP_TO_DATE
            }
            CheckResult.UpToDate
        }
    }

    /**
     * Dynamic ETag / Header checking against GitHub repository API endpoints.
     * Returns HTTP 304 (Not Modified) with zero payload latency when unchanged.
     */
    private fun fetchManifestWithEtag(): String {
        // 1. Try GitHub Contents API with ETag
        try {
            val reqBuilder = Request.Builder()
                .url(GITHUB_CONTENTS_API_URL)
                .header("Accept", "application/vnd.github.v3+json")

            lastEtag?.let {
                reqBuilder.header("If-None-Match", it)
            }

            httpClient.newCall(reqBuilder.build()).execute().use { response ->
                if (response.code == 304 && cachedManifestJson.isNotBlank()) {
                    return cachedManifestJson
                }
                if (response.isSuccessful) {
                    val etag = response.header("ETag")
                    if (!etag.isNullOrBlank()) lastEtag = etag

                    val bodyStr = response.body?.string().orEmpty()
                    if (bodyStr.contains("\"content\"")) {
                        val obj = JSONObject(bodyStr)
                        val base64Content = obj.optString("content", "").replace("\n", "").replace("\r", "")
                        val decoded = String(Base64.decode(base64Content, Base64.DEFAULT), Charsets.UTF_8)
                        if (decoded.isNotBlank()) {
                            cachedManifestJson = decoded
                            return decoded
                        }
                    }
                }
            }
        } catch (_: Exception) {}

        // 2. Direct Raw GitHub URL with Cache Buster Fallback
        try {
            val cacheBusterUrl = "$DEFAULT_MANIFEST_URL?nocache=${System.currentTimeMillis()}"
            val req = Request.Builder().url(cacheBusterUrl).build()
            httpClient.newCall(req).execute().use { response ->
                if (response.isSuccessful) {
                    val str = response.body?.string().orEmpty()
                    if (str.isNotBlank()) {
                        cachedManifestJson = str
                        return str
                    }
                }
            }
        } catch (_: Exception) {}

        return cachedManifestJson
    }

    /**
     * High-Performance Chunked Downloader with Auto-Resume and Connection Recovery.
     * Streams binary downloads asynchronously on Dispatchers.IO.
     * Uses HTTP Range headers (bytes=$offset-) to resume interrupted downloads.
     * Streams exact speed (MB/s) and ETA to StateFlow without dropping UI frames.
     */
    suspend fun downloadUpdatePackage(context: Context, release: UpdateRelease): String? = withContext(dispatchers.io) {
        if (release.zipUrl.isBlank()) return@withContext null

        downloadMutex.withLock {
            isCurrentlyDownloading = true
            _statusFlow.value = SystemUpdateStatus.DOWNLOADING

            val updatesDir = File(context.filesDir, "updates").apply { mkdirs() }
            val targetFile = File(updatesDir, "PowerOS_update_${release.versionCode}.zip")
            val partFile = File(updatesDir, "PowerOS_update_${release.versionCode}.zip.part")

            // If target file already exists and passes security checks, return immediately
            if (targetFile.exists() && targetFile.length() > 0) {
                val isHeaderValid = NativeSecurityBridge.verifyPayloadHeader(targetFile, dispatchers.io)
                val isSha256Valid = release.sha256.isBlank() || NativeSecurityBridge.verifyIntegrity(
                    targetFile.absolutePath,
                    release.sha256,
                    dispatchers.io
                )
                if (isHeaderValid && isSha256Valid) {
                    _downloadProgressFlow.value = 100
                    _downloadSpeedFlow.value = "100%"
                    _downloadEtaFlow.value = "Ready"
                    _isResumingFlow.value = false
                    _downloadedFileFlow.value = targetFile.absolutePath
                    _statusFlow.value = SystemUpdateStatus.READY_TO_INSTALL
                    isCurrentlyDownloading = false
                    return@withLock targetFile.absolutePath
                }
            }

            val maxRetries = 5
            var retryCount = 0
            var downloadSuccess = false

            while (retryCount < maxRetries && !downloadSuccess) {
                val existingBytes = if (partFile.exists()) partFile.length() else 0L
                val isResuming = existingBytes > 0L
                _isResumingFlow.value = isResuming

                try {
                    val requestBuilder = Request.Builder().url(release.zipUrl)
                    if (isResuming) {
                        requestBuilder.header("Range", "bytes=$existingBytes-")
                    }
                    val request = requestBuilder.build()
                    httpClient.newCall(request).execute().use { response ->
                        val responseCode = response.code
                        if (responseCode != 200 && responseCode != 206 && responseCode != 416) {
                            retryCount++
                            delay(1500L)
                            return@use // Proceed to next loop iteration after delay
                        }

                        if (responseCode == 416) {
                            if (partFile.exists() && partFile.length() > 0) {
                                if (targetFile.exists()) targetFile.delete()
                                partFile.renameTo(targetFile)
                                downloadSuccess = true
                                return@use
                            } else {
                                partFile.delete()
                                retryCount++
                                return@use
                            }
                        }

                        val body = response.body
                        if (body == null) {
                            retryCount++
                            delay(1000L)
                            return@use
                        }

                        val streamLength = body.contentLength()
                        val appendMode = (responseCode == 206)
                        val totalLength = if (appendMode) {
                            existingBytes + if (streamLength > 0) streamLength else 0L
                        } else {
                            if (streamLength > 0) streamLength else 0L
                        }

                        if (!appendMode && partFile.exists()) {
                            partFile.delete()
                        }

                        val outputStream = FileOutputStream(partFile, appendMode)
                        val inputStream = body.byteStream()
                        val buffer = ByteArray(16384)

                        var currentBytes = if (appendMode) existingBytes else 0L
                        var bytesRead: Int
                        var lastWindowTime = System.currentTimeMillis()
                        var lastWindowBytes = 0L

                        inputStream.use { input ->
                            outputStream.use { output ->
                                while (input.read(buffer).also { bytesRead = it } != -1) {
                                    output.write(buffer, 0, bytesRead)
                                    currentBytes += bytesRead
                                    lastWindowBytes += bytesRead

                                    val now = System.currentTimeMillis()
                                    val elapsedMs = now - lastWindowTime
                                    if (elapsedMs >= 500) {
                                        val speedBps = (lastWindowBytes * 1000.0) / elapsedMs
                                        val speedMb = speedBps / (1024.0 * 1024.0)
                                        val speedStr = if (speedMb >= 0.1) {
                                            String.format(Locale.US, "%.2f MB/s", speedMb)
                                        } else {
                                            String.format(Locale.US, "%.1f KB/s", speedBps / 1024.0)
                                        }
                                        val currentMb = currentBytes / (1024.0 * 1024.0)
                                        val totalMb = totalLength / (1024.0 * 1024.0)
                                        val progressStr = String.format(Locale.US, "%.1f MB / %.1f MB", currentMb, totalMb)
                                        _downloadSpeedFlow.value = "$speedStr  •  $progressStr"

                                        if (totalLength > 0 && speedBps > 0) {
                                            val remainingBytes = (totalLength - currentBytes).coerceAtLeast(0)
                                            val etaSec = (remainingBytes / speedBps).toLong()
                                            _downloadEtaFlow.value = if (etaSec >= 60) {
                                                "${etaSec / 60}m ${etaSec % 60}s"
                                            } else {
                                                "${etaSec}s"
                                            }
                                            val progress = ((currentBytes * 100) / totalLength).toInt().coerceIn(0, 100)
                                            _downloadProgressFlow.value = progress
                                        }
                                        lastWindowTime = now
                                        lastWindowBytes = 0L
                                    }
                                }
                            }
                        }

                        if (partFile.exists() && partFile.length() > 0) {
                            if (targetFile.exists()) targetFile.delete()
                            partFile.renameTo(targetFile)
                            downloadSuccess = true
                        }
                    }
                } catch (e: Exception) {
                    retryCount++
                    _isResumingFlow.value = true
                    _downloadSpeedFlow.value = "Reconnecting..."
                    _downloadEtaFlow.value = "Auto-Resuming (${retryCount}/$maxRetries)"
                    delay(2000L)
                }
            }

            isCurrentlyDownloading = false

            if (downloadSuccess && targetFile.exists() && targetFile.length() > 0) {
                // Native Security Integrity & Header Validation
                val isHeaderValid = NativeSecurityBridge.verifyPayloadHeader(targetFile, dispatchers.io)
                if (!isHeaderValid) {
                    targetFile.delete()
                    _statusFlow.value = SystemUpdateStatus.ERROR
                    _downloadSpeedFlow.value = "Corrupt payload"
                    _downloadEtaFlow.value = ""
                    return@withLock null
                }

                if (release.sha256.isNotBlank()) {
                    val isSha256Valid = NativeSecurityBridge.verifyIntegrity(
                        targetFile.absolutePath,
                        release.sha256,
                        dispatchers.io
                    )
                    if (!isSha256Valid) {
                        targetFile.delete()
                        _statusFlow.value = SystemUpdateStatus.ERROR
                        _downloadSpeedFlow.value = "Hash mismatch"
                        _downloadEtaFlow.value = ""
                        return@withLock null
                    }
                }

                _downloadProgressFlow.value = 100
                _downloadSpeedFlow.value = "Verified"
                _downloadEtaFlow.value = "Ready to install"
                _isResumingFlow.value = false
                _downloadedFileFlow.value = targetFile.absolutePath
                _statusFlow.value = SystemUpdateStatus.READY_TO_INSTALL
                targetFile.absolutePath
            } else {
                _statusFlow.value = SystemUpdateStatus.ERROR
                _downloadSpeedFlow.value = "Download failed"
                _downloadEtaFlow.value = ""
                _isResumingFlow.value = false
                null
            }
        }
    }

    fun parseAnnouncementJson(jsonString: String): SystemAnnouncement? {
        if (jsonString.isBlank()) return null
        try {
            val root = JSONObject(jsonString)
            if (root.has("announcement")) {
                val ann = root.getJSONObject("announcement")
                return SystemAnnouncement(
                    title = ann.optString("title", "System Announcement"),
                    date = ann.optString("date", ""),
                    message = ann.optString("message", "")
                )
            }
        } catch (_: Throwable) {
            val titleMatch = Regex("\"title\"\\s*:\\s*\"([^\"]*)\"").find(jsonString)
            val messageMatch = Regex("\"message\"\\s*:\\s*\"([^\"]*)\"").find(jsonString)
            val dateMatch = Regex("\"date\"\\s*:\\s*\"([^\"]*)\"").find(jsonString)
            if (titleMatch != null && messageMatch != null) {
                return SystemAnnouncement(
                    title = titleMatch.groupValues[1],
                    date = dateMatch?.groupValues?.get(1).orEmpty(),
                    message = messageMatch.groupValues[1]
                )
            }
        }
        return null
    }

    fun parseReleaseJson(jsonString: String, channel: ReleaseChannel = ReleaseChannel.STABLE): UpdateRelease? {
        if (jsonString.isBlank()) return null
        try {
            val root = JSONObject(jsonString)
            val releaseObj = if (root.has("channels")) {
                val channels = root.getJSONObject("channels")
                if (channel == ReleaseChannel.STABLE && channels.has("beta")) {
                    channels.getJSONObject("beta")
                } else if (channels.has("stable")) {
                    channels.getJSONObject("stable")
                } else {
                    root
                }
            } else if (root.has("stable")) {
                root.getJSONObject("stable")
            } else {
                root
            }
            return UpdateRelease(
                version = releaseObj.optString("version", "2.1.0-RELEASE"),
                versionCode = releaseObj.optInt("versionCode", 210),
                releaseDate = releaseObj.optString("releaseDate", ""),
                size = releaseObj.optString("size", ""),
                zipUrl = releaseObj.optString("zipUrl", ""),
                changelog = releaseObj.optString("changelog", ""),
                channel = channel.tag,
                sha256 = releaseObj.optString("sha256", ""),
                freeze = releaseObj.optBoolean("freeze", false)
            )
        } catch (_: Throwable) {
            return parseReleaseJsonFallback(jsonString, channel)
        }
    }

    private fun parseReleaseJsonFallback(json: String, channel: ReleaseChannel): UpdateRelease? {
        return try {
            val channelBlockMatch = Regex("\"${channel.tag}\"\\s*:\\s*\\{([^}]+)\\}").find(json)
            val targetScope = channelBlockMatch?.groupValues?.get(1) ?: json

            fun extractString(key: String): String {
                val match = Regex("\"$key\"\\s*:\\s*\"([^\"]*)\"").find(targetScope)
                return match?.groupValues?.get(1).orEmpty()
            }
            fun extractInt(key: String): Int {
                val match = Regex("\"$key\"\\s*:\\s*(\\d+)").find(targetScope)
                return match?.groupValues?.get(1)?.toIntOrNull() ?: 210
            }

            val version = extractString("version").ifEmpty { "2.1.0-RELEASE" }
            val versionCode = extractInt("versionCode")
            val releaseDate = extractString("releaseDate")
            val size = extractString("size")
            val zipUrl = extractString("zipUrl")
            val changelog = extractString("changelog")
            val sha256 = extractString("sha256")
            val freeze = Regex("\"freeze\"\\s*:\\s*(true|false)").find(targetScope)?.groupValues?.get(1)?.toBoolean() ?: false

            UpdateRelease(
                version = version,
                versionCode = versionCode,
                releaseDate = releaseDate,
                size = size,
                zipUrl = zipUrl,
                changelog = changelog,
                channel = channel.tag,
                sha256 = sha256,
                freeze = freeze
            )
        } catch (_: Exception) {
            null
        }
    }

    sealed interface CheckResult {
        data object UpToDate : CheckResult
        data class UpdateFound(val release: UpdateRelease) : CheckResult
        data class UpdateReady(val release: UpdateRelease, val filePath: String) : CheckResult
    }
}
