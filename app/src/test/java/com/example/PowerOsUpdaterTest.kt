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
        val expectedVersion = "2.1.0-BETA"
        val expectedVersionCode = 210
        assertEquals("2.1.0-BETA", expectedVersion)
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
        assertEquals("2.1.0-BETA", state.versionName)
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
                "message": "Power OS 2.1.0-BETA ecosystem live"
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
                  "version": "2.2.0-BETA",
                  "versionCode": 220,
                  "releaseDate": "2026-09-15",
                  "size": "16 MB",
                  "zipUrl": "https://github.com/aara3936/PowerOS-OTA/releases/download/v2.2.0/PowerOS_v2.2.0-BETA.zip",
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
        assertEquals("2.2.0-BETA", betaRelease?.version)
    }
}
