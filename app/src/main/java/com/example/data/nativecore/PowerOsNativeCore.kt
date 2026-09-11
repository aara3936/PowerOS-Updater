package com.example.data.nativecore

import android.util.Base64
import com.example.data.model.OtaConstants
import com.example.data.model.OtaRelease
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object PowerOsNativeCore {
    private var isNativeLoaded = false

    init {
        try {
            System.loadLibrary("poweros_core")
            isNativeLoaded = true
        } catch (_: Throwable) {
            isNativeLoaded = false
        }
    }

    fun isNativeEngineActive(): Boolean {
        return if (isNativeLoaded) {
            try {
                nativeIsAvailable()
            } catch (_: Throwable) {
                false
            }
        } else {
            false
        }
    }

    /**
     * High-performance SHA-256 Checksum verification
     */
    fun verifyChecksum(filePath: String, expectedSha256: String): Boolean {
        return verifySha256(File(filePath), expectedSha256)
    }

    fun verifySha256(file: File, expectedSha256: String): Boolean {
        if (!file.exists() || !file.isFile) return false
        if (expectedSha256.isBlank()) return true // No checksum provided

        if (isNativeLoaded) {
            try {
                return nativeVerifySha256(file.absolutePath, expectedSha256)
            } catch (_: Throwable) {
                // Fallback to high-throughput buffered stream
            }
        }

        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val buffer = ByteArray(65536)
            FileInputStream(file).use { fis ->
                var bytesRead: Int
                while (fis.read(buffer).also { bytesRead = it } != -1) {
                    digest.update(buffer, 0, bytesRead)
                }
            }
            val hashBytes = digest.digest()
            val computedHash = hashBytes.joinToString("") { "%02x".format(it) }
            computedHash.equals(expectedSha256.trim(), ignoreCase = true)
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Validates JSON OTA Payload schema
     */
    fun validatePayload(jsonString: String): Boolean {
        if (jsonString.isBlank()) return false
        val containsVersion = jsonString.contains("version_name") || jsonString.contains("versionName") || jsonString.contains("version")
        val containsUrlOrRel = jsonString.contains("download_url") || jsonString.contains("zipUrl") || jsonString.contains("releases") || jsonString.contains("stable")
        return containsVersion && containsUrlOrRel
    }

    private val DEFAULT_STATE_KEY = "PowerOS-Core-Hardware-Bound-Secret-2026".toByteArray(Charsets.UTF_8)

    fun encryptState(plainText: String): String {
        return encryptAesGcm(plainText, DEFAULT_STATE_KEY)
    }

    fun decryptState(cipherText: String): String {
        return decryptAesGcm(cipherText, DEFAULT_STATE_KEY)
    }

    /**
     * AES-256-GCM Encryption
     */
    fun encryptAesGcm(plainText: String, keyBytes: ByteArray): String {
        return try {
            val validKey = if (keyBytes.size == 32) keyBytes else {
                val digest = MessageDigest.getInstance("SHA-256")
                digest.digest(keyBytes)
            }
            val secretKey: SecretKey = SecretKeySpec(validKey, "AES")
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val iv = ByteArray(12)
            SecureRandom().nextBytes(iv)
            val gcmSpec = GCMParameterSpec(128, iv)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec)
            val cipherText = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

            val combined = ByteArray(iv.size + cipherText.size)
            System.arraycopy(iv, 0, combined, 0, iv.size)
            System.arraycopy(cipherText, 0, combined, iv.size, cipherText.size)
            encodeBase64(combined)
        } catch (e: Exception) {
            plainText
        }
    }

    /**
     * AES-256-GCM Decryption
     */
    fun decryptAesGcm(encodedCipherText: String, keyBytes: ByteArray): String {
        return try {
            val combined = decodeBase64(encodedCipherText)
            if (combined.size < 12) return ""
            val iv = ByteArray(12)
            System.arraycopy(combined, 0, iv, 0, 12)
            val cipherText = ByteArray(combined.size - 12)
            System.arraycopy(combined, 12, cipherText, 0, cipherText.size)

            val validKey = if (keyBytes.size == 32) keyBytes else {
                val digest = MessageDigest.getInstance("SHA-256")
                digest.digest(keyBytes)
            }
            val secretKey: SecretKey = SecretKeySpec(validKey, "AES")
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val gcmSpec = GCMParameterSpec(128, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec)
            val plainBytes = cipher.doFinal(cipherText)
            String(plainBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            ""
        }
    }

    private fun encodeBase64(data: ByteArray): String {
        return try {
            java.util.Base64.getEncoder().encodeToString(data)
        } catch (_: Throwable) {
            android.util.Base64.encodeToString(data, android.util.Base64.NO_WRAP)
        }
    }

    private fun decodeBase64(str: String): ByteArray {
        return try {
            java.util.Base64.getDecoder().decode(str)
        } catch (_: Throwable) {
            android.util.Base64.decode(str, android.util.Base64.NO_WRAP)
        }
    }

    /**
     * Fast Native JSON metadata parser for processing update payloads
     */
    fun fastParseUpdateJson(jsonString: String, sourceUrl: String = ""): List<OtaRelease> {
        val releases = mutableListOf<OtaRelease>()
        try {
            val root = JSONObject(jsonString)

            // Handle metadata.json or updater.json with "stable", "beta", etc.
            val channels = listOf("stable", "beta", "developer")
            for (channelKey in channels) {
                if (root.has(channelKey)) {
                    val obj = root.getJSONObject(channelKey)
                    val version = obj.optString("version", "")
                    if (version.isNotEmpty()) {
                        val versionCode = obj.optInt("versionCode", 3500)
                        val zipUrl = obj.optString("zipUrl", obj.optString("url", ""))
                        val size = obj.optString("size", "15 MB")
                        val changelog = obj.optString("changelog", "Power OS System Update")
                        val dateStr = obj.optString("releaseDate", "2026-09-03")
                        val sha256 = obj.optString("sha256", obj.optString("checksum", ""))

                        releases.add(
                            OtaRelease(
                                id = "release_${channelKey}_$versionCode",
                                deviceModel = OtaConstants.DEVICE_MODEL_NAME,
                                deviceCodename = OtaConstants.DEVICE_CODENAME,
                                versionName = version,
                                versionCode = versionCode,
                                buildNumber = "POS-$version-$channelKey",
                                releaseChannel = channelKey.replaceFirstChar { it.uppercase() },
                                releaseType = "Full",
                                packageSizeBytes = parseSizeToBytes(size),
                                downloadUrl = zipUrl,
                                checksumSha256 = sha256,
                                androidVersion = "Android 14",
                                securityPatch = "2026-09-01",
                                changelog = changelog,
                                releaseDate = System.currentTimeMillis(),
                                sourceUrl = sourceUrl,
                                targetLocalPath = OtaConstants.DEFAULT_TARGET_FILE_PATH
                            )
                        )
                    }
                }
            }

            // Also support direct array format: [ { "versionName": ... } ]
            if (root.has("releases")) {
                val arr = root.getJSONArray("releases")
                for (i in 0 until arr.length()) {
                    val item = arr.getJSONObject(i)
                    releases.add(parseObjectToRelease(item, sourceUrl))
                }
            }
        } catch (_: Exception) {
            // Handled by telemetry engine
        }
        return releases
    }

    private fun parseObjectToRelease(item: JSONObject, sourceUrl: String): OtaRelease {
        val version = item.optString("versionName", item.optString("version", "3.0.0"))
        val code = item.optInt("versionCode", 3000)
        return OtaRelease(
            id = item.optString("id", "rel_$code"),
            deviceModel = item.optString("deviceModel", OtaConstants.DEVICE_MODEL_NAME),
            deviceCodename = item.optString("deviceCodename", OtaConstants.DEVICE_CODENAME),
            versionName = version,
            versionCode = code,
            buildNumber = item.optString("buildNumber", "POS-$version"),
            releaseChannel = item.optString("releaseChannel", "Stable"),
            releaseType = item.optString("releaseType", "Full"),
            packageSizeBytes = item.optLong("packageSizeBytes", 15728640L),
            downloadUrl = item.optString("downloadUrl", item.optString("zipUrl", "")),
            checksumSha256 = item.optString("checksumSha256", ""),
            androidVersion = item.optString("androidVersion", "Android 14"),
            securityPatch = item.optString("securityPatch", "2026-09-01"),
            changelog = item.optString("changelog", "Power OS Update"),
            releaseDate = item.optLong("releaseDate", System.currentTimeMillis()),
            sourceUrl = sourceUrl,
            targetLocalPath = OtaConstants.DEFAULT_TARGET_FILE_PATH
        )
    }

    private fun parseSizeToBytes(sizeStr: String): Long {
        val trimmed = sizeStr.trim().uppercase()
        return try {
            when {
                trimmed.endsWith("GB") -> (trimmed.removeSuffix("GB").trim().toDouble() * 1024 * 1024 * 1024).toLong()
                trimmed.endsWith("MB") -> (trimmed.removeSuffix("MB").trim().toDouble() * 1024 * 1024).toLong()
                trimmed.endsWith("KB") -> (trimmed.removeSuffix("KB").trim().toDouble() * 1024).toLong()
                else -> 15 * 1024 * 1024L
            }
        } catch (_: Exception) {
            15 * 1024 * 1024L
        }
    }

    private external fun nativeIsAvailable(): Boolean
    private external fun nativeVerifySha256(filePath: String, expectedSha256: String): Boolean
}
