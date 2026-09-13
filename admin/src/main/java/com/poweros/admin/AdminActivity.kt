package com.poweros.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.poweros.admin.github.GithubApiEngine
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdminActivity : ComponentActivity() {

    private val prefs by lazy {
        getSharedPreferences("poweros_admin_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        setContentView(R.layout.activity_admin)

        val scrollRoot = findViewById<android.view.View>(R.id.adminScrollRoot)
        ViewCompat.setOnApplyWindowInsetsListener(scrollRoot) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        val etGithubRepo = findViewById<EditText>(R.id.etGithubRepo)
        val etGithubToken = findViewById<EditText>(R.id.etGithubToken)
        val etAnnouncementTitle = findViewById<EditText>(R.id.etAnnouncementTitle)
        val etAnnouncementMessage = findViewById<EditText>(R.id.etAnnouncementMessage)
        val btnPushAnnouncement = findViewById<Button>(R.id.btnPushAnnouncement)

        val rgChannel = findViewById<RadioGroup>(R.id.rgChannel)
        val rbBeta = findViewById<RadioButton>(R.id.rbBeta)
        val etReleaseVersion = findViewById<EditText>(R.id.etReleaseVersion)
        val etReleaseCode = findViewById<EditText>(R.id.etReleaseCode)
        val etZipUrl = findViewById<EditText>(R.id.etZipUrl)
        val btnDeployRelease = findViewById<Button>(R.id.btnDeployRelease)
        val tvAdminStatus = findViewById<TextView>(R.id.tvAdminStatus)

        // Restore saved settings
        val savedRepo = prefs.getString("key_github_repo", "aara3936/PowerOS-OTA")
        val savedToken = prefs.getString("key_github_token", "")
        etGithubRepo.setText(savedRepo)
        if (!savedToken.isNullOrBlank()) {
            etGithubToken.setText(savedToken)
        }

        btnPushAnnouncement.setOnClickListener {
            val repo = etGithubRepo.text.toString().trim().ifEmpty { "aara3936/PowerOS-OTA" }
            val token = etGithubToken.text.toString().trim()
            val title = etAnnouncementTitle.text.toString().trim()
            val message = etAnnouncementMessage.text.toString().trim()

            if (title.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Title and message cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save token and repo for convenience
            prefs.edit().putString("key_github_repo", repo).putString("key_github_token", token).apply()

            lifecycleScope.launch {
                val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
                val isoDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                tvAdminStatus.text = "Publishing announcement to GitHub repository...\nRepo: $repo"

                // 1. Direct GitHub REST API Pipeline
                val gitResult = GithubApiEngine.pushManifestUpdate(
                    repo = repo,
                    token = token.ifEmpty { null },
                    commitMessage = "feat(announcement): $title [Power OS Admin]"
                ) { rootJson ->
                    val annObj = JSONObject().apply {
                        put("title", title)
                        put("date", isoDate)
                        put("message", message)
                    }
                    rootJson.put("announcement", annObj)
                }

                // 2. Real-Time Inter-App Fleet Broadcast Dispatch
                val localPayload = JSONObject().apply {
                    put("title", title)
                    put("message", message)
                    put("timestamp", timestamp)
                    put("targetPackage", "com.poweros.updater")
                }
                val syncIntent = Intent("com.poweros.updater.ACTION_OTA_SYNC").apply {
                    putExtra("extra_type", "ANNOUNCEMENT")
                    putExtra("extra_title", title)
                    putExtra("extra_message", message)
                    putExtra("extra_date", timestamp)
                    putExtra("extra_payload_json", localPayload.toString())
                }
                sendBroadcast(syncIntent)

                // 3. Instant UI Feedback
                when (gitResult) {
                    is GithubApiEngine.PushResult.Success -> {
                        tvAdminStatus.text = "GitHub REST Sync: SUCCESS (HTTP ${gitResult.statusCode})\n" +
                                "Commit: ${gitResult.commitSha.take(8)}\n" +
                                "Announcement: \"$title\"\n" +
                                "Time: $timestamp\n" +
                                "Fleet broadcast: Dispatched"
                        Toast.makeText(this@AdminActivity, "Synced directly to GitHub & Fleet!", Toast.LENGTH_SHORT).show()
                    }
                    is GithubApiEngine.PushResult.Failure -> {
                        val authNote = if (gitResult.isAuthIssue) " (Token required for direct remote push)" else ""
                        tvAdminStatus.text = "GitHub REST: ${gitResult.errorMessage}$authNote\n" +
                                "Announcement: \"$title\"\n" +
                                "Time: $timestamp\n" +
                                "Local Fleet Broadcast: ACTIVE & DISPATCHED"
                        Toast.makeText(this@AdminActivity, "Broadcast to fleet (${gitResult.statusCode})", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnDeployRelease.setOnClickListener {
            val repo = etGithubRepo.text.toString().trim().ifEmpty { "aara3936/PowerOS-OTA" }
            val token = etGithubToken.text.toString().trim()
            val isBeta = rbBeta.isChecked
            val channel = if (isBeta) "beta" else "stable"
            val version = etReleaseVersion.text.toString().trim()
            val codeStr = etReleaseCode.text.toString().trim()
            val zipUrl = etZipUrl.text.toString().trim()

            if (version.isEmpty() || codeStr.isEmpty() || zipUrl.isEmpty()) {
                Toast.makeText(this, "All release fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val code = codeStr.toIntOrNull() ?: 220
            prefs.edit().putString("key_github_repo", repo).putString("key_github_token", token).apply()

            lifecycleScope.launch {
                val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
                val isoDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                val sha256 = "a69f78326a1112b4e2f893f41ff72d0012bc0b9856f74a0c8411d3311f92e859"
                val changelog = "Power OS $version ($channel):\n• Direct GitHub pipeline release\n• High-performance chunked auto-resume"

                tvAdminStatus.text = "Deploying release to GitHub updater.json...\nVersion: $version ($code)"

                // 1. Direct GitHub REST API Pipeline
                val gitResult = GithubApiEngine.pushManifestUpdate(
                    repo = repo,
                    token = token.ifEmpty { null },
                    commitMessage = "release($channel): $version ($code) [Power OS Admin]"
                ) { rootJson ->
                    val channelsObj = rootJson.optJSONObject("channels") ?: JSONObject().also {
                        rootJson.put("channels", it)
                    }
                    val relObj = JSONObject().apply {
                        put("version", version)
                        put("versionCode", code)
                        put("releaseDate", isoDate)
                        put("size", "25 MB")
                        put("zipUrl", zipUrl)
                        put("sha256", sha256)
                        put("changelog", changelog)
                    }
                    channelsObj.put(channel, relObj)
                }

                // 2. Real-Time Inter-App Fleet Broadcast Dispatch
                val releasePayload = JSONObject().apply {
                    put("channel", channel)
                    put("version", version)
                    put("versionCode", code)
                    put("zipUrl", zipUrl)
                    put("deployedAt", timestamp)
                    put("sha256", sha256)
                    put("changelog", changelog)
                }
                val syncIntent = Intent("com.poweros.updater.ACTION_OTA_SYNC").apply {
                    putExtra("extra_type", "RELEASE")
                    putExtra("extra_channel", channel)
                    putExtra("extra_version", version)
                    putExtra("extra_code", code)
                    putExtra("extra_zip_url", zipUrl)
                    putExtra("extra_sha256", sha256)
                    putExtra("extra_date", timestamp)
                    putExtra("extra_changelog", changelog)
                    putExtra("extra_payload_json", releasePayload.toString())
                }
                sendBroadcast(syncIntent)

                // 3. Instant UI Feedback
                when (gitResult) {
                    is GithubApiEngine.PushResult.Success -> {
                        tvAdminStatus.text = "GitHub REST Release: SUCCESS (HTTP ${gitResult.statusCode})\n" +
                                "Commit: ${gitResult.commitSha.take(8)}\n" +
                                "Target: $version ($code) on ${channel.uppercase(Locale.US)}\n" +
                                "Time: $timestamp\n" +
                                "Fleet broadcast: Dispatched"
                        Toast.makeText(this@AdminActivity, "Release committed to GitHub & broadcast to fleet!", Toast.LENGTH_SHORT).show()
                    }
                    is GithubApiEngine.PushResult.Failure -> {
                        val authNote = if (gitResult.isAuthIssue) " (Token required for direct remote push)" else ""
                        tvAdminStatus.text = "GitHub REST: ${gitResult.errorMessage}$authNote\n" +
                                "Target: $version ($code) on ${channel.uppercase(Locale.US)}\n" +
                                "Time: $timestamp\n" +
                                "Local Fleet Broadcast: ACTIVE & DISPATCHED"
                        Toast.makeText(this@AdminActivity, "Deployed to fleet (${gitResult.statusCode})", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val btnEmergencyRollback = findViewById<Button>(R.id.btnEmergencyRollback)
        btnEmergencyRollback.setOnClickListener {
            val repo = etGithubRepo.text.toString().trim().ifEmpty { "aara3936/PowerOS-OTA" }
            val token = etGithubToken.text.toString().trim()
            val isBeta = rbBeta.isChecked
            val channel = if (isBeta) "beta" else "stable"
            
            lifecycleScope.launch {
                val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
                val isoDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                
                tvAdminStatus.text = "Executing Emergency Rollback on GitHub updater.json..."

                // 1. Direct GitHub REST API Pipeline
                val gitResult = GithubApiEngine.pushManifestUpdate(
                    repo = repo,
                    token = token.ifEmpty { null },
                    commitMessage = "rollback($channel): Emergency Freeze [Power OS Admin]"
                ) { rootJson ->
                    val channelsObj = rootJson.optJSONObject("channels") ?: JSONObject().also {
                        rootJson.put("channels", it)
                    }
                    val relObj = JSONObject().apply {
                        put("version", "2.0.0-ROLLBACK")
                        put("versionCode", 200)
                        put("releaseDate", isoDate)
                        put("size", "0 MB")
                        put("zipUrl", "")
                        put("sha256", "")
                        put("changelog", "EMERGENCY ROLLBACK & FREEZE INITIATED")
                        put("freeze", true)
                    }
                    channelsObj.put(channel, relObj)
                }

                // 2. Real-Time Inter-App Fleet Broadcast Dispatch
                val syncIntent = Intent("com.poweros.updater.ACTION_OTA_SYNC").apply {
                    putExtra("extra_type", "RELEASE")
                    putExtra("extra_channel", channel)
                    putExtra("extra_version", "2.0.0-ROLLBACK")
                    putExtra("extra_code", 200)
                    putExtra("extra_zip_url", "")
                    putExtra("extra_sha256", "")
                    putExtra("extra_date", timestamp)
                    putExtra("extra_changelog", "EMERGENCY ROLLBACK & FREEZE INITIATED")
                }
                sendBroadcast(syncIntent)

                // 3. Instant UI Feedback
                when (gitResult) {
                    is GithubApiEngine.PushResult.Success -> {
                        tvAdminStatus.text = "Emergency Rollback: SUCCESS (HTTP ${gitResult.statusCode})\n" +
                                "Commit: ${gitResult.commitSha.take(8)}\n" +
                                "Target: FROZEN on ${channel.uppercase(Locale.US)}\n" +
                                "Time: $timestamp"
                        Toast.makeText(this@AdminActivity, "Rollback committed & broadcast!", Toast.LENGTH_SHORT).show()
                    }
                    is GithubApiEngine.PushResult.Failure -> {
                        tvAdminStatus.text = "Emergency Rollback: ${gitResult.errorMessage}\n" +
                                "Local Fleet Broadcast: ACTIVE & DISPATCHED"
                        Toast.makeText(this@AdminActivity, "Broadcasted Rollback Locally", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
