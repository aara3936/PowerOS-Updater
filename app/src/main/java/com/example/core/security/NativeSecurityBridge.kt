package com.example.core.security

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

object NativeSecurityBridge {
    private const val TAG = "NativeSecurityBridge"
    private var isNativeLoaded = false

    private fun logInfo(msg: String) {
        try {
            Log.i(TAG, msg)
        } catch (_: Throwable) {
            println("[$TAG] INFO: $msg")
        }
    }

    private fun logWarn(msg: String) {
        try {
            Log.w(TAG, msg)
        } catch (_: Throwable) {
            println("[$TAG] WARN: $msg")
        }
    }

    private fun logError(msg: String, tr: Throwable? = null) {
        try {
            Log.e(TAG, msg, tr)
        } catch (_: Throwable) {
            println("[$TAG] ERROR: $msg ${tr?.message.orEmpty()}")
        }
    }

    init {
        try {
            System.loadLibrary("native-security")
            isNativeLoaded = true
            logInfo("Native C++ security library loaded successfully")
        } catch (t: Throwable) {
            isNativeLoaded = false
            logWarn("Native security library fallback active: ${t.message}")
        }
    }

    @JvmStatic
    external fun nativeComputeSha256(filePath: String): String

    @JvmStatic
    external fun nativeVerifyIntegrity(filePath: String, expectedHash: String): Boolean

    @JvmStatic
    external fun nativeVerifyPayloadHeader(bytes: ByteArray): Boolean

    suspend fun computeSha256(filePath: String, dispatcher: CoroutineDispatcher = Dispatchers.IO): String =
        withContext(dispatcher) {
            if (isNativeLoaded) {
                try {
                    val result = nativeComputeSha256(filePath)
                    if (result.isNotEmpty()) return@withContext result
                } catch (e: Throwable) {
                    logError("Native SHA-256 execution failed, switching to fallback", e)
                }
            }
            computeSha256Fallback(filePath)
        }

    suspend fun verifyIntegrity(
        filePath: String,
        expectedHash: String,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Boolean = withContext(dispatcher) {
        if (expectedHash.isBlank()) return@withContext true
        if (isNativeLoaded) {
            try {
                return@withContext nativeVerifyIntegrity(filePath, expectedHash)
            } catch (e: Throwable) {
                logError("Native integrity check failed, switching to fallback", e)
            }
        }
        val computed = computeSha256Fallback(filePath)
        computed.equals(expectedHash.trim(), ignoreCase = true)
    }

    suspend fun verifyPayloadHeader(
        file: File,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Boolean = withContext(dispatcher) {
        if (!file.exists() || file.length() < 4) return@withContext false
        val header = ByteArray(4)
        try {
            FileInputStream(file).use { it.read(header) }
            if (isNativeLoaded) {
                try {
                    return@withContext nativeVerifyPayloadHeader(header)
                } catch (e: Throwable) {
                    logError("Native header check failed, switching to fallback", e)
                }
            }
            header[0] == 0x50.toByte() && header[1] == 0x4B.toByte() &&
                ((header[2] == 0x03.toByte() && header[3] == 0x04.toByte()) ||
                 (header[2] == 0x05.toByte() && header[3] == 0x06.toByte()))
        } catch (e: Exception) {
            logError("Payload header check failed", e)
            false
        }
    }

    private fun computeSha256Fallback(filePath: String): String {
        val file = File(filePath)
        if (!file.exists()) return ""
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            FileInputStream(file).use { fis ->
                val buffer = ByteArray(65536)
                var read: Int
                while (fis.read(buffer).also { read = it } != -1) {
                    digest.update(buffer, 0, read)
                }
            }
            digest.digest().joinToString("") { "%02x".format(it) }
        } catch (_: Throwable) {
            ""
        }
    }
}
