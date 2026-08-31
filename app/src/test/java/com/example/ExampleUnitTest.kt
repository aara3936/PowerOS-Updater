package com.example

import com.example.data.model.OtaConstants
import com.example.data.network.NetworkUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun parse_lineageos_style_ota_json() {
    val json = """
      {
        "response": [
          {
            "datetime": 1725000000,
            "filename": "PowerOS-OppoA6X-v2.1.zip",
            "id": "oppo_a6x_210",
            "romtype": "official",
            "size": 1887436800,
            "url": "https://github.com/aara3936/Oppo-A6X-OTA/releases/download/v2.1.0/rom.zip",
            "version": "2.1.0",
            "version_name": "Power OS 2.1.0 (Oppo A6X)",
            "version_code": 210,
            "build_number": "POS-2.1.0-OPPO-A6X-20260831",
            "device": "Oppo A6X",
            "changelog": "Official OTA build for Oppo A6X",
            "sha256": "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
          }
        ]
      }
    """.trimIndent()

    val releases = NetworkUtils.parseOtaJson(json)
    assertEquals(1, releases.size)
    val rel = releases[0]
    assertEquals(OtaConstants.DEVICE_MODEL_NAME, rel.deviceModel)
    assertEquals(210, rel.versionCode)
    assertEquals("https://github.com/aara3936/Oppo-A6X-OTA/releases/download/v2.1.0/rom.zip", rel.downloadUrl)
    assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", rel.checksumSha256)
  }

  @Test
  fun parse_direct_object_ota_json() {
    val json = """
      {
        "version_name": "PowerOS 2.2.0 (Oppo A6X)",
        "version_code": 220,
        "download_url": "https://github.com/aara3936/Oppo-A6X-OTA/releases/download/v2.2.0/rom.zip",
        "size": 1900000000,
        "device": "Oppo A6X"
      }
    """.trimIndent()

    val releases = NetworkUtils.parseOtaJson(json)
    assertEquals(1, releases.size)
    assertEquals(220, releases[0].versionCode)
  }
}
