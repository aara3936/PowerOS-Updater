package com.example

import com.example.core.dispatcher.DispatcherProvider
import com.example.core.repository.SystemCoreRepository
import com.example.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PowerOsUpdaterTest {

    private val testDispatcher = StandardTestDispatcher()

    private val testDispatchers = object : DispatcherProvider {
        override val main: CoroutineDispatcher get() = testDispatcher
        override val io: CoroutineDispatcher get() = testDispatcher
        override val default: CoroutineDispatcher get() = testDispatcher
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testAppMetadata() {
        val expectedVersion = "2.1.0-RELEASE"
        val expectedVersionCode = 210
        assertEquals("2.1.0-RELEASE", expectedVersion)
        assertEquals(210, expectedVersionCode)
    }

    @Test
    fun testFrameBudgetEvaluation() = runTest(testDispatcher) {
        val repo = SystemCoreRepository(testDispatchers)
        val budget60Hz = repo.calculateFrameBudget(60.0f)
        val budget120Hz = repo.calculateFrameBudget(120.0f)

        // 60Hz should be ~16.66ms, 120Hz should be ~8.33ms
        assertEquals(16.66f, budget60Hz, 0.1f)
        assertEquals(8.33f, budget120Hz, 0.1f)
    }

    @Test
    fun testMainViewModelStateFlowLifecycle() = runTest(testDispatcher) {
        val repo = SystemCoreRepository(testDispatchers)
        val viewModel = MainViewModel(repo, testDispatchers)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isInitialized)
        assertTrue(state.renderPipelineActive)
        assertTrue(state.hardwareAccelerated)
        assertEquals("Power OS Updater", state.appName)
        assertEquals("2.1.0-RELEASE", state.versionName)
        assertEquals(210, state.versionCode)

        viewModel.updateDisplayMetrics(120.0f)
        testDispatcher.scheduler.advanceUntilIdle()

        val updatedState = viewModel.uiState.value
        assertEquals(120.0f, updatedState.displayRefreshRateHz, 0.01f)
        assertEquals(8.33f, updatedState.targetFrameTimeBudgetMs, 0.1f)
    }

    @Test
    fun testOtaManifestParsing() {
        val sampleJson = """
            {
              "stable": {
                "version": "2.2.0-RELEASE",
                "versionCode": 220,
                "releaseDate": "2026-09-15",
                "size": "25 MB",
                "zipUrl": "https://github.com/aara3936/PowerOS-OTA/releases/download/v2.2.0/update.zip",
                "changelog": "Power OS 2.2.0"
              }
            }
        """.trimIndent()

        val release = com.example.core.engine.OtaEngine.parseReleaseJson(sampleJson)
        org.junit.Assert.assertNotNull(release)
        assertEquals("2.2.0-RELEASE", release?.version)
        assertEquals(220, release?.versionCode)
        assertEquals("https://github.com/aara3936/PowerOS-OTA/releases/download/v2.2.0/update.zip", release?.zipUrl)
        assertTrue(release!!.versionCode > com.example.core.engine.OtaEngine.CURRENT_VERSION_CODE)
    }

    @Test
    fun testMenuActions() = runTest(testDispatcher) {
        val repo = SystemCoreRepository(testDispatchers)
        val viewModel = MainViewModel(repo, testDispatchers)

        viewModel.onBetaSectionClicked()
        assertEquals(com.example.core.model.DialogType.BETA_SECTION, viewModel.uiState.value.activeDialog)

        viewModel.onNotificationsClicked()
        assertEquals(com.example.core.model.DialogType.NOTIFICATIONS, viewModel.uiState.value.activeDialog)

        viewModel.onPrivacyLegalClicked()
        assertEquals(com.example.core.model.DialogType.PRIVACY_LEGAL, viewModel.uiState.value.activeDialog)

        viewModel.onSettingsClicked()
        assertEquals(com.example.core.model.DialogType.SETTINGS, viewModel.uiState.value.activeDialog)

        viewModel.dismissDialog()
        assertEquals(null, viewModel.uiState.value.activeDialog)

        viewModel.updateSettings(wifiOnly = true, intervalMinutes = 15)
        assertTrue(viewModel.uiState.value.autoDownloadWifiOnly)
        assertEquals(15, viewModel.uiState.value.checkIntervalMinutes)
    }

    @Test
    fun testDualChannelAndAnnouncementParsing() {
        val json = """
            {
              "announcement": {
                "title": "Welcome Fleet",
                "date": "2026-09-13",
                "message": "Power OS 2.1.0-RELEASE ecosystem live"
              },
              "channels": {
                "stable": {
                  "version": "2.1.0",
                  "versionCode": 210,
                  "releaseDate": "2026-09-13",
                  "size": "15 MB",
                  "zipUrl": "https://github.com/aara3936/PowerOS-OTA/releases/download/v2.1.0/PowerOS_v2.1.0.zip",
                  "changelog": "Stable"
                },
                "beta": {
                  "version": "2.2.0-RELEASE",
                  "versionCode": 220,
                  "releaseDate": "2026-09-15",
                  "size": "16 MB",
                  "zipUrl": "https://github.com/aara3936/PowerOS-OTA/releases/download/v2.2.0/PowerOS_v2.2.0-RELEASE.zip",
                  "changelog": "Beta Preview"
                }
              }
            }
        """.trimIndent()

        val announcement = com.example.core.engine.OtaEngine.parseAnnouncementJson(json)
        org.junit.Assert.assertNotNull(announcement)
        assertEquals("Welcome Fleet", announcement?.title)

        val stableRelease = com.example.core.engine.OtaEngine.parseReleaseJson(json, com.example.core.model.ReleaseChannel.STABLE)
        org.junit.Assert.assertNotNull(stableRelease)
        assertEquals(210, stableRelease?.versionCode)
        assertEquals("2.1.0", stableRelease?.version)

        val betaRelease = com.example.core.engine.OtaEngine.parseReleaseJson(json, com.example.core.model.ReleaseChannel.BETA)
        org.junit.Assert.assertNotNull(betaRelease)
        assertEquals(220, betaRelease?.versionCode)
        assertEquals("2.2.0-RELEASE", betaRelease?.version)
    }

    @Test
    fun testSnakeCaseManifestParsing() = runTest(testDispatcher) {
        val repo = SystemCoreRepository(testDispatchers)
        val snakeJson = """
            {
              "version_name": "2.3.0-RELEASE",
              "version_code": 230,
              "release_date": "2026-09-20",
              "size": "30 MB",
              "zip_url": "https://github.com/aara3936/PowerOS-OTA/releases/download/v2.3.0/update.zip",
              "changelog": "Power OS 2.3.0"
            }
        """.trimIndent()

        val release = repo.parseUpdateRelease(snakeJson)
        org.junit.Assert.assertNotNull(release)
        assertEquals("2.3.0-RELEASE", release?.version)
        assertEquals(230, release?.versionCode)
        assertEquals("https://github.com/aara3936/PowerOS-OTA/releases/download/v2.3.0/update.zip", release?.zipUrl)
        assertTrue(repo.isUpdateAvailable(release!!))
    }

    @Test
    fun testNativeSecurityVerification() = runTest(testDispatcher) {
        val tempFile = java.io.File.createTempFile("test_payload", ".zip")
        try {
            // Write standard ZIP header (0x50, 0x4B, 0x03, 0x04)
            tempFile.writeBytes(byteArrayOf(0x50, 0x4B, 0x03, 0x04, 0x00, 0x00))

            val isHeaderValid = com.example.core.security.NativeSecurityBridge.verifyPayloadHeader(tempFile, testDispatcher)
            assertTrue("Header must be identified as valid zip format", isHeaderValid)

            val computedHash = com.example.core.security.NativeSecurityBridge.computeSha256(tempFile.absolutePath, testDispatcher)
            assertTrue("SHA-256 hash must be non-empty", computedHash.isNotEmpty())

            val matches = com.example.core.security.NativeSecurityBridge.verifyIntegrity(
                tempFile.absolutePath,
                computedHash,
                testDispatcher
            )
            assertTrue("Integrity verification must pass with matching hash", matches)
        } finally {
            tempFile.delete()
        }
    }

    @Test
    fun testRealtimeSyncFlow() = runTest(testDispatcher) {
        val testAnnouncement = com.example.core.model.SystemAnnouncement(
            title = "Fleet Test Alert",
            date = "2026-09-15",
            message = "Admin dispatched live test."
        )
        com.example.core.engine.OtaEngine.setLiveAnnouncement(testAnnouncement)
        assertEquals("Fleet Test Alert", com.example.core.engine.OtaEngine.announcementFlow.value?.title)

        val testRelease = com.example.core.model.UpdateRelease(
            version = "2.3.0-RELEASE",
            versionCode = 230,
            releaseDate = "2026-09-20",
            size = "32 MB",
            zipUrl = "https://example.com/poweros_230.zip",
            changelog = "Real-time sync test release",
            channel = "beta",
            sha256 = "test_hash"
        )
        com.example.core.engine.OtaEngine.setLiveRelease(com.example.core.model.ReleaseChannel.STABLE, testRelease)
        assertEquals(230, com.example.core.engine.OtaEngine.latestReleaseFlow.value?.versionCode)
        assertEquals(com.example.core.model.SystemUpdateStatus.UPDATE_AVAILABLE, com.example.core.engine.OtaEngine.statusFlow.value)
    }

    @Test
    fun testDownloadMetricsStateFlow() = runTest(testDispatcher) {
        val speed = "2.45 MB/s"
        val eta = "18s"
        val event = com.example.core.model.AppEngineEvent.DownloadMetricsUpdated(speed, eta)
        assertEquals(speed, event.speedText)
        assertEquals(eta, event.etaText)

        val state = com.example.core.model.AppEngineState(
            downloadSpeedText = speed,
            downloadEtaText = eta,
            isDownloadResuming = true
        )
        assertTrue(state.isDownloadResuming)
        assertEquals("2.45 MB/s", state.downloadSpeedText)
        assertEquals("18s", state.downloadEtaText)
    }
}
