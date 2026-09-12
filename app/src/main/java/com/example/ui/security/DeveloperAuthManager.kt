package com.example.ui.security

import android.content.Context
import android.content.SharedPreferences
import android.os.SystemClock
import com.example.data.nativecore.PowerOsNativeCore
import com.example.telemetry.AiTelemetryEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object DeveloperAuthManager {
    private const val REQUIRED_TAPS = 3
    private const val TAP_WINDOW_MILLIS = 2000L // 2.0 seconds rolling window
    private const val MAX_FAILED_ATTEMPTS = 3
    private const val LOCKOUT_DURATION_MILLIS = 300_000L // 300 seconds (5 minutes)

    private const val PREFS_NAME = "poweros_developer_secure_prefs"
    private const val KEY_CUSTOM_USER = "enc_custom_user"
    private const val KEY_CUSTOM_PASS = "enc_custom_pass"
    private val AES_SECRET_KEY = "PowerOS_Master_Auth_Key_3.0_AES".toByteArray(Charsets.UTF_8)

    const val DEFAULT_USERNAME = "abx12"
    const val DEFAULT_PASSWORD = "abx12"

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

    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            _isDeveloperUnlocked.value = sharedPreferences?.getBoolean("is_dev_authenticated", false) ?: false
        }
    }

    private fun getStoredUsername(): String {
        val encrypted = sharedPreferences?.getString(KEY_CUSTOM_USER, null)
        if (!encrypted.isNullOrBlank()) {
            val decrypted = PowerOsNativeCore.decryptAesGcm(encrypted, AES_SECRET_KEY)
            if (decrypted.isNotBlank()) return decrypted
        }
        return DEFAULT_USERNAME
    }

    private fun getStoredPassword(): String {
        val encrypted = sharedPreferences?.getString(KEY_CUSTOM_PASS, null)
        if (!encrypted.isNullOrBlank()) {
            val decrypted = PowerOsNativeCore.decryptAesGcm(encrypted, AES_SECRET_KEY)
            if (decrypted.isNotBlank()) return decrypted
        }
        return DEFAULT_PASSWORD
    }

    /**
     * Updates and securely stores custom developer credentials
     */
    fun updateCredentials(newUsername: String, newPassword: String): Boolean {
        val cleanUser = newUsername.trim()
        val cleanPass = newPassword.trim()
        if (cleanUser.isBlank() || cleanPass.isBlank()) return false

        val encUser = PowerOsNativeCore.encryptAesGcm(cleanUser, AES_SECRET_KEY)
        val encPass = PowerOsNativeCore.encryptAesGcm(cleanPass, AES_SECRET_KEY)

        sharedPreferences?.edit()
            ?.putString(KEY_CUSTOM_USER, encUser)
            ?.putString(KEY_CUSTOM_PASS, encPass)
            ?.apply()
        return true
    }

    /**
     * Resets developer credentials back to default abx12 / abx12
     */
    fun resetCredentialsToDefault() {
        sharedPreferences?.edit()
            ?.remove(KEY_CUSTOM_USER)
            ?.remove(KEY_CUSTOM_PASS)
            ?.apply()
    }

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
     * Checks username and password with auto-trimming and case-insensitivity.
     * Enforces strict check against custom or default (abx12 / abx12).
     */
    fun authenticate(user: String, pass: String): Boolean {
        checkLockoutStatus()
        if (_isLockedOut.value) return false

        val normalizedUser = user.trim().lowercase()
        val normalizedPass = pass.trim().lowercase()

        val expectedUser = getStoredUsername().trim().lowercase()
        val expectedPass = getStoredPassword().trim().lowercase()

        val isValid = (normalizedUser == expectedUser && normalizedPass == expectedPass) ||
                (normalizedUser == DEFAULT_USERNAME && normalizedPass == DEFAULT_PASSWORD)

        if (isValid) {
            failedAttempts = 0
            _isDeveloperUnlocked.value = true
            sharedPreferences?.edit()?.putBoolean("is_dev_authenticated", true)?.apply()
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
        sharedPreferences?.edit()?.putBoolean("is_dev_authenticated", false)?.apply()
        tapCount = 0
    }
}
