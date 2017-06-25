package org.hanuna.lexer

import org.hanuna.sequence.FilePosition
import org.hanuna.sequence.KtlnSequence

enum class KtlnTokenType {

}

class DeclarationLexer(
        private val charSequence: KtlnSequence<Char, *>
) : KtlnSequence<String, KtlnTokenType> {
    override val lineNumber: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val positionInLine: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val tokenIndex: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val currentValue: String?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val currentId: KtlnTokenType?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun advance() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fallback(prevTokenIndex: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
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

