package com.example.core.repository

import android.content.Context
import com.example.core.dispatcher.DispatcherProvider
import com.example.core.model.AppEngineState
import com.example.core.model.ManifestResponse
import com.example.core.model.ReleaseChannel
import com.example.core.model.SystemAnnouncement
import com.example.core.model.SystemUpdateStatus
import com.example.core.model.UpdateRelease
import com.example.core.persistence.OtaStateStore
import com.google.gson.Gson
import kotlinx.coroutines.withContext
import org.json.JSONObject

class SystemCoreRepository(
    private val dispatchers: DispatcherProvider
) {
    private val gson by lazy { Gson() }

    companion object {
        const val CURRENT_VERSION_CODE = 210
        const val CURRENT_VERSION_NAME = "2.1.0-RELEASE"
        const val DEFAULT_APP_NAME = "Power OS Updater"
        const val DEFAULT_REMOTE_VERSION = "4.09STABLEE1R3S09(EXO001) PK"
        const val DEFAULT_REMOTE_VERSION_CODE = 9999
    }

    /**
     * Initializes core environment parameters strictly on Dispatchers.IO
     * to eliminate UI thread blocking. Restores persisted update state if active.
     */
    suspend fun loadInitialCoreState(context: Context? = null): AppEngineState = withContext(dispatchers.io) {
        val baseState = AppEngineState(
            appName = DEFAULT_APP_NAME,
            versionName = CURRENT_VERSION_NAME,
            versionCode = CURRENT_VERSION_CODE,
            isInitialized = true,
            renderPipelineActive = true,
            hardwareAccelerated = true,
            displayRefreshRateHz = 60.0f,
            targetFrameTimeBudgetMs = 16.67f,
            lifecycleStateDescription = "Initialized on Dispatchers.IO"
        )

        if (context != null) {
            val cached = OtaStateStore.loadState(context)
            if (cached.latestRelease != null && isUpdateAvailable(cached.latestRelease, CURRENT_VERSION_CODE)) {
                return@withContext baseState.copy(
                    updateStatus = if (cached.updateStatus == SystemUpdateStatus.READY_TO_INSTALL) {
                        SystemUpdateStatus.READY_TO_INSTALL
                    } else if (cached.updateStatus == SystemUpdateStatus.DOWNLOADING) {
                        SystemUpdateStatus.DOWNLOADING
                    } else {
                        SystemUpdateStatus.UPDATE_AVAILABLE
                    },
                    currentChannel = cached.currentChannel,
                    latestRelease = cached.latestRelease,
                    downloadProgress = cached.downloadProgress,
                    downloadedFilePath = cached.downloadedFilePath,
                    announcement = cached.announcement
                )
            }
        }
        baseState
    }

    /**
     * Computes target frame budget on Dispatchers.Default:
     * e.g., 60Hz -> ~16.6ms, 90Hz -> ~11.1ms, 120Hz -> ~8.33ms
     */
    suspend fun calculateFrameBudget(refreshRateHz: Float): Float = withContext(dispatchers.default) {
        val safeRate = if (refreshRateHz > 0f) refreshRateHz else 60.0f
        1000.0f / safeRate
    }

    /**
     * Parses updater.json using @SerializedName annotations.
     * Supports both snake_case ("version_code", "version_name") and camelCase ("versionCode", "version").
     */
    suspend fun parseUpdateRelease(
        jsonString: String,
        channel: ReleaseChannel = ReleaseChannel.STABLE
    ): UpdateRelease? = withContext(dispatchers.default) {
        if (jsonString.isBlank()) return@withContext null
        try {
            val manifest = gson.fromJson(jsonString, ManifestResponse::class.java)

            // 1. Channel in channels map
            val channelRelease = manifest.channels?.get(channel.tag)
                ?: manifest.channels?.get(if (channel == ReleaseChannel.STABLE) "stable" else "beta")
            if (channelRelease != null && channelRelease.versionCode > 0) {
                return@withContext channelRelease.copy(channel = channel.tag)
            }

            // 2. Direct channel objects (stable / beta)
            val direct = if (channel == ReleaseChannel.BETA) manifest.beta else manifest.stable
            if (direct != null && direct.versionCode > 0) {
                return@withContext direct.copy(channel = channel.tag)
            }

            // 3. Fallback to alternative channel if target not explicitly defined
            if (channel == ReleaseChannel.STABLE && manifest.stable != null && manifest.stable.versionCode > 0) {
                return@withContext manifest.stable.copy(channel = channel.tag)
            } else if (channel == ReleaseChannel.BETA && manifest.beta != null && manifest.beta.versionCode > 0) {
                return@withContext manifest.beta.copy(channel = channel.tag)
            }

            // 4. Root level properties with @SerializedName
            if (manifest.versionCode != null && manifest.versionCode > 0) {
                return@withContext UpdateRelease(
                    version = manifest.version ?: "${manifest.versionCode}.0.0-RELEASE",
                    versionCode = manifest.versionCode,
                    releaseDate = manifest.releaseDate.orEmpty(),
                    size = manifest.size.orEmpty(),
                    zipUrl = manifest.zipUrl.orEmpty(),
                    changelog = manifest.changelog.orEmpty(),
                    channel = channel.tag,
                    sha256 = manifest.sha256.orEmpty(),
                    freeze = manifest.freeze ?: false
                )
            }
        } catch (_: Exception) {}

        // Fallback manual JSON parser for non-standard JSON envelopes
        parseReleaseManual(jsonString, channel)
    }

    fun parseAnnouncement(jsonString: String): SystemAnnouncement? {
        if (jsonString.isBlank()) return null
        return try {
            val manifest = gson.fromJson(jsonString, ManifestResponse::class.java)
            manifest.announcement
        } catch (_: Exception) {
            try {
                val root = JSONObject(jsonString)
                if (root.has("announcement")) {
                    val ann = root.getJSONObject("announcement")
                    SystemAnnouncement(
                        title = ann.optString("title", "System Announcement"),
                        date = ann.optString("date", ""),
                        message = ann.optString("message", "")
                    )
                } else null
            } catch (_: Exception) {
                null
            }
        }
    }

    fun isUpdateAvailable(release: UpdateRelease, currentVersionCode: Int = CURRENT_VERSION_CODE): Boolean {
        return release.versionCode > currentVersionCode && !release.freeze
    }

    private fun parseReleaseManual(jsonString: String, channel: ReleaseChannel): UpdateRelease? {
        return try {
            val root = JSONObject(jsonString)
            val releaseObj = if (root.has("channels")) {
                val channels = root.getJSONObject("channels")
                if (channel == ReleaseChannel.BETA && channels.has("beta")) {
                    channels.getJSONObject("beta")
                } else if (channels.has("stable")) {
                    channels.getJSONObject("stable")
                } else if (channels.has("beta")) {
                    channels.getJSONObject("beta")
                } else {
                    root
                }
            } else if (channel == ReleaseChannel.BETA && root.has("beta")) {
                root.getJSONObject("beta")
            } else if (root.has("stable")) {
                root.getJSONObject("stable")
            } else {
                root
            }

            val version = when {
                releaseObj.has("version_name") -> releaseObj.optString("version_name")
                releaseObj.has("version") -> releaseObj.optString("version")
                releaseObj.has("versionName") -> releaseObj.optString("versionName")
                else -> DEFAULT_REMOTE_VERSION
            }

            val versionCode = when {
                releaseObj.has("version_code") -> releaseObj.optInt("version_code", DEFAULT_REMOTE_VERSION_CODE)
                releaseObj.has("versionCode") -> releaseObj.optInt("versionCode", DEFAULT_REMOTE_VERSION_CODE)
                else -> DEFAULT_REMOTE_VERSION_CODE
            }

            val releaseDate = when {
                releaseObj.has("release_date") -> releaseObj.optString("release_date")
                releaseObj.has("releaseDate") -> releaseObj.optString("releaseDate")
                else -> ""
            }

            val zipUrl = when {
                releaseObj.has("zip_url") -> releaseObj.optString("zip_url")
                releaseObj.has("zipUrl") -> releaseObj.optString("zipUrl")
                releaseObj.has("url") -> releaseObj.optString("url")
                releaseObj.has("download_url") -> releaseObj.optString("download_url")
                else -> ""
            }

            val size = releaseObj.optString("size", "")
            val changelog = releaseObj.optString("changelog", "")
            val sha256 = releaseObj.optString("sha256", "")
            val freeze = releaseObj.optBoolean("freeze", false)

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
}
