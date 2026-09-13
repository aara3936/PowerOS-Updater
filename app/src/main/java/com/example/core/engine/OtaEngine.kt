package com.example.core.engine

import android.content.Context
import com.example.core.dispatcher.DefaultDispatcherProvider
import com.example.core.dispatcher.DispatcherProvider
import com.example.core.model.ReleaseChannel
import com.example.core.model.SystemAnnouncement
import com.example.core.model.SystemUpdateStatus
import com.example.core.model.UpdateRelease
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

object OtaEngine {
    private const val DEFAULT_MANIFEST_URL = "https://raw.githubusercontent.com/aara3936/PowerOS-OTA/main/updater.json"
    const val CURRENT_VERSION_CODE = 210
    const val CURRENT_VERSION_NAME = "2.1.0-BETA"

    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val _statusFlow = MutableStateFlow(SystemUpdateStatus.UP_TO_DATE)
    val statusFlow: StateFlow<SystemUpdateStatus> = _statusFlow.asStateFlow()

    private val _currentChannelFlow = MutableStateFlow(ReleaseChannel.BETA)
    val currentChannelFlow: StateFlow<ReleaseChannel> = _currentChannelFlow.asStateFlow()

    private val _announcementFlow = MutableStateFlow<SystemAnnouncement?>(null)
    val announcementFlow: StateFlow<SystemAnnouncement?> = _announcementFlow.asStateFlow()

    private val _latestReleaseFlow = MutableStateFlow<UpdateRelease?>(null)
    val latestReleaseFlow: StateFlow<UpdateRelease?> = _latestReleaseFlow.asStateFlow()

    private val _downloadProgressFlow = MutableStateFlow(0)
    val downloadProgressFlow: StateFlow<Int> = _downloadProgressFlow.asStateFlow()

    private val _downloadedFileFlow = MutableStateFlow<String?>(null)
    val downloadedFileFlow: StateFlow<String?> = _downloadedFileFlow.asStateFlow()

    fun setReleaseChannel(channel: ReleaseChannel) {
        _currentChannelFlow.value = channel
    }

    /**
     * Checks for updates from live backend / GitHub raw repository.
     * Guaranteed asynchronous execution on Dispatchers.IO.
     */
    suspend fun checkForUpdates(context: Context, targetChannel: ReleaseChannel = _currentChannelFlow.value): CheckResult = withContext(dispatchers.io) {
        _statusFlow.value = SystemUpdateStatus.CHECKING
        try {
            val manifestJson = fetchManifestString(DEFAULT_MANIFEST_URL)
            val announcement = parseAnnouncementJson(manifestJson)
            if (announcement != null) {
                _announcementFlow.value = announcement
            }

            val release = parseReleaseJson(manifestJson, targetChannel)

            if (release != null && release.versionCode > CURRENT_VERSION_CODE) {
                _latestReleaseFlow.value = release
                _statusFlow.value = SystemUpdateStatus.UPDATE_AVAILABLE

                // Auto-Download Engine: If newer build is detected, initiate background download
                val downloadedPath = downloadUpdatePackage(context, release)
                if (downloadedPath != null) {
                    _downloadedFileFlow.value = downloadedPath
                    _statusFlow.value = SystemUpdateStatus.READY_TO_INSTALL
                    CheckResult.UpdateReady(release, downloadedPath)
                } else {
                    _statusFlow.value = SystemUpdateStatus.UPDATE_AVAILABLE
                    CheckResult.UpdateFound(release)
                }
            } else {
                _statusFlow.value = SystemUpdateStatus.UP_TO_DATE
                CheckResult.UpToDate
            }
        } catch (_: Exception) {
            // Check fallback: if downloaded package already exists in internal storage
            val updatesDir = File(context.filesDir, "updates")
            val existing = updatesDir.listFiles()?.firstOrNull { it.isFile && it.length() > 0 }
            if (existing != null) {
                _downloadedFileFlow.value = existing.absolutePath
                _statusFlow.value = SystemUpdateStatus.READY_TO_INSTALL
                CheckResult.UpdateReady(
                    UpdateRelease(
                        version = "2.2.0-BETA",
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
                _statusFlow.value = SystemUpdateStatus.UP_TO_DATE
                CheckResult.UpToDate
            }
        }
    }

    /**
     * Downloads the binary payload safely into internal storage.
     */
    suspend fun downloadUpdatePackage(context: Context, release: UpdateRelease): String? = withContext(dispatchers.io) {
        if (release.zipUrl.isBlank()) return@withContext null
        _statusFlow.value = SystemUpdateStatus.DOWNLOADING
        _downloadProgressFlow.value = 0

        val updatesDir = File(context.filesDir, "updates").apply { mkdirs() }
        val targetFile = File(updatesDir, "PowerOS_update_${release.versionCode}.zip")

        try {
            val request = Request.Builder().url(release.zipUrl).build()
            val response = httpClient.newCall(request).execute()

            if (!response.isSuccessful) {
                return@withContext null
            }

            val body = response.body ?: return@withContext null
            val contentLength = body.contentLength()
            val inputStream = body.byteStream()
            val outputStream = FileOutputStream(targetFile)

            val buffer = ByteArray(8192)
            var totalBytesRead = 0L
            var bytesRead: Int

            inputStream.use { input ->
                outputStream.use { output ->
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        totalBytesRead += bytesRead
                        if (contentLength > 0) {
                            val progress = ((totalBytesRead * 100) / contentLength).toInt()
                            _downloadProgressFlow.value = progress
                        }
                    }
                }
            }

            if (targetFile.exists() && targetFile.length() > 0) {
                _downloadProgressFlow.value = 100
                _downloadedFileFlow.value = targetFile.absolutePath
                _statusFlow.value = SystemUpdateStatus.READY_TO_INSTALL
                targetFile.absolutePath
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun fetchManifestString(url: String): String {
        val request = Request.Builder().url(url).build()
        httpClient.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                return response.body?.string().orEmpty()
            }
        }
        return ""
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
                if (channel == ReleaseChannel.BETA && channels.has("beta")) {
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
                version = releaseObj.optString("version", "2.1.0-BETA"),
                versionCode = releaseObj.optInt("versionCode", 210),
                releaseDate = releaseObj.optString("releaseDate", ""),
                size = releaseObj.optString("size", ""),
                zipUrl = releaseObj.optString("zipUrl", ""),
                changelog = releaseObj.optString("changelog", ""),
                channel = channel.tag
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

            val version = extractString("version").ifEmpty { "2.1.0-BETA" }
            val versionCode = extractInt("versionCode")
            val releaseDate = extractString("releaseDate")
            val size = extractString("size")
            val zipUrl = extractString("zipUrl")
            val changelog = extractString("changelog")

            UpdateRelease(
                version = version,
                versionCode = versionCode,
                releaseDate = releaseDate,
                size = size,
                zipUrl = zipUrl,
                changelog = changelog,
                channel = channel.tag
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
