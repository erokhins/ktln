package org.hanuna.test

import com.intellij.rt.execution.junit.FileComparisonFailure
import junit.framework.ComparisonFailure
import org.junit.Assert
import org.junit.Test
import java.io.File
import java.io.IOException
import kotlin.test.assertEquals

abstract class AbstractFileTest(
        val inputFolder: String,
        val outputFolder: String,
        val outputPostfix: String
) {
    @Test
    fun doTest() {
        val outputRoot = File(outputFolder)

        val rootFile = File(inputFolder)
        for (file in rootFile.walk()) {
            val relativePath = file.toRelativeString(rootFile)
            val outputFile = outputRoot.resolve(relativePath)
            if (file.isDirectory) {
                outputFile.mkdir()
            }
            else {
                val fileToWrite = outputFile.resolveSibling("${outputFile.nameWithoutExtension}.$outputPostfix")
                val output = transform(file.readText(), relativePath)
                assertEqualsToFile(fileToWrite, output)
            }
        }
    }


    abstract fun transform(inputFileContent: String, relativePath: String): String
}

class SimpleTest : AbstractFileTest("src", "o/simple/", "_kt") {
    override fun transform(inputFileContent: String, relativePath: String): String {
        return relativePath + "\n" + inputFileContent
    }
}

fun assertEqualsToFile(expectedFile: File, actual: String) {
    try {
        if (!expectedFile.exists()) {
            expectedFile.writeText(actual)
            Assert.fail("Expected data file did not exist. Generating: " + expectedFile)
        }

        val expected = expectedFile.readText()
        if (expected != actual) {
            throw FileComparisonFailure("Actual data differs from file content: " + expectedFile.name, expected, actual,
                    expectedFile.absolutePath)
        }
    } catch (e: IOException) {
        throw e
    }

}

fun assertEquals(expected: String, actual: String, message: String = "") {
    if (expected != actual) {
        throw ComparisonFailure(message, expected, actual)
    }
}