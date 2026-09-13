package com.poweros.admin.github

import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * Native GitHub REST API Pipeline for Power OS Admin Console.
 * Directly publishes releases, tags, and announcements to the remote repository.
 * Zero-leak asynchronous operations on Dispatchers.IO.
 */
object GithubApiEngine {

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    private const val GITHUB_API_BASE = "https://api.github.com/repos"

    data class ManifestContent(
        val sha: String,
        val jsonObject: JSONObject,
        val rawJson: String
    )

    sealed interface PushResult {
        data class Success(
            val statusCode: Int,
            val commitSha: String,
            val message: String
        ) : PushResult

        data class Failure(
            val statusCode: Int,
            val errorMessage: String,
            val isAuthIssue: Boolean
        ) : PushResult
    }

    suspend fun getRemoteManifest(repo: String, token: String?): ManifestContent? = withContext(Dispatchers.IO) {
        try {
            val url = "$GITHUB_API_BASE/$repo/contents/updater.json?ref=main"
            val reqBuilder = Request.Builder()
                .url(url)
                .header("Accept", "application/vnd.github.v3+json")

            if (!token.isNullOrBlank()) {
                reqBuilder.header("Authorization", "Bearer $token")
            }

            httpClient.newCall(reqBuilder.build()).execute().use { response ->
                if (!response.isSuccessful) return@withContext null
                val bodyStr = response.body?.string().orEmpty()
                val json = JSONObject(bodyStr)
                val sha = json.optString("sha", "")
                val base64Content = json.optString("content", "").replace("\n", "").replace("\r", "")
                val decodedString = if (base64Content.isNotEmpty()) {
                    String(Base64.decode(base64Content, Base64.DEFAULT), Charsets.UTF_8)
                } else {
                    "{}"
                }
                ManifestContent(
                    sha = sha,
                    jsonObject = JSONObject(decodedString),
                    rawJson = decodedString
                )
            }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun pushManifestUpdate(
        repo: String,
        token: String?,
        commitMessage: String,
        transform: (JSONObject) -> Unit
    ): PushResult = withContext(Dispatchers.IO) {
        if (token.isNullOrBlank()) {
            return@withContext PushResult.Failure(
                statusCode = 401,
                errorMessage = "GitHub Personal Access Token (PAT) not configured. Fallback to local fleet broadcast.",
                isAuthIssue = true
            )
        }

        try {
            val current = getRemoteManifest(repo, token)
            val currentSha = current?.sha.orEmpty()
            val targetJson = current?.jsonObject ?: JSONObject().apply {
                put("announcement", JSONObject())
                put("channels", JSONObject())
            }

            transform(targetJson)
            val updatedJsonString = targetJson.toString(2)
            val base64Encoded = Base64.encodeToString(updatedJsonString.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)

            val payload = JSONObject().apply {
                put("message", commitMessage)
                put("content", base64Encoded)
                if (currentSha.isNotEmpty()) {
                    put("sha", currentSha)
                }
                put("branch", "main")
            }

            val requestBody = payload.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url("$GITHUB_API_BASE/$repo/contents/updater.json")
                .header("Accept", "application/vnd.github.v3+json")
                .header("Authorization", "Bearer $token")
                .put(requestBody)
                .build()

            httpClient.newCall(request).execute().use { response ->
                val code = response.code
                val respBody = response.body?.string().orEmpty()

                if (response.isSuccessful) {
                    val respJson = try { JSONObject(respBody) } catch (_: Exception) { JSONObject() }
                    val commitSha = respJson.optJSONObject("commit")?.optString("sha", "latest") ?: "ok"
                    PushResult.Success(
                        statusCode = code,
                        commitSha = commitSha,
                        message = "Remote updater.json successfully committed to main"
                    )
                } else {
                    val errorMsg = try {
                        JSONObject(respBody).optString("message", "HTTP $code")
                    } catch (_: Exception) {
                        "HTTP $code: ${response.message}"
                    }
                    PushResult.Failure(
                        statusCode = code,
                        errorMessage = errorMsg,
                        isAuthIssue = (code == 401 || code == 403)
                    )
                }
            }
        } catch (e: Exception) {
            PushResult.Failure(
                statusCode = 0,
                errorMessage = e.message ?: "Network error communicating with GitHub API",
                isAuthIssue = false
            )
        }
    }
}
