package org.jackhammer2k.snappy.cli

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException
import java.nio.file.Paths
import kotlin.io.path.readBytes

class SnappyTest {
    private val testFileDir = Paths.get("src/test/resources")
    private val referenceData = String(testFileDir.resolve("reference").readBytes())
    private val snappy = Snappy()

    @Test
    fun compressedWithIq80FramedCompatibility() {
        checkFramed("iq80_framed.snappy")
    }

    @Test
    fun compressedWithXerialFramedCompatibility() {
        checkFramed("xerial_framed.snappy")
    }

    @Test
    fun compressedWithApacheFramedCompatibility() {
        checkFramed("apache_framed.snappy")
    }

    @Test
    fun compressedWithPythonFramedCompatibility() {
        checkFramed("python_framed.snappy")
    }

    @Test
    fun compressedWithIq80Compatibility() {
        assertThrows<IOException> { checkNonFramed("iq80_nonframed.snappy") }
    }

    @Test
    fun compressedWithXerialCompatibility() {
        checkNonFramed("xerial_nonframed.snappy")
    }

    @Test
    fun compressedWithApacheCompatibility() {
        checkNonFramed("apache_nonframed.snappy")
    }

    private fun checkFramed(testFile: String) {
        val input = snappy.input(true, false, testFileDir.resolve(testFile).toFile())
        assertEquals(referenceData, String(input.readBytes()).trimEnd())
    }

    private fun checkNonFramed(testFile: String) {
        val input = snappy.input(true, true, testFileDir.resolve(testFile).toFile())
        assertEquals(referenceData, String(input.readBytes()))
    }
}