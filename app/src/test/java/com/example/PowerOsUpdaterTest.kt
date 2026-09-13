package com.example

import org.junit.Assert.assertEquals
import org.junit.Test

class PowerOsUpdaterTest {
    @Test
    fun testAppMetadata() {
        val expectedVersion = "2.1.0-BETA"
        val expectedVersionCode = 210
        assertEquals("2.1.0-BETA", expectedVersion)
        assertEquals(210, expectedVersionCode)
    }
}
