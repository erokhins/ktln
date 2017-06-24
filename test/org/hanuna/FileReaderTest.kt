package org.hanuna

import org.hanuna.lexer.FileReader
import org.hanuna.lexer.advance
import org.hanuna.lexer.position
import org.hanuna.test.assertEquals
import org.junit.Test

class FileReaderTest {
    private val text =
"""12
345
6


78 90
"""

    @Test
    fun simpleRead() {
        val reader = FileReader(text)
        val result = buildString {
            while (reader.currentValue != null) {
                append(reader.currentValue!!)
                reader.advance()
            }
        }
        assertEquals(text, result)
    }

    @Test
    fun lineNumbers() {
        val reader = FileReader(text)

        reader.advance(2)
        assertEquals("1:3", reader.pos)

        reader.advance()
        assertEquals("2:1", reader.pos)

        reader.advance(3)
        assertEquals("2:4", reader.pos)

        reader.advance()
        assertEquals("3:1", reader.pos)

        reader.advance(3)
        assertEquals("5:1", reader.pos)
    }

    @Test
    fun endLineNumber() {
        val reader = FileReader("1")
        reader.advance()
        assertEquals("1:2", reader.pos)
    }

    @Test
    fun endLineNumber2() {
        val reader = FileReader("1\n")
        reader.advance(2)
        assertEquals("2:1", reader.pos)
    }

    @Test
    fun readAndFallback() {
        val reader = FileReader(text)

        while (reader.currentValue != null) reader.advance()
        reader.fallback(0)

        val result = buildString {
            while (reader.currentValue != null) {
                append(reader.currentValue!!)
                reader.advance()
            }
        }
        assertEquals(text, result)
    }

    @Test
    fun fallbackPositions() {
        val reader = FileReader(text)

        reader.advance(4)
        assertEquals("2:2", reader.pos)

        reader.fallback(3)
        assertEquals("2:1", reader.pos)

        reader.fallback(2)
        assertEquals("1:3", reader.pos)
    }

    @Test
    fun fallbackPositions2() {
        val reader = FileReader(text)

        reader.advance(4)
        assertEquals("2:2", reader.pos)

        reader.fallback(2)
        assertEquals("1:3", reader.pos)
    }

    @Test
    fun simpleFallback() {
        val reader = FileReader(" ")
        reader.advance()
        reader.fallback(0)
        assertEquals("1:1", reader.pos)
    }

    @Test
    fun simpleFallback2() {
        val reader = FileReader("\n123")
        reader.advance(3)
        reader.fallback(2)
        assertEquals("2:2", reader.pos)
    }

    val FileReader.pos get() = position.toString()

}