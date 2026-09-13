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
}
