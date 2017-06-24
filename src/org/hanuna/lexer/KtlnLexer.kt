package org.hanuna.lexer

import org.hanuna.parser.FilePosition


interface Sequence<Id : Any, Value: Any> {
    // all offsets about begin current token
    val lineNumber: Int
    val offsetInLine: Int
    val absolutePosition: Int

    // null if it is the end
    val currentValue: Value?
    val currentId: Id?
    fun advance()

    fun fallback(absolutePosition: Int)
}




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

