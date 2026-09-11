package com.example.data.local

import androidx.room.*
import com.example.data.model.OtaConstants
import com.example.data.model.OtaRelease
import com.example.data.model.UpdateHistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface OtaReleaseDao {
    @Query("SELECT * FROM ota_releases WHERE deviceModel = :device ORDER BY versionCode DESC")
    fun getReleasesForDevice(device: String = OtaConstants.DEVICE_MODEL_NAME): Flow<List<OtaRelease>>

    @Query("SELECT * FROM ota_releases WHERE deviceModel = :device AND releaseChannel = :channel ORDER BY versionCode DESC LIMIT 1")
    fun getLatestRelease(device: String = OtaConstants.DEVICE_MODEL_NAME, channel: String = "Stable"): Flow<OtaRelease?>

    @Query("SELECT * FROM ota_releases WHERE id = :id LIMIT 1")
    suspend fun getReleaseById(id: String): OtaRelease?

    @Query("SELECT * FROM ota_releases ORDER BY versionCode DESC")
    fun getAllReleases(): Flow<List<OtaRelease>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRelease(release: OtaRelease)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReleases(releases: List<OtaRelease>)

    @Update
    suspend fun updateRelease(release: OtaRelease)

    @Delete
    suspend fun deleteRelease(release: OtaRelease)

    @Query("DELETE FROM ota_releases WHERE id = :id")
    suspend fun deleteReleaseById(id: String)

    @Query("DELETE FROM ota_releases")
    suspend fun clearAllReleases()
}

@Dao
interface UpdateHistoryDao {
    @Query("SELECT * FROM update_history ORDER BY installedTimestamp DESC")
    fun getAllHistory(): Flow<List<UpdateHistoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(item: UpdateHistoryItem)

    @Query("DELETE FROM update_history WHERE historyId = :id")
    suspend fun deleteHistoryById(id: Long)

    @Query("DELETE FROM update_history")
    suspend fun clearHistory()
}
