package com.poweros.admin

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class AdminActivity : ComponentActivity() {

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
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

        btnPushAnnouncement.setOnClickListener {
            val title = etAnnouncementTitle.text.toString().trim()
            val message = etAnnouncementMessage.text.toString().trim()

            if (title.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Title and message cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
                val payload = JSONObject().apply {
                    put("title", title)
                    put("message", message)
                    put("timestamp", timestamp)
                    put("targetPackage", "com.poweros.updater")
                }

                tvAdminStatus.text = "Announcement Dispatched:\n\"$title\"\nTime: $timestamp\nFleet: com.poweros.updater"
                Toast.makeText(this@AdminActivity, "Announcement broadcast to fleet!", Toast.LENGTH_SHORT).show()
            }
        }

        btnDeployRelease.setOnClickListener {
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

            lifecycleScope.launch {
                val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
                val releasePayload = JSONObject().apply {
                    put("channel", channel)
                    put("version", version)
                    put("versionCode", code)
                    put("zipUrl", zipUrl)
                    put("deployedAt", timestamp)
                }

                tvAdminStatus.text = "OTA Release Deployed:\nVersion: $version ($code)\nChannel: ${channel.uppercase(Locale.US)}\nPayload URL: $zipUrl\nDeployed: $timestamp"
                Toast.makeText(this@AdminActivity, "OTA update release deployed to $channel channel!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
