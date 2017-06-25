package org.hanuna.sequence

data class FilePosition(val lineNumber: Int, val positionInLine: Int) {
    override fun toString() = "$lineNumber:$positionInLine"
}

fun KtlnSequence<*, *>.checkFallbackParameter(prevTokenIndex: Int) {
    if (prevTokenIndex > tokenIndex || prevTokenIndex < 0)
        throw IllegalAccessException("Illegal argument: $prevTokenIndex, currentIndex: $tokenIndex")
}

fun KtlnSequence<*, *>.advance(count: Int) = repeat(count) { advance() }

val KtlnSequence<*, *>.position get() = FilePosition(lineNumber, positionInLine)

