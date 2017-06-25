package org.hanuna

import org.hanuna.lexer.*
import org.hanuna.sequence.KtlnSequence
import org.hanuna.sequence.advance
import org.hanuna.sequence.position
import org.hanuna.test.assertEquals
import org.junit.Test
import kotlin.test.assertEquals

abstract class ReadersTest {
    private val text =
"""12
345
6


78 90
"""

    @Test
    fun simpleRead() {
        val reader = createReader(text)
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
        val reader = createReader(text)

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
        val reader = createReader("1")
        reader.advance()
        assertEquals("1:2", reader.pos)
    }

    @Test
    fun endLineNumber2() {
        val reader = createReader("1\n")
        reader.advance(2)
        assertEquals("2:1", reader.pos)
    }

    @Test
    fun readAndFallback() {
        val reader = createReader(text)

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
        val reader = createReader(text)

        reader.advance(4)
        assertEquals("2:2", reader.pos)

        reader.fallback(3)
        assertEquals("2:1", reader.pos)

        reader.fallback(2)
        assertEquals("1:3", reader.pos)
    }

    @Test
    fun fallbackPositions2() {
        val reader = createReader(text)

        reader.advance(4)
        assertEquals("2:2", reader.pos)

        reader.fallback(2)
        assertEquals("1:3", reader.pos)
    }

    @Test
    fun simpleFallback() {
        val reader = createReader(" ")
        reader.advance()
        reader.fallback(0)
        assertEquals("1:1", reader.pos)
    }

    @Test
    fun simpleFallback2() {
        val reader = createReader("\n123")
        reader.advance(3)
        reader.fallback(2)
        assertEquals("2:2", reader.pos)
    }

    @Test
    fun cacheTest() {
        val reader = createReader("0123456789")
        reader.advance(7)
        assertEquals('7', reader.currentValue)

        reader.fallback(5)
        assertEquals('5', reader.currentValue)

        reader.fallback(3)
        assertEquals('3', reader.currentValue)

        reader.advance()
        assertEquals('4', reader.currentValue)

        reader.fallback(2)
        assertEquals('2', reader.currentValue)
    }

    val KtlnSequence<Char, *>.pos get() = position.toString()

    abstract fun createReader(text: String): KtlnSequence<Char, *>

    class KtlnFileReaderTest : ReadersTest() {
        override fun createReader(text: String) = KtlnFileReader(text)
    }

    class CachedFileReaderTest3 : ReadersTest() {
        override fun createReader(text: String): KtlnSequence<Char, *> = CachedKtlnSequence(KtlnFileReader(text), 3)
    }

    class CachedFileReaderTest1 : ReadersTest() {
        override fun createReader(text: String): KtlnSequence<Char, *> = CachedKtlnSequence(KtlnFileReader(text), 1)
    }
}