package com.example.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Environment
import com.example.data.model.OtaConstants
import com.example.data.model.OtaRelease
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.security.MessageDigest
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * Network and I/O utility responsible for querying the live GitHub OTA repository
 * and managing downloads to the Oppo A6X target directory (/sdcard/Download/OTA/rom.zip).
 */
object NetworkUtils {

    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }

    /**
     * Checks if the device has an active internet connection.
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return true
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /**
     * Resolves the target destination file for the Oppo A6X ROM.
     * Guaranteed to target /sdcard/Download/OTA/rom.zip or primary storage equivalent.
     */
    fun resolveTargetRomFile(): File {
        val directPath = File(OtaConstants.DEFAULT_TARGET_FILE_PATH)
        val parentDir = directPath.parentFile
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs()
        }

        // Check if direct /sdcard path is writable; if not fallback to external storage downloads
        if (parentDir != null && parentDir.canWrite()) {
            return directPath
        }

        val publicDownloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val otaDir = File(publicDownloads, "OTA")
        if (!otaDir.exists()) {
            otaDir.mkdirs()
        }
        return File(otaDir, OtaConstants.DEFAULT_TARGET_FILENAME)
    }

    /**
     * Fetches and parses the live OTA JSON from GitHub raw repository:
     * Tries primary URL (metadata.json), and falls back to updater.json if primary fails.
     */
    suspend fun fetchOtaManifest(
        rawUrl: String = OtaConstants.DEFAULT_RAW_JSON_URL
    ): Result<List<OtaRelease>> = withContext(Dispatchers.IO) {
        val urlsToTry = if (rawUrl != OtaConstants.DEFAULT_FALLBACK_JSON_URL) {
            listOf(rawUrl, OtaConstants.DEFAULT_FALLBACK_JSON_URL)
        } else {
            listOf(rawUrl)
        }

        var lastError: Exception? = null
        for (currentUrl in urlsToTry) {
            try {
                val request = Request.Builder()
                    .url(currentUrl)
                    .addHeader("User-Agent", "PowerOS-OppoA6X-OTA/2.0")
                    .addHeader("Accept", "application/json, text/plain, */*")
                    .build()

                val response = okHttpClient.newCall(request).execute()
                if (!response.isSuccessful) {
                    val code = response.code
                    response.close()
                    lastError = IllegalStateException("GitHub OTA server returned HTTP $code for $currentUrl")
                    continue
                }

                val bodyString = response.body?.string().orEmpty()
                response.close()

                if (bodyString.isBlank()) {
                    lastError = IllegalStateException("Empty response from $currentUrl")
                    continue
                }

                val parsedReleases = parseOtaJson(bodyString, currentUrl)
                if (parsedReleases.isNotEmpty()) {
                    return@withContext Result.success(parsedReleases)
                } else {
                    lastError = IllegalStateException("No valid releases found in $currentUrl")
                }
            } catch (e: Exception) {
                lastError = e
            }
        }
        Result.failure(lastError ?: IllegalStateException("Unable to fetch manifest"))
    }

    /**
     * Parses raw JSON string into a list of [OtaRelease].
     * Supports:
     * - Nested channel dictionary objects (e.g. "stable", "early_access", "closed_beta", "beta")
     * - Arrays of releases
     * - Wrapped responses ("response", "updates", "releases")
     * - Single release objects at the root
     */
    fun parseOtaJson(jsonString: String, sourceUrl: String = OtaConstants.DEFAULT_RAW_JSON_URL): List<OtaRelease> {
        val releases = mutableListOf<OtaRelease>()
        try {
            val trimmed = jsonString.trim()
            if (trimmed.startsWith("[")) {
                val array = JSONArray(trimmed)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    parseSingleReleaseObject(obj, sourceUrl)?.let { releases.add(it) }
                }
            } else if (trimmed.startsWith("{")) {
                val root = JSONObject(trimmed)

                // 1. Check for channel-specific nested objects
                val channelKeys = listOf(
                    "stable" to "Stable",
                    "early_access" to "Early Access",
                    "earlyAccess" to "Early Access",
                    "closed_beta" to "Closed Beta",
                    "closedBeta" to "Closed Beta",
                    "beta" to "Early Access",
                    "alpha" to "Closed Beta"
                )
                var parsedAnyChannel = false
                for ((key, channelName) in channelKeys) {
                    if (root.has(key)) {
                        val channelObj = root.optJSONObject(key)
                        if (channelObj != null) {
                            parseSingleReleaseObject(channelObj, sourceUrl, defaultChannel = channelName)?.let {
                                releases.add(it)
                                parsedAnyChannel = true
                            }
                        }
                    }
                }

                if (!parsedAnyChannel) {
                    if (root.has("response")) {
                        val responseArray = root.getJSONArray("response")
                        for (i in 0 until responseArray.length()) {
                            val obj = responseArray.getJSONObject(i)
                            parseSingleReleaseObject(obj, sourceUrl)?.let { releases.add(it) }
                        }
                    } else if (root.has("updates")) {
                        val updatesArray = root.getJSONArray("updates")
                        for (i in 0 until updatesArray.length()) {
                            val obj = updatesArray.getJSONObject(i)
                            parseSingleReleaseObject(obj, sourceUrl)?.let { releases.add(it) }
                        }
                    } else if (root.has("releases")) {
                        val releasesArray = root.getJSONArray("releases")
                        for (i in 0 until releasesArray.length()) {
                            val obj = releasesArray.getJSONObject(i)
                            parseSingleReleaseObject(obj, sourceUrl)?.let { releases.add(it) }
                        }
                    } else {
                        // Single release object at root
                        parseSingleReleaseObject(root, sourceUrl)?.let { releases.add(it) }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return releases
    }

    private fun parseSingleReleaseObject(
        obj: JSONObject,
        sourceUrl: String,
        defaultChannel: String = "Stable"
    ): OtaRelease? {
        return try {
            val versionName = when {
                obj.has("version_name") -> obj.optString("version_name")
                obj.has("version") -> {
                    val rawVer = obj.optString("version")
                    if (rawVer.startsWith("Power", ignoreCase = true)) rawVer else "Power OS v$rawVer"
                }
                obj.has("name") -> obj.optString("name")
                else -> "Power OS System Update"
            }

            val versionCode = when {
                obj.has("versionCode") -> obj.optInt("versionCode")
                obj.has("version_code") -> obj.optInt("version_code")
                obj.has("version") -> {
                    val digits = obj.optString("version").replace(".", "").filter { it.isDigit() }
                    digits.toIntOrNull() ?: 1000
                }
                else -> 1000
            }

            val buildNumber = obj.optString(
                "build_number",
                obj.optString("build", "POS-$versionCode-STABLE-OppoA6X")
            )

            val deviceModel = obj.optString(
                "device_model",
                obj.optString("device", OtaConstants.DEVICE_MODEL_NAME)
            )

            val releaseChannel = obj.optString(
                "release_channel",
                obj.optString("channel", if (obj.optString("romtype") == "beta") "Beta" else defaultChannel)
            )

            val releaseType = obj.optString("release_type", obj.optString("type", "Full OTA Package"))

            val packageSizeBytes = when {
                obj.has("package_size_bytes") -> obj.optLong("package_size_bytes")
                obj.has("size") -> obj.optLong("size")
                obj.has("filesize") -> obj.optLong("filesize")
                obj.has("packageSize") -> obj.optLong("packageSize")
                else -> 0L
            }

            val downloadUrl = when {
                obj.has("zipUrl") -> obj.optString("zipUrl")
                obj.has("download_url") -> obj.optString("download_url")
                obj.has("url") -> obj.optString("url")
                obj.has("downloadUrl") -> obj.optString("downloadUrl")
                else -> ""
            }

            if (downloadUrl.isBlank()) {
                return null
            }

            val checksumSha256 = when {
                obj.has("checksum_sha256") -> obj.optString("checksum_sha256")
                obj.has("sha256") -> obj.optString("sha256")
                obj.has("checksum") -> obj.optString("checksum")
                obj.has("md5") -> obj.optString("md5")
                else -> ""
            }

            val changelog = when {
                obj.has("changelog") -> obj.optString("changelog")
                obj.has("notes") -> obj.optString("notes")
                obj.has("description") -> obj.optString("description")
                else -> "System update for Oppo A6X with performance and stability improvements."
            }

            val securityPatch = obj.optString("security_patch", obj.optString("securityPatch", "2026-08-05"))
            val releaseDate = obj.optLong("datetime", obj.optLong("release_date", System.currentTimeMillis()))
            val id = obj.optString("id", "oppoa6x_v${versionCode}")

            OtaRelease(
                id = id,
                deviceModel = if (deviceModel.isNotBlank()) deviceModel else OtaConstants.DEVICE_MODEL_NAME,
                deviceCodename = OtaConstants.DEVICE_CODENAME,
                versionName = versionName,
                versionCode = versionCode,
                buildNumber = buildNumber,
                releaseChannel = releaseChannel,
                releaseType = releaseType,
                packageSizeBytes = packageSizeBytes,
                downloadUrl = downloadUrl,
                checksumSha256 = checksumSha256,
                androidVersion = "Android 15",
                securityPatch = securityPatch,
                changelog = changelog,
                releaseDate = releaseDate,
                isMandatory = obj.optBoolean("is_mandatory", obj.optBoolean("isMandatory", false)),
                minRequiredVersion = obj.optInt("min_required_version", 0),
                status = "PUBLISHED",
                rolloutPercentage = 100,
                sourceUrl = sourceUrl,
                targetLocalPath = OtaConstants.DEFAULT_TARGET_FILE_PATH
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Computes the SHA-256 checksum of a local file.
     */
    fun computeFileSha256(file: File): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            file.inputStream().use { stream ->
                val buffer = ByteArray(8192)
                var read: Int
                while (stream.read(buffer).also { read = it } > 0) {
                    digest.update(buffer, 0, read)
                }
            }
            digest.digest().joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            ""
        }
    }
}
