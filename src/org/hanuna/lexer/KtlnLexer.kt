package org.hanuna.lexer

import org.hanuna.parser.FilePosition


interface Sequence<out Id : Any, out Value: Any> {
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

fun Sequence<*, *>.checkTokenIndex(prevTokenIndex: Int) {
    if (prevTokenIndex > tokenIndex || prevTokenIndex < 0)
        throw IllegalAccessException("Illegal argument: $prevTokenIndex, currentIndex: $tokenIndex")
}

fun Sequence<*, *>.advance(count: Int) = repeat(count) { advance() }

val Sequence<*, *>.position get() = FilePosition(lineNumber, positionInLine)


interface KtlnLexer {
    val current: KtlnToken
    val filePosition: FilePosition

    // 0 -> current
    fun future(shift: Int): KtlnToken?
    fun move(shift: Int)
}

sealed class KtlnToken



sealed class Keyword(val value: String) : KtlnToken() {
    object Package : Keyword("package")
    object Class : Keyword("class")
    object Interface : Keyword("interface")
    object Val : Keyword("val")
    object Fun : Keyword("fun")
    object Try : Keyword("try")
}

