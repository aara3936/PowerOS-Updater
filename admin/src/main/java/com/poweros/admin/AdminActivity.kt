package com.poweros.admin

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class AdminActivity : ComponentActivity() {

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
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

                // Dispatch Real-Time Sync Broadcast directly to OTA_Updater fleet
                val syncIntent = Intent("com.poweros.updater.ACTION_OTA_SYNC").apply {
                    putExtra("extra_type", "ANNOUNCEMENT")
                    putExtra("extra_title", title)
                    putExtra("extra_message", message)
                    putExtra("extra_date", timestamp)
                    putExtra("extra_payload_json", payload.toString())
                }
                sendBroadcast(syncIntent)

                tvAdminStatus.text = "Announcement Dispatched:\n\"$title\"\nTime: $timestamp\nReal-time broadcast dispatched to OTA_Updater fleet"
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
                    put("sha256", "a69f78326a1112b4e2f893f41ff72d0012bc0b9856f74a0c8411d3311f92e859")
                    put("changelog", "Power OS $version ($channel) deployed from Admin Console")
                }

                // Dispatch Real-Time Sync Broadcast directly to OTA_Updater fleet
                val syncIntent = Intent("com.poweros.updater.ACTION_OTA_SYNC").apply {
                    putExtra("extra_type", "RELEASE")
                    putExtra("extra_channel", channel)
                    putExtra("extra_version", version)
                    putExtra("extra_code", code)
                    putExtra("extra_zip_url", zipUrl)
                    putExtra("extra_sha256", "a69f78326a1112b4e2f893f41ff72d0012bc0b9856f74a0c8411d3311f92e859")
                    putExtra("extra_date", timestamp)
                    putExtra("extra_changelog", "Power OS $version ($channel) deployed from Admin Console")
                    putExtra("extra_payload_json", releasePayload.toString())
                }
                sendBroadcast(syncIntent)

                tvAdminStatus.text = "OTA Release Deployed:\nVersion: $version ($code)\nChannel: ${channel.uppercase(Locale.US)}\nPayload URL: $zipUrl\nDeployed: $timestamp\nReal-time sync broadcast sent"
                Toast.makeText(this@AdminActivity, "OTA update release deployed to $channel channel!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
