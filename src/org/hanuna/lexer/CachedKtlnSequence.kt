package org.hanuna.lexer

import org.hanuna.sequence.KtlnSequence
import org.hanuna.sequence.checkFallbackParameter

class CachedKtlnSequence<out Value : Any, out Id : Any>(
        private val delegate: KtlnSequence<Value, Id>,
        private val cacheSize: Int = 15
) : KtlnSequence<Value, Id> {
    private val idCache = arrayOfNulls<Any>(cacheSize)
    private val valueCache = arrayOfNulls<Any>(cacheSize)
    private val lineNumberCache = IntArray(cacheSize)
    private val positionInLineCache = IntArray(cacheSize)

    private var myCurrentIndex = 0
    private var lastDelegateFallbackIndex = 0

    init {
        if (delegate.tokenIndex != 0) throw IllegalArgumentException("Supported only non-started delegates")
        if (cacheSize <= 0) throw IllegalArgumentException("Cache size should be more than zer")

        cacheCurrent()
    }

    override fun advance() {
        assert(myCurrentIndex <= delegate.tokenIndex)

        if (myCurrentIndex == delegate.tokenIndex) {
            myCurrentIndex++
            delegate.advance()
            cacheCurrent()
        }
        else {
            myCurrentIndex++
        }
    }

    override fun fallback(prevTokenIndex: Int) {
        checkFallbackParameter(prevTokenIndex)
        if (isSafeCached(prevTokenIndex)) {
            myCurrentIndex = prevTokenIndex
        } else {
            delegate.fallback(prevTokenIndex)
            myCurrentIndex = prevTokenIndex
            cacheCurrent()
            lastDelegateFallbackIndex = prevTokenIndex
        }
    }

    private fun isSafeCached(prevTokenIndex: Int): Boolean {
        assert(prevTokenIndex <= myCurrentIndex)
        assert(myCurrentIndex <= delegate.tokenIndex)

        if (delegate.tokenIndex - prevTokenIndex >= cacheSize) return false

        if (prevTokenIndex < lastDelegateFallbackIndex) return false

        return true
    }

    private fun cacheCurrent() {
        assert(delegate.tokenIndex == myCurrentIndex)
        val arrayIndex = delegate.tokenIndex.arrayIndex

        idCache[arrayIndex] = delegate.currentId
        valueCache[arrayIndex] = delegate.currentValue
        lineNumberCache[arrayIndex] = delegate.lineNumber
        positionInLineCache[arrayIndex] = delegate.positionInLine
    }

    override val tokenIndex get() = myCurrentIndex
    override val lineNumber get() = lineNumberCache[myCurrentIndex.arrayIndex]
    override val positionInLine get() = positionInLineCache[myCurrentIndex.arrayIndex]
    override val currentValue get() = @Suppress("UNCHECKED_CAST") (valueCache[myCurrentIndex.arrayIndex] as Value?)
    override val currentId get() = @Suppress("UNCHECKED_CAST") ( idCache[myCurrentIndex.arrayIndex] as Id?)

    private val Int.arrayIndex get() = Math.floorMod(this, cacheSize)
}