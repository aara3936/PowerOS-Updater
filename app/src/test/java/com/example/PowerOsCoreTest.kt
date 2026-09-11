package com.example

import com.example.data.nativecore.PowerOsNativeCore
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class PowerOsCoreTest {

    @Test
    fun testSha256ChecksumVerification() {
        val testFile = File.createTempFile("ota_test", ".bin")
        testFile.writeText("PowerOS-OTA-Payload-Verification-String")

        // Compute expected SHA-256
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        val expectedHash = digest.digest(testFile.readBytes()).joinToString("") { "%02x".format(it) }

        val isValid = PowerOsNativeCore.verifyChecksum(testFile.absolutePath, expectedHash)
        assertTrue("Checksum verification must return true for valid payload", isValid)

        val isInvalid = PowerOsNativeCore.verifyChecksum(testFile.absolutePath, "invalid_hash_123")
        assertFalse("Checksum verification must return false for mismatching hash", isInvalid)

        testFile.delete()
    }

    @Test
    fun testPayloadJsonValidation() {
        val validJson = """
            {
                "version_name": "2.0.0-BETA",
                "version_code": 200,
                "download_url": "https://example.com/ota.zip",
                "checksum_sha256": "abcdef1234567890"
            }
        """.trimIndent()

        val isValid = PowerOsNativeCore.validatePayload(validJson)
        assertTrue("Valid OTA JSON payload must pass validation", isValid)

        val invalidJson = """
            {
                "unknown_field": "val"
            }
        """.trimIndent()

        val isInvalid = PowerOsNativeCore.validatePayload(invalidJson)
        assertFalse("Incomplete OTA JSON payload must fail validation", isInvalid)
    }

    @Test
    fun testStateEncryptionAndDecryption() {
        val secretState = "DeveloperCredentials:abx12:masterToken_99"
        val encrypted = PowerOsNativeCore.encryptState(secretState)
        assertNotNull(encrypted)
        assertNotEquals(secretState, encrypted)

        val decrypted = PowerOsNativeCore.decryptState(encrypted)
        assertEquals(secretState, decrypted)
    }
}
