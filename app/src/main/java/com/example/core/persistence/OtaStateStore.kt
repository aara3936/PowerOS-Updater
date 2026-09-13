package com.example.core.persistence

import android.content.Context
import android.content.SharedPreferences
import com.example.core.model.AppEngineState
import com.example.core.model.ReleaseChannel
import com.example.core.model.SystemAnnouncement
import com.example.core.model.SystemUpdateStatus
import com.example.core.model.UpdateRelease

object OtaStateStore {
    private const val PREFS_NAME = "power_os_ota_state_prefs"

    private const val KEY_STATUS = "key_update_status"
    private const val KEY_CHANNEL = "key_channel"
    private const val KEY_PROGRESS = "key_download_progress"
    private const val KEY_FILE_PATH = "key_downloaded_file_path"
    private const val KEY_WIFI_ONLY = "key_auto_download_wifi"
    private const val KEY_INTERVAL = "key_check_interval"

    private const val KEY_VER_NAME = "key_rel_ver_name"
    private const val KEY_VER_CODE = "key_rel_ver_code"
    private const val KEY_VER_DATE = "key_rel_ver_date"
    private const val KEY_VER_SIZE = "key_rel_ver_size"
    private const val KEY_VER_URL = "key_rel_ver_url"
    private const val KEY_VER_CHANGELOG = "key_rel_ver_changelog"
    private const val KEY_VER_SHA256 = "key_rel_ver_sha256"

    private const val KEY_ANN_TITLE = "key_ann_title"
    private const val KEY_ANN_DATE = "key_ann_date"
    private const val KEY_ANN_MSG = "key_ann_msg"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveState(context: Context, state: AppEngineState) {
        val prefs = getPrefs(context)
        prefs.edit().apply {
            putString(KEY_STATUS, state.updateStatus.name)
            putString(KEY_CHANNEL, state.currentChannel.name)
            putInt(KEY_PROGRESS, state.downloadProgress)
            putString(KEY_FILE_PATH, state.downloadedFilePath)
            putBoolean(KEY_WIFI_ONLY, state.autoDownloadWifiOnly)
            putInt(KEY_INTERVAL, state.checkIntervalMinutes)

            val release = state.latestRelease
            if (release != null) {
                putString(KEY_VER_NAME, release.version)
                putInt(KEY_VER_CODE, release.versionCode)
                putString(KEY_VER_DATE, release.releaseDate)
                putString(KEY_VER_SIZE, release.size)
                putString(KEY_VER_URL, release.zipUrl)
                putString(KEY_VER_CHANGELOG, release.changelog)
                putString(KEY_VER_SHA256, release.sha256)
            }

            val ann = state.announcement
            if (ann != null) {
                putString(KEY_ANN_TITLE, ann.title)
                putString(KEY_ANN_DATE, ann.date)
                putString(KEY_ANN_MSG, ann.message)
            }
            apply()
        }
    }

    fun loadState(context: Context): AppEngineState {
        val prefs = getPrefs(context)
        val statusName = prefs.getString(KEY_STATUS, SystemUpdateStatus.UP_TO_DATE.name)
        val status = try {
            SystemUpdateStatus.valueOf(statusName ?: SystemUpdateStatus.UP_TO_DATE.name)
        } catch (_: Exception) {
            SystemUpdateStatus.UP_TO_DATE
        }

        val channelName = prefs.getString(KEY_CHANNEL, ReleaseChannel.STABLE.name)
        val channel = try {
            ReleaseChannel.valueOf(channelName ?: ReleaseChannel.STABLE.name)
        } catch (_: Exception) {
            ReleaseChannel.STABLE
        }

        val progress = prefs.getInt(KEY_PROGRESS, 0)
        val filePath = prefs.getString(KEY_FILE_PATH, null)
        val wifiOnly = prefs.getBoolean(KEY_WIFI_ONLY, true)
        val interval = prefs.getInt(KEY_INTERVAL, 15)

        val relVersion = prefs.getString(KEY_VER_NAME, null)
        val release = if (relVersion != null) {
            UpdateRelease(
                version = relVersion,
                versionCode = prefs.getInt(KEY_VER_CODE, 210),
                releaseDate = prefs.getString(KEY_VER_DATE, "").orEmpty(),
                size = prefs.getString(KEY_VER_SIZE, "").orEmpty(),
                zipUrl = prefs.getString(KEY_VER_URL, "").orEmpty(),
                changelog = prefs.getString(KEY_VER_CHANGELOG, "").orEmpty(),
                channel = channel.tag,
                sha256 = prefs.getString(KEY_VER_SHA256, "").orEmpty()
            )
        } else {
            null
        }

        val annTitle = prefs.getString(KEY_ANN_TITLE, null)
        val announcement = if (annTitle != null) {
            SystemAnnouncement(
                title = annTitle,
                date = prefs.getString(KEY_ANN_DATE, "").orEmpty(),
                message = prefs.getString(KEY_ANN_MSG, "").orEmpty()
            )
        } else {
            null
        }

        return AppEngineState(
            updateStatus = status,
            currentChannel = channel,
            downloadProgress = progress,
            downloadedFilePath = filePath,
            autoDownloadWifiOnly = wifiOnly,
            checkIntervalMinutes = interval,
            latestRelease = release,
            announcement = announcement
        )
    }

    fun saveAnnouncement(context: Context, ann: SystemAnnouncement) {
        val prefs = getPrefs(context)
        prefs.edit().apply {
            putString(KEY_ANN_TITLE, ann.title)
            putString(KEY_ANN_DATE, ann.date)
            putString(KEY_ANN_MSG, ann.message)
            apply()
        }
    }

    fun saveLatestRelease(context: Context, release: UpdateRelease) {
        val prefs = getPrefs(context)
        prefs.edit().apply {
            putString(KEY_VER_NAME, release.version)
            putInt(KEY_VER_CODE, release.versionCode)
            putString(KEY_VER_DATE, release.releaseDate)
            putString(KEY_VER_SIZE, release.size)
            putString(KEY_VER_URL, release.zipUrl)
            putString(KEY_VER_CHANGELOG, release.changelog)
            putString(KEY_VER_SHA256, release.sha256)
            if (release.versionCode > 210) {
                putString(KEY_STATUS, SystemUpdateStatus.UPDATE_AVAILABLE.name)
            }
            apply()
        }
    }
}
