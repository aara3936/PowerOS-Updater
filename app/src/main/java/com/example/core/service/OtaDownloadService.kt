package com.example.core.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.core.engine.OtaEngine
import com.example.core.model.ReleaseChannel
import com.example.core.model.SystemUpdateStatus
import com.example.core.model.UpdateRelease
import com.example.core.persistence.OtaStateStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OtaDownloadService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    private lateinit var notificationManager: NotificationManager

    companion object {
        const val CHANNEL_ID = "power_os_ota_download_channel"
        const val NOTIFICATION_ID = 9001

        const val ACTION_START_DOWNLOAD = "com.poweros.updater.action.START_DOWNLOAD"
        const val EXTRA_VERSION = "extra_version"
        const val EXTRA_VERSION_CODE = "extra_version_code"
        const val EXTRA_ZIP_URL = "extra_zip_url"
        const val EXTRA_RELEASE_DATE = "extra_release_date"
        const val EXTRA_SIZE = "extra_size"
        const val EXTRA_CHANGELOG = "extra_changelog"
        const val EXTRA_CHANNEL = "extra_channel"
        const val EXTRA_SHA256 = "extra_sha256"

        fun startDownload(context: Context, release: UpdateRelease) {
            val intent = Intent(context, OtaDownloadService::class.java).apply {
                action = ACTION_START_DOWNLOAD
                putExtra(EXTRA_VERSION, release.version)
                putExtra(EXTRA_VERSION_CODE, release.versionCode)
                putExtra(EXTRA_ZIP_URL, release.zipUrl)
                putExtra(EXTRA_RELEASE_DATE, release.releaseDate)
                putExtra(EXTRA_SIZE, release.size)
                putExtra(EXTRA_CHANGELOG, release.changelog)
                putExtra(EXTRA_CHANNEL, release.channel)
                putExtra(EXTRA_SHA256, release.sha256)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_START_DOWNLOAD) {
            val version = intent.getStringExtra(EXTRA_VERSION) ?: "2.2.0-BETA"
            val versionCode = intent.getIntExtra(EXTRA_VERSION_CODE, 220)
            val zipUrl = intent.getStringExtra(EXTRA_ZIP_URL) ?: ""
            val releaseDate = intent.getStringExtra(EXTRA_RELEASE_DATE) ?: ""
            val size = intent.getStringExtra(EXTRA_SIZE) ?: ""
            val changelog = intent.getStringExtra(EXTRA_CHANGELOG) ?: ""
            val channelTag = intent.getStringExtra(EXTRA_CHANNEL) ?: "beta"
            val sha256 = intent.getStringExtra(EXTRA_SHA256) ?: ""

            val release = UpdateRelease(
                version = version,
                versionCode = versionCode,
                releaseDate = releaseDate,
                size = size,
                zipUrl = zipUrl,
                changelog = changelog,
                channel = channelTag,
                sha256 = sha256
            )

            startForeground(NOTIFICATION_ID, buildProgressNotification(version, 0))

            serviceScope.launch {
                // Monitor live download progress
                launch {
                    OtaEngine.downloadProgressFlow.collectLatest { progress ->
                        notificationManager.notify(
                            NOTIFICATION_ID,
                            buildProgressNotification(version, progress)
                        )
                    }
                }

                val resultPath = OtaEngine.downloadUpdatePackage(applicationContext, release)
                if (resultPath != null) {
                    notificationManager.notify(
                        NOTIFICATION_ID,
                        buildCompleteNotification(version)
                    )
                } else {
                    notificationManager.notify(
                        NOTIFICATION_ID,
                        buildErrorNotification(version)
                    )
                }
                stopForeground(STOP_FOREGROUND_DETACH)
                stopSelf(startId)
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Power OS Updates",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Background OTA updates & download progress"
                setShowBadge(false)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createPendingIntent(): PendingIntent {
        val launchIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getActivity(this, 0, launchIntent, flags)
    }

    private fun buildProgressNotification(version: String, progress: Int): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Downloading Power OS $version")
            .setContentText("OTA Payload download: $progress%")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setProgress(100, progress, false)
            .setOngoing(true)
            .setContentIntent(createPendingIntent())
            .build()
    }

    private fun buildCompleteNotification(version: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Power OS $version Ready")
            .setContentText("Integrity verified natively. Tap to install.")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent())
            .build()
    }

    private fun buildErrorNotification(version: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Power OS $version Download Failed")
            .setContentText("Integrity check failed or network disconnected.")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent())
            .build()
    }
}
