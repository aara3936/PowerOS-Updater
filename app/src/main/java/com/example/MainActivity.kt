package com.example

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.core.model.SystemUpdateStatus
import com.example.core.worker.OtaCheckWorker
import com.example.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var pulseAnimator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Configure system bars for immersive dark midnight theme
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        setContentView(R.layout.activity_main)

        val mainContentRoot = findViewById<View>(R.id.mainContentRoot)
        val topHeaderBar = findViewById<View>(R.id.topHeaderBar)
        val btnOverflowMenu = findViewById<ImageButton>(R.id.btnOverflowMenu)
        val tvAppName = findViewById<TextView>(R.id.tvAppName)
        val tvVersion = findViewById<TextView>(R.id.tvVersion)
        val statusPill = findViewById<View>(R.id.statusPill)
        val indicatorDot = findViewById<View>(R.id.indicatorDot)
        val tvStatusText = findViewById<TextView>(R.id.tvStatusText)

        // Modern WindowInsetsCompat handling for seamless edge-to-edge support
        ViewCompat.setOnApplyWindowInsetsListener(mainContentRoot) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            topHeaderBar.setPadding(
                20.dpToPx(),
                systemBars.top + 8.dpToPx(),
                12.dpToPx(),
                8.dpToPx()
            )
            mainContentRoot.setPadding(
                systemBars.left,
                0,
                systemBars.right,
                systemBars.bottom + 16.dpToPx()
            )
            insets
        }

        // Setup 3-Dot Overflow Menu (Strict Vertical Order)
        btnOverflowMenu.setOnClickListener { view ->
            showOverflowMenu(view)
        }

        // Tap Status Pill to manually trigger check
        statusPill.setOnClickListener {
            viewModel.checkForUpdates(applicationContext)
        }

        // Detect display refresh rate for frame budgeting (60Hz / 90Hz / 120Hz)
        val refreshRate = detectDisplayRefreshRate()
        viewModel.updateDisplayMetrics(refreshRate)

        // Schedule periodic background checking via WorkManager (10-15 minute interval)
        OtaCheckWorker.schedulePeriodicCheck(applicationContext)

        // Initial background update check
        viewModel.checkForUpdates(applicationContext)

        // Lifecycle-safe StateFlow collection
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        if (tvAppName.text != state.appName) {
                            tvAppName.text = state.appName
                        }
                        val formattedVersion = state.versionName
                        if (tvVersion.text != formattedVersion && formattedVersion.isNotEmpty()) {
                            tvVersion.text = getString(R.string.version_name)
                        }

                        // Update Live Status Badge
                        updateStatusBadge(state.updateStatus, state.downloadProgress, indicatorDot, tvStatusText)
                    }
                }

                launch {
                    viewModel.userFeedback.collect { message ->
                        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewModel.updateLifecycleState("Created")
    }

    private fun showOverflowMenu(anchor: View) {
        val popup = PopupMenu(this, anchor)
        popup.menuInflater.inflate(R.menu.main_overflow_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_beta_section -> {
                    viewModel.onBetaSectionClicked()
                    showBetaDialog()
                    true
                }
                R.id.menu_notifications -> {
                    viewModel.onNotificationsClicked()
                    showNotificationsDialog()
                    true
                }
                R.id.menu_privacy_legal -> {
                    viewModel.onPrivacyLegalClicked()
                    showPrivacyDialog()
                    true
                }
                R.id.menu_settings -> {
                    viewModel.onSettingsClicked()
                    showSettingsDialog()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun showBetaDialog() {
        val channels = arrayOf("Stable Channel (v2.1.0)", "Beta Channel (v2.1.0-BETA / Preview)")
        val currentChannel = viewModel.uiState.value.currentChannel
        val checkedItem = if (currentChannel == com.example.core.model.ReleaseChannel.STABLE) 0 else 1

        com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setTitle(R.string.menu_beta_section)
            .setSingleChoiceItems(channels, checkedItem) { dialog, which ->
                val newChannel = if (which == 0) com.example.core.model.ReleaseChannel.STABLE else com.example.core.model.ReleaseChannel.BETA
                viewModel.switchReleaseChannel(newChannel, applicationContext)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setOnDismissListener {
                viewModel.dismissDialog()
            }
            .show()
    }

    private fun showNotificationsDialog() {
        val state = viewModel.uiState.value
        val announcement = state.announcement
        val message = if (announcement != null) {
            "${announcement.title}\nDate: ${announcement.date}\n\n${announcement.message}"
        } else {
            "Power OS 2.1.0-BETA Reboot Notice:\n\n• Zero-lag MVVM Coroutines Engine established.\n• 15-Minute Background Auto-Check Worker active.\n• Direct GitHub Releases binary streaming active."
        }

        com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setTitle(R.string.menu_notifications)
            .setMessage(message)
            .setPositiveButton("Dismiss") { dialog, _ ->
                dialog.dismiss()
            }
            .setOnDismissListener {
                viewModel.dismissDialog()
            }
            .show()
    }

    private fun showPrivacyDialog() {
        val policyText = "Power OS Privacy Policy & Legal Declaration\n\n" +
            "1. Zero Telemetry: No user data, analytics, or personal identifiers are collected, transmitted, or stored.\n\n" +
            "2. Cryptographic Integrity: All update packages are cryptographically signed and SHA-256 verified prior to installation.\n\n" +
            "3. Direct Distribution: Update binary streams originate directly from verified Power OS release servers.\n\n" +
            "4. Open Source: Distributed under the Apache 2.0 License."

        com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setTitle(R.string.menu_privacy_legal)
            .setMessage(policyText)
            .setPositiveButton("Acknowledge") { dialog, _ ->
                dialog.dismiss()
            }
            .setOnDismissListener {
                viewModel.dismissDialog()
            }
            .show()
    }

    private fun showSettingsDialog() {
        val state = viewModel.uiState.value
        val items = arrayOf(
            "Auto-Download over Wi-Fi only",
            "Auto-Check frequency (Every 15 min)"
        )
        val checked = booleanArrayOf(state.autoDownloadWifiOnly, true)

        com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setTitle(R.string.menu_settings)
            .setMultiChoiceItems(items, checked) { _, which, isChecked ->
                checked[which] = isChecked
            }
            .setPositiveButton("Save") { dialog, _ ->
                viewModel.updateSettings(checked[0], 15)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setOnDismissListener {
                viewModel.dismissDialog()
            }
            .show()
    }

    private fun updateStatusBadge(
        status: SystemUpdateStatus,
        downloadProgress: Int,
        indicatorDot: View,
        tvStatusText: TextView
    ) {
        when (status) {
            SystemUpdateStatus.UP_TO_DATE -> {
                stopPulsating(indicatorDot)
                setIndicatorColor(indicatorDot, Color.parseColor("#10B981"))
                tvStatusText.text = getString(R.string.status_up_to_date)
            }
            SystemUpdateStatus.CHECKING -> {
                setIndicatorColor(indicatorDot, Color.parseColor("#38BDF8"))
                tvStatusText.text = getString(R.string.status_checking)
                startPulsating(indicatorDot)
            }
            SystemUpdateStatus.UPDATE_AVAILABLE -> {
                stopPulsating(indicatorDot)
                setIndicatorColor(indicatorDot, Color.parseColor("#38BDF8"))
                tvStatusText.text = getString(R.string.status_update_available)
            }
            SystemUpdateStatus.DOWNLOADING -> {
                startPulsating(indicatorDot)
                setIndicatorColor(indicatorDot, Color.parseColor("#F59E0B"))
                tvStatusText.text = "Downloading Update ($downloadProgress%)"
            }
            SystemUpdateStatus.READY_TO_INSTALL -> {
                stopPulsating(indicatorDot)
                setIndicatorColor(indicatorDot, Color.parseColor("#10B981"))
                tvStatusText.text = getString(R.string.status_ready_to_install)
            }
            SystemUpdateStatus.ERROR -> {
                stopPulsating(indicatorDot)
                setIndicatorColor(indicatorDot, Color.parseColor("#EF4444"))
                tvStatusText.text = getString(R.string.status_up_to_date)
            }
        }
    }

    private fun setIndicatorColor(dot: View, colorInt: Int) {
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(colorInt)
            setSize(10.dpToPx(), 10.dpToPx())
        }
        dot.background = drawable
    }

    private fun startPulsating(view: View) {
        if (pulseAnimator == null) {
            pulseAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 1.0f, 0.2f).apply {
                duration = 600
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
            }
        }
        if (pulseAnimator?.isRunning != true) {
            pulseAnimator?.start()
        }
    }

    private fun stopPulsating(view: View) {
        pulseAnimator?.cancel()
        view.alpha = 1.0f
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateLifecycleState("Resumed")
    }

    override fun onPause() {
        super.onPause()
        viewModel.updateLifecycleState("Paused")
    }

    override fun onDestroy() {
        super.onDestroy()
        pulseAnimator?.cancel()
        pulseAnimator = null
    }

    private fun detectDisplayRefreshRate(): Float {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                display?.refreshRate ?: 60.0f
            } else {
                @Suppress("DEPRECATION")
                (getSystemService(WINDOW_SERVICE) as? WindowManager)?.defaultDisplay?.refreshRate ?: 60.0f
            }
        } catch (_: Exception) {
            60.0f
        }
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}
