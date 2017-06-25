package org.hanuna.sequence

interface KtlnSequence<out Value : Any, out Id: Any> {
    // all offsets about begin current token
    val lineNumber: Int
    val positionInLine: Int
    val tokenIndex: Int

    // null if it is the end
    val currentValue: Value?
    val currentId: Id?
    fun advance()

    fun fallback(prevTokenIndex: Int)
}
