package com.example.ui.security

import android.content.Context
import android.os.SystemClock
import com.example.telemetry.AiTelemetryEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object DeveloperAuthManager {
    private const val REQUIRED_TAPS = 3
    private const val TAP_WINDOW_MILLIS = 2000L // 2.0 seconds rolling window
    private const val MAX_FAILED_ATTEMPTS = 3
    private const val LOCKOUT_DURATION_MILLIS = 300_000L // 300 seconds (5 minutes)

    private const val EXPECTED_USERNAME = "abx12"
    private const val EXPECTED_PASSWORD = "abx12"

    private var tapCount = 0
    private var lastTapTime = 0L

    private var failedAttempts = 0
    private var lockoutExpiryTime = 0L

    private val _isDeveloperUnlocked = MutableStateFlow(false)
    val isDeveloperUnlocked: StateFlow<Boolean> = _isDeveloperUnlocked.asStateFlow()

    private val _isLockedOut = MutableStateFlow(false)
    val isLockedOut: StateFlow<Boolean> = _isLockedOut.asStateFlow()

    private val _lockoutRemainingSeconds = MutableStateFlow(0)
    val lockoutRemainingSeconds: StateFlow<Int> = _lockoutRemainingSeconds.asStateFlow()

    /**
     * Registers a tap on the App Version label.
     * Returns true if 3 taps occurred within 2.0s rolling window (and not locked out).
     */
    fun registerVersionTap(): Boolean {
        checkLockoutStatus()
        if (_isLockedOut.value) {
            return false
        }

        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastTapTime > TAP_WINDOW_MILLIS) {
            // Silently reset tap counter if window expired
            tapCount = 1
        } else {
            tapCount++
        }
        lastTapTime = currentTime

        if (tapCount >= REQUIRED_TAPS) {
            tapCount = 0
            return true
        }
        return false
    }

    /**
     * Checks username and password.
     * Enforces strict lowercase check: abx12 / abx12
     */
    fun authenticate(user: String, pass: String): Boolean {
        checkLockoutStatus()
        if (_isLockedOut.value) return false

        val normalizedUser = user.trim().lowercase()
        val normalizedPass = pass.trim().lowercase()

        if (normalizedUser == EXPECTED_USERNAME && normalizedPass == EXPECTED_PASSWORD) {
            failedAttempts = 0
            _isDeveloperUnlocked.value = true
            AiTelemetryEngine.logAuthEvent(normalizedUser, success = true, attemptsLeft = MAX_FAILED_ATTEMPTS)
            return true
        } else {
            failedAttempts++
            val remaining = (MAX_FAILED_ATTEMPTS - failedAttempts).coerceAtLeast(0)
            AiTelemetryEngine.logAuthEvent(normalizedUser, success = false, attemptsLeft = remaining)

            if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                // Lock out for 300 seconds
                lockoutExpiryTime = SystemClock.elapsedRealtime() + LOCKOUT_DURATION_MILLIS
                _isLockedOut.value = true
                _lockoutRemainingSeconds.value = (LOCKOUT_DURATION_MILLIS / 1000).toInt()
            }
            return false
        }
    }

    fun checkLockoutStatus() {
        val now = SystemClock.elapsedRealtime()
        if (now < lockoutExpiryTime) {
            _isLockedOut.value = true
            val remaining = ((lockoutExpiryTime - now) / 1000).toInt().coerceAtLeast(1)
            _lockoutRemainingSeconds.value = remaining
        } else {
            _isLockedOut.value = false
            _lockoutRemainingSeconds.value = 0
            if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                failedAttempts = 0
            }
        }
    }

    fun getFailedAttempts(): Int = failedAttempts

    /**
     * Session Auto-Purge Security:
     * Immediately purges developer state whenever app is minimized, sent to background, or manually locked.
     */
    fun purgeSession() {
        _isDeveloperUnlocked.value = false
        tapCount = 0
    }
}
