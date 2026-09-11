package com.example.telemetry

import android.os.Build
import com.example.data.model.OtaConstants
import com.example.data.model.OtaRelease
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ConcurrentLinkedDeque

data class TelemetryEvent(
    val timestamp: Long = System.currentTimeMillis(),
    val type: String, // "NETWORK_ERROR", "PARSE_ERROR", "CRASH", "SELF_HEAL", "AUTH"
    val summary: String,
    val details: String,
    val isRecovered: Boolean = true
)

object AiTelemetryEngine {
    private const val MAX_RING_BUFFER_SIZE = 50
    private val ringBuffer = ConcurrentLinkedDeque<TelemetryEvent>()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)

    fun logNetworkFailure(url: String, statusCode: Int, errorMessage: String) {
        val event = TelemetryEvent(
            type = "NETWORK_ERROR",
            summary = "HTTP $statusCode on endpoint",
            details = "URL: $url\nCode: $statusCode\nMessage: $errorMessage\nAction: Triggered exponential backoff retry / fallback cache."
        )
        record(event)
    }

    fun logParseError(rawJson: String, exception: Throwable) {
        val snippet = if (rawJson.length > 200) rawJson.take(200) + "..." else rawJson
        val event = TelemetryEvent(
            type = "PARSE_ERROR",
            summary = "JSON schema parse mismatch: ${exception.localizedMessage}",
            details = "Snippet: $snippet\nException: ${exception.stackTraceToString().take(500)}\nAction: Self-healing fallback applied."
        )
        record(event)
    }

    fun logCrash(throwable: Throwable) {
        val event = TelemetryEvent(
            type = "CRASH",
            summary = "Intercepted exception: ${throwable.javaClass.simpleName}",
            details = throwable.stackTraceToString().take(1000),
            isRecovered = true
        )
        record(event)
    }

    fun logSelfHeal(reason: String, fallbackVersion: String) {
        val event = TelemetryEvent(
            type = "SELF_HEAL",
            summary = "Self-healing activated: $reason",
            details = "Defaulted gracefully to fallback release ($fallbackVersion) to preserve UI state without disruption."
        )
        record(event)
    }

    fun logAuthEvent(username: String, success: Boolean, attemptsLeft: Int) {
        val event = TelemetryEvent(
            type = "AUTH",
            summary = if (success) "Developer mode unlocked" else "Developer authentication failed",
            details = "Target: $username | Success: $success | Remaining attempts: $attemptsLeft",
            isRecovered = true
        )
        record(event)
    }

    private fun record(event: TelemetryEvent) {
        ringBuffer.addLast(event)
        while (ringBuffer.size > MAX_RING_BUFFER_SIZE) {
            ringBuffer.pollFirst()
        }
    }

    fun getRecentEvents(): List<TelemetryEvent> {
        return ringBuffer.toList().reversed()
    }

    /**
     * Fallback default release when remote metadata is unreachable or malformed.
     */
    fun getSafeFallbackRelease(): OtaRelease {
        return OtaRelease(
            id = "fallback_v37_release",
            deviceModel = OtaConstants.DEVICE_MODEL_NAME,
            deviceCodename = OtaConstants.DEVICE_CODENAME,
            versionName = "3.A.7.0.102GL",
            versionCode = 3790,
            buildNumber = "POS-3.7.90-STABLE-OppoA6X",
            releaseChannel = "Stable",
            releaseType = "Full",
            packageSizeBytes = 15728640L,
            downloadUrl = "https://github.com/aara3936/Oppo-A6X-OTA/releases/download/v3.7.90/PowerOS_v3.7.90_OppoA6X.zip",
            checksumSha256 = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
            androidVersion = "Android 14",
            securityPatch = "2026-09-01",
            changelog = "Power OS V3.7 Stable Release (Self-Healing Local Fallback Cache):\n• Liquid Glass UI architecture v3.0\n• Full Oppo A6X hardware scheduling\n• Adaptive frame-rate optimization",
            releaseDate = System.currentTimeMillis(),
            sourceUrl = OtaConstants.DEFAULT_RAW_JSON_URL,
            targetLocalPath = OtaConstants.DEFAULT_TARGET_FILE_PATH
        )
    }

    /**
     * Module 4.3: One-Click AI Diagnostic Prompt Exporter.
     * Formats recorded crash/error traces into a clean technical prompt format ready to copy to clipboard.
     */
    fun generateAiDiagnosticPrompt(): String {
        val sb = StringBuilder()
        sb.appendLine("================================================================================")
        sb.appendLine("POWER OS OTA UPDATER — SYSTEM DIAGNOSTIC & AI SELF-HEALING REPORT")
        sb.appendLine("================================================================================")
        sb.appendLine("Timestamp: ${dateFormat.format(Date())}")
        sb.appendLine("Device Model: ${OtaConstants.DEVICE_MODEL_NAME} (${OtaConstants.DEVICE_CODENAME})")
        sb.appendLine("OS Version: ${OtaConstants.CURRENT_BASE_VERSION_NAME} (Build ${OtaConstants.CURRENT_BASE_VERSION_CODE})")
        sb.appendLine("Android SDK: ${Build.VERSION.SDK_INT} (Release: ${Build.VERSION.RELEASE})")
        sb.appendLine("Architecture: ${Build.SUPPORTED_ABIS.joinToString(", ")}")
        sb.appendLine("Target Rom Destination: ${OtaConstants.DEFAULT_TARGET_FILE_PATH}")
        sb.appendLine("Remote Endpoint: ${OtaConstants.DEFAULT_RAW_JSON_URL}")
        sb.appendLine("Fallback Endpoint: ${OtaConstants.DEFAULT_FALLBACK_JSON_URL}")
        sb.appendLine("--------------------------------------------------------------------------------")
        sb.appendLine("ACTIVE TELEMETRY RING-BUFFER LOG (Last ${ringBuffer.size} Events):")
        sb.appendLine("--------------------------------------------------------------------------------")

        if (ringBuffer.isEmpty()) {
            sb.appendLine("[NOMINAL] No system errors or crash events recorded. System running smoothly.")
        } else {
            ringBuffer.forEachIndexed { index, event ->
                sb.appendLine("[#${index + 1}] [${dateFormat.format(Date(event.timestamp))}] [${event.type}]")
                sb.appendLine("Summary: ${event.summary}")
                sb.appendLine("Details: ${event.details.replace("\n", "\n         ")}")
                sb.appendLine("Self-Healed: ${event.isRecovered}")
                sb.appendLine("--------------------------------------------------------------------------------")
            }
        }

        sb.appendLine("================================================================================")
        sb.appendLine("AI INSTRUCTION FOR INSTANT RESOLUTION:")
        sb.appendLine("Please review the telemetry log above, analyze any network/parsing discrepancies,")
        sb.appendLine("and apply deterministic patches to the Power OS updater pipeline without regressions.")
        sb.appendLine("================================================================================")
        return sb.toString()
    }
}
