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
    @Query("SELECT * FROM ota_releases WHERE (deviceModel = :device OR deviceModel = 'Oppo A6X' OR deviceModel = '') ORDER BY versionCode DESC")
    fun getReleasesForDevice(device: String = "Oppo A6X"): Flow<List<OtaRelease>>

    @Query("SELECT * FROM ota_releases WHERE (deviceModel = :device OR deviceModel = 'Oppo A6X' OR deviceModel = '') AND (releaseChannel = :channel OR (:channel = 'Stable' AND (releaseChannel = 'Official' OR releaseChannel = 'Stable' OR releaseChannel = 'stable' OR releaseChannel = 'official')) OR (:channel = 'Early Access' AND (releaseChannel = 'Early Access' OR releaseChannel = 'Beta' OR releaseChannel = 'beta' OR releaseChannel = 'early_access')) OR (:channel = 'Closed Beta' AND (releaseChannel = 'Closed Beta' OR releaseChannel = 'Alpha' OR releaseChannel = 'alpha' OR releaseChannel = 'nightly' OR releaseChannel = 'closed_beta'))) AND status != 'DEPRECATED' ORDER BY versionCode DESC LIMIT 1")
    fun getLatestRelease(device: String = "Oppo A6X", channel: String = "Stable"): Flow<OtaRelease?>

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
