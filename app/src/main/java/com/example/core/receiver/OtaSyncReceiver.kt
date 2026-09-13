package com.example.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.core.engine.OtaEngine
import com.example.core.model.ReleaseChannel
import com.example.core.model.SystemAnnouncement
import com.example.core.model.SystemUpdateStatus
import com.example.core.model.UpdateRelease
import com.example.core.persistence.OtaStateStore
import org.json.JSONObject

/**
 * Real-Time Sync Receiver connecting OTA_Admin publishing with OTA_Updater.
 * Captures broadcast dispatches from OTA_Admin and immediately applies
 * remote announcements and release payloads without restarting the app.
 */
class OtaSyncReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_OTA_SYNC = "com.poweros.updater.ACTION_OTA_SYNC"
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_PAYLOAD_JSON = "extra_payload_json"
        const val TYPE_ANNOUNCEMENT = "ANNOUNCEMENT"
        const val TYPE_RELEASE = "RELEASE"
        private const val TAG = "OtaSyncReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_OTA_SYNC) return

        val type = intent.getStringExtra(EXTRA_TYPE) ?: ""
        val jsonPayload = intent.getStringExtra(EXTRA_PAYLOAD_JSON) ?: ""

        try {
            when (type.uppercase()) {
                TYPE_ANNOUNCEMENT -> {
                    val title = intent.getStringExtra("extra_title")
                        ?: JSONObject(jsonPayload).optString("title", "Fleet Update")
                    val message = intent.getStringExtra("extra_message")
                        ?: JSONObject(jsonPayload).optString("message", "")
                    val date = intent.getStringExtra("extra_date")
                        ?: JSONObject(jsonPayload).optString("timestamp", "Just now")

                    val announcement = SystemAnnouncement(title = title, date = date, message = message)
                    OtaEngine.setLiveAnnouncement(announcement)
                    OtaStateStore.saveAnnouncement(context, announcement)
                    // Logging stripped for production
                }

                TYPE_RELEASE -> {
                    val jsonObj = if (jsonPayload.isNotBlank()) JSONObject(jsonPayload) else JSONObject()
                    val channelStr = intent.getStringExtra("extra_channel")
                        ?: jsonObj.optString("channel", "beta")
                    val version = intent.getStringExtra("extra_version")
                        ?: jsonObj.optString("version", "2.2.0-RELEASE")
                    val versionCode = intent.getIntExtra("extra_code", 0).let {
                        if (it != 0) it else jsonObj.optInt("versionCode", 220)
                    }
                    val zipUrl = intent.getStringExtra("extra_zip_url")
                        ?: jsonObj.optString("zipUrl", "")
                    val sha256 = intent.getStringExtra("extra_sha256")
                        ?: jsonObj.optString("sha256", "")
                    val changelog = intent.getStringExtra("extra_changelog")
                        ?: jsonObj.optString("changelog", "Automated deployment from Power OS Admin Console")
                    val releaseDate = intent.getStringExtra("extra_date")
                        ?: jsonObj.optString("deployedAt", "2026-09-15")

                    val channel = if (channelStr.equals("stable", ignoreCase = true)) {
                        ReleaseChannel.STABLE
                    } else {
                        ReleaseChannel.STABLE
                    }

                    val release = UpdateRelease(
                        version = version,
                        versionCode = versionCode,
                        releaseDate = releaseDate,
                        size = "25 MB",
                        zipUrl = zipUrl,
                        changelog = changelog,
                        sha256 = sha256
                    )

                    OtaEngine.setLiveRelease(channel, release)
                    if (versionCode > OtaEngine.CURRENT_VERSION_CODE) {
                        OtaEngine.setStatus(SystemUpdateStatus.UPDATE_AVAILABLE)
                    } else if (versionCode < OtaEngine.CURRENT_VERSION_CODE || changelog.contains("ROLLBACK")) {
                        OtaEngine.setStatus(SystemUpdateStatus.UP_TO_DATE)
                        java.io.File(context.filesDir, "updates").deleteRecursively()
                    }
                    OtaStateStore.saveLatestRelease(context, release)
                    // Logging stripped for production
                }
            }
        } catch (e: Exception) {
            // Error logs stripped for production
        }
    }
}
