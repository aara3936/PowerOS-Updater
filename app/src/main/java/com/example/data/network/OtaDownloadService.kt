package com.example.data.network

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.data.model.DownloadProgress
import com.example.data.model.DownloadStatus
import com.example.data.model.OtaRelease
import com.example.data.nativecore.PowerOsNativeCore
import com.example.telemetry.AiTelemetryEngine
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.RandomAccessFile

class OtaDownloadService : Service() {

    companion object {
        const val CHANNEL_ID = "poweros_ota_download_channel"
        const val NOTIFICATION_ID = 1001

        const val ACTION_START = "com.example.ota.ACTION_START"
        const val ACTION_PAUSE = "com.example.ota.ACTION_PAUSE"
        const val ACTION_RESUME = "com.example.ota.ACTION_RESUME"
        const val ACTION_CANCEL = "com.example.ota.ACTION_CANCEL"

        const val EXTRA_RELEASE = "extra_release"

        private val _serviceProgress = MutableStateFlow(DownloadProgress())
        val serviceProgress: StateFlow<DownloadProgress> = _serviceProgress.asStateFlow()

        fun updateProgress(updater: (DownloadProgress) -> DownloadProgress) {
            _serviceProgress.update(updater)
        }

        fun startDownload(context: Context, release: OtaRelease) {
            val intent = Intent(context, OtaDownloadService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_RELEASE, release)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun pauseDownload(context: Context) {
            val intent = Intent(context, OtaDownloadService::class.java).apply {
                action = ACTION_PAUSE
            }
            context.startService(intent)
        }

        fun resumeDownload(context: Context, release: OtaRelease) {
            val intent = Intent(context, OtaDownloadService::class.java).apply {
                action = ACTION_RESUME
                putExtra(EXTRA_RELEASE, release)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun cancelDownload(context: Context) {
            val intent = Intent(context, OtaDownloadService::class.java).apply {
                action = ACTION_CANCEL
            }
            context.startService(intent)
        }
    }

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var downloadJob: Job? = null
    private var activeRelease: OtaRelease? = null
    private var isPaused = false

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val release = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(EXTRA_RELEASE, OtaRelease::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(EXTRA_RELEASE)
                }
                if (release != null) {
                    activeRelease = release
                    isPaused = false
                    startForegroundWithNotification(release.versionName, 0, "Connecting...")
                    launchDownload(release, resume = false)
                }
            }
            ACTION_RESUME -> {
                val release = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(EXTRA_RELEASE, OtaRelease::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(EXTRA_RELEASE)
                } ?: activeRelease

                if (release != null) {
                    activeRelease = release
                    isPaused = false
                    startForegroundWithNotification(release.versionName, (_serviceProgress.value.progress * 100).toInt(), "Resuming...")
                    launchDownload(release, resume = true)
                }
            }
            ACTION_PAUSE -> {
                isPaused = true
                downloadJob?.cancel()
                _serviceProgress.value = _serviceProgress.value.copy(
                    status = DownloadStatus.PAUSED,
                    currentStep = "Download paused"
                )
                updateNotification(
                    activeRelease?.versionName ?: "Power OS",
                    (_serviceProgress.value.progress * 100).toInt(),
                    "Download Paused",
                    showResume = true
                )
            }
            ACTION_CANCEL -> {
                downloadJob?.cancel()
                _serviceProgress.value = DownloadProgress(status = DownloadStatus.IDLE)
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    private fun launchDownload(release: OtaRelease, resume: Boolean) {
        downloadJob?.cancel()
        downloadJob = serviceScope.launch {
            val destinationFile = NetworkUtils.resolveTargetRomFile(release.targetLocalPath, applicationContext)
            val targetUrl = release.downloadUrl.ifBlank {
                "https://github.com/aara3936/Oppo-A6X-OTA/releases/download/v3.7.90/PowerOS_v3.7.90_OppoA6X.zip"
            }

            var downloadedBytes = if (resume && destinationFile.exists()) destinationFile.length() else 0L
            if (!resume && destinationFile.exists()) {
                destinationFile.delete()
                downloadedBytes = 0L
            }

            var totalBytes = release.packageSizeBytes.coerceAtLeast(15728640L)
            var lastEmitTime = System.currentTimeMillis()
            var bytesSinceLastEmit = 0L

            try {
                _serviceProgress.value = DownloadProgress(
                    status = DownloadStatus.DOWNLOADING,
                    progress = if (totalBytes > 0) downloadedBytes.toFloat() / totalBytes else 0f,
                    downloadedBytes = downloadedBytes,
                    totalBytes = totalBytes,
                    currentStep = "Connecting to CDN...",
                    destinationPath = destinationFile.absolutePath
                )

                val requestBuilder = Request.Builder()
                    .url(targetUrl)
                    .header("User-Agent", "PowerOS-OTA-Downloader/2.0-BETA")

                if (resume && downloadedBytes > 0) {
                    requestBuilder.header("Range", "bytes=$downloadedBytes-")
                }

                NetworkUtils.okHttpClient.newCall(requestBuilder.build()).execute().use { response ->
                    if (!response.isSuccessful && response.code != 206) {
                        throw Exception("HTTP ${response.code}: ${response.message}")
                    }

                    val body = response.body ?: throw Exception("Empty response body")
                    val serverLength = body.contentLength()
                    if (serverLength > 0) {
                        totalBytes = if (resume && response.code == 206) downloadedBytes + serverLength else serverLength
                    }

                    destinationFile.parentFile?.mkdirs()
                    val inputStream: InputStream = body.byteStream()
                    val randomAccessFile = RandomAccessFile(destinationFile, "rw")
                    if (resume) {
                        randomAccessFile.seek(downloadedBytes)
                    } else {
                        randomAccessFile.setLength(0)
                    }

                    val buffer = ByteArray(32768)
                    var bytesRead = 0

                    while (isActive && inputStream.read(buffer).also { bytesRead = it } != -1) {
                        randomAccessFile.write(buffer, 0, bytesRead)
                        downloadedBytes += bytesRead
                        bytesSinceLastEmit += bytesRead

                        val now = System.currentTimeMillis()
                        val timeDelta = now - lastEmitTime
                        if (timeDelta >= 250L || downloadedBytes >= totalBytes) {
                            val speed = if (timeDelta > 0) (bytesSinceLastEmit * 1000L) / timeDelta else 0L
                            val remainingBytes = (totalBytes - downloadedBytes).coerceAtLeast(0L)
                            val eta = if (speed > 0) remainingBytes / speed else 0L
                            val progress = if (totalBytes > 0) downloadedBytes.toFloat() / totalBytes else 0f

                            val progressObj = DownloadProgress(
                                status = DownloadStatus.DOWNLOADING,
                                progress = progress.coerceIn(0f, 1f),
                                downloadedBytes = downloadedBytes,
                                totalBytes = totalBytes,
                                speedBytesPerSec = speed,
                                etaSeconds = eta,
                                currentStep = "Downloading (${(downloadedBytes / (1024 * 1024))} MB / ${(totalBytes / (1024 * 1024))} MB)",
                                destinationPath = destinationFile.absolutePath,
                                actualFileSize = downloadedBytes
                            )
                            _serviceProgress.value = progressObj

                            val speedMb = speed.toFloat() / (1024 * 1024)
                            updateNotification(
                                release.versionName,
                                (progress * 100).toInt(),
                                String.format("%.1f MB/s • ETA: %ds", speedMb, eta),
                                showResume = false
                            )

                            lastEmitTime = now
                            bytesSinceLastEmit = 0L
                        }
                    }

                    randomAccessFile.close()
                    inputStream.close()
                }

                if (!isActive || isPaused) return@launch

                // Verification step
                _serviceProgress.value = DownloadProgress(
                    status = DownloadStatus.VERIFYING,
                    progress = 1f,
                    downloadedBytes = downloadedBytes,
                    totalBytes = totalBytes,
                    currentStep = "Verifying cryptographic SHA-256 integrity...",
                    destinationPath = destinationFile.absolutePath,
                    actualFileSize = destinationFile.length()
                )
                updateNotification(release.versionName, 100, "Verifying package integrity...", showResume = false)

                val isValid = if (release.checksumSha256.isNotBlank()) {
                    PowerOsNativeCore.verifySha256(destinationFile, release.checksumSha256)
                } else {
                    destinationFile.exists() && destinationFile.length() > 0
                }

                if (isValid) {
                    _serviceProgress.value = DownloadProgress(
                        status = DownloadStatus.READY_TO_INSTALL,
                        progress = 1f,
                        downloadedBytes = downloadedBytes,
                        totalBytes = totalBytes,
                        currentStep = "Package ready for installation.",
                        destinationPath = destinationFile.absolutePath,
                        actualFileSize = destinationFile.length()
                    )
                    updateNotification(release.versionName, 100, "Download completed. Ready to install.", showResume = false)
                } else {
                    throw Exception("SHA-256 signature verification failed")
                }
            } catch (e: Exception) {
                if (!isPaused) {
                    AiTelemetryEngine.logCrash(e)
                    _serviceProgress.value = DownloadProgress(
                        status = DownloadStatus.FAILED,
                        errorMessage = e.localizedMessage ?: "Download failed",
                        currentStep = "Error: ${e.localizedMessage}",
                        destinationPath = destinationFile.absolutePath
                    )
                    updateNotification(release.versionName, 0, "Download failed: ${e.localizedMessage}", showResume = true)
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Power OS System Updates",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows progress for downloading Power OS system updates."
                setShowBadge(false)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun startForegroundWithNotification(versionName: String, progress: Int, contentText: String) {
        val notification = buildNotification(versionName, progress, contentText, showResume = false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun updateNotification(versionName: String, progress: Int, contentText: String, showResume: Boolean) {
        val notification = buildNotification(versionName, progress, contentText, showResume)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        manager?.notify(NOTIFICATION_ID, notification)
    }

    private fun buildNotification(
        versionName: String,
        progress: Int,
        contentText: String,
        showResume: Boolean
    ): android.app.Notification {
        val mainIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val mainPendingIntent = PendingIntent.getActivity(
            this, 0, mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val pauseIntent = Intent(this, OtaDownloadService::class.java).apply { action = ACTION_PAUSE }
        val pausePendingIntent = PendingIntent.getService(
            this, 1, pauseIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val resumeIntent = Intent(this, OtaDownloadService::class.java).apply { action = ACTION_RESUME }
        val resumePendingIntent = PendingIntent.getService(
            this, 2, resumeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val cancelIntent = Intent(this, OtaDownloadService::class.java).apply { action = ACTION_CANCEL }
        val cancelPendingIntent = PendingIntent.getService(
            this, 3, cancelIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Power OS Update ($versionName)")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentIntent(mainPendingIntent)
            .setOngoing(!showResume && progress < 100)
            .setOnlyAlertOnce(true)

        if (progress in 0..100) {
            builder.setProgress(100, progress, false)
        }

        if (showResume) {
            builder.addAction(android.R.drawable.ic_media_play, "Resume", resumePendingIntent)
        } else if (progress < 100) {
            builder.addAction(android.R.drawable.ic_media_pause, "Pause", pausePendingIntent)
        }
        builder.addAction(android.R.drawable.ic_delete, "Cancel", cancelPendingIntent)

        return builder.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
