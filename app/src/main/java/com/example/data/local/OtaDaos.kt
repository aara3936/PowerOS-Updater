package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.OtaRelease
import com.example.data.model.UpdateHistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface OtaReleaseDao {
    @Query("SELECT * FROM ota_releases WHERE deviceModel = :device ORDER BY versionCode DESC")
    fun getReleasesForDevice(device: String = "Cloud V1"): Flow<List<OtaRelease>>

    @Query("SELECT * FROM ota_releases WHERE deviceModel = :device AND releaseChannel = :channel AND status != 'DEPRECATED' ORDER BY versionCode DESC LIMIT 1")
    fun getLatestRelease(device: String = "Cloud V1", channel: String = "Stable"): Flow<OtaRelease?>

    @Query("SELECT * FROM ota_releases WHERE id = :id LIMIT 1")
    suspend fun getReleaseById(id: String): OtaRelease?

    @Query("SELECT * FROM ota_releases ORDER BY releaseDate DESC")
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

    @Query("DELETE FROM update_history")
    suspend fun clearHistory()
}
