package org.hanuna.lexer

import org.hanuna.lexer.CharType.*
import org.hanuna.sequence.KtlnSequence
import org.hanuna.sequence.SequenceIdDecorator
import java.lang.Character.*

enum class CharType {
    NEW_LINE,
    DIGIT, // 0-9
    LETTER, // a-z, A-Z
    UNDERSCORE,
    OTHER
}

class CharTypeDecorator(
        delegate: KtlnSequence<Char, *>
): SequenceIdDecorator<Char, Any, CharType>(delegate) {
    override fun transformId(oldId: Any, oldValue: Char): CharType {
        val exactSymbol = when (oldValue) {
            '\n' -> NEW_LINE
            '_' -> UNDERSCORE
            else -> null
        }

        if (exactSymbol != null) return exactSymbol

        return when {
            isDigit(oldValue) -> DIGIT
            isLetter(oldValue) -> LETTER
            else -> OTHER
        }
    }
}