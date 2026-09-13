package com.example

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

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
        val tvAppName = findViewById<TextView>(R.id.tvAppName)
        val tvVersion = findViewById<TextView>(R.id.tvVersion)

        // Modern WindowInsetsCompat handling for seamless edge-to-edge support
        ViewCompat.setOnApplyWindowInsetsListener(mainContentRoot) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left + 24.dpToPx(),
                systemBars.top + 24.dpToPx(),
                systemBars.right + 24.dpToPx(),
                systemBars.bottom + 24.dpToPx()
            )
            insets
        }

        // Detect display refresh rate for frame budgeting (60Hz / 90Hz / 120Hz)
        val refreshRate = detectDisplayRefreshRate()
        viewModel.updateDisplayMetrics(refreshRate)

        // Lifecycle-safe StateFlow collection (prevents leaks, handles rotation seamlessly)
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
                    }
                }
            }
        }

        viewModel.updateLifecycleState("Created")
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateLifecycleState("Resumed")
    }

    override fun onPause() {
        super.onPause()
        viewModel.updateLifecycleState("Paused")
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
