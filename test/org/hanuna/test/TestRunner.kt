package org.hanuna.test

import org.junit.Test
import java.io.File

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
                fileToWrite.writeText(output)
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