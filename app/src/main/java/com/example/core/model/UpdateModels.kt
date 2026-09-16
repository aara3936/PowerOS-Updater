package com.example.core.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UpdateRelease(
    @SerializedName(value = "version", alternate = ["version_name", "versionName"])
    val version: String = "4.09STABLEE1R3S09(EXO001) PK",

    @SerializedName(value = "versionCode", alternate = ["version_code", "version_Code"])
    val versionCode: Int = 9999,

    @SerializedName(value = "releaseDate", alternate = ["release_date", "date"])
    val releaseDate: String = "",

    @SerializedName("size")
    val size: String = "",

    @SerializedName(value = "zipUrl", alternate = ["zip_url", "url", "download_url"])
    val zipUrl: String = "",

    @SerializedName("changelog")
    val changelog: String = "",

    @SerializedName("channel")
    val channel: String = "stable",

    @SerializedName("sha256")
    val sha256: String = "",

    @SerializedName("freeze")
    val freeze: Boolean = false
)

@Keep
data class SystemAnnouncement(
    @SerializedName("title")
    val title: String = "",

    @SerializedName(value = "date", alternate = ["release_date"])
    val date: String = "",

    @SerializedName("message")
    val message: String = ""
)

@Keep
data class ManifestResponse(
    @SerializedName("announcement")
    val announcement: SystemAnnouncement? = null,

    @SerializedName("channels")
    val channels: Map<String, UpdateRelease>? = null,

    @SerializedName("stable")
    val stable: UpdateRelease? = null,

    @SerializedName("beta")
    val beta: UpdateRelease? = null,

    @SerializedName(value = "version", alternate = ["version_name", "versionName"])
    val version: String? = null,

    @SerializedName(value = "versionCode", alternate = ["version_code", "version_Code"])
    val versionCode: Int? = null,

    @SerializedName(value = "releaseDate", alternate = ["release_date", "date"])
    val releaseDate: String? = null,

    @SerializedName("size")
    val size: String? = null,

    @SerializedName(value = "zipUrl", alternate = ["zip_url", "url", "download_url"])
    val zipUrl: String? = null,

    @SerializedName("changelog")
    val changelog: String? = null,

    @SerializedName("sha256")
    val sha256: String? = null,

    @SerializedName("freeze")
    val freeze: Boolean? = null
)

@Keep
enum class SystemUpdateStatus {
    CHECKING,
    UP_TO_DATE,
    UPDATE_AVAILABLE,
    DOWNLOADING,
    READY_TO_INSTALL,
    ERROR
}

@Keep
enum class ReleaseChannel(val displayName: String, val tag: String) {
    STABLE("Stable Channel", "stable"),
    BETA("Beta Channel", "beta")
}


