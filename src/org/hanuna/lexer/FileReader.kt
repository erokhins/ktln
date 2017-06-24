package org.hanuna.lexer

class FileReader(val text: String): Sequence<Unit, Char> {
    override var lineNumber = 1
        private set

    override var positionInLine = 1
        private set

    override var tokenIndex = 0
        private set

    override val currentValue get() = text.elementAtOrNull(tokenIndex)

    override val currentId: Unit? get() = Unit.takeIf { tokenIndex < text.length }

    override fun advance() {
        if (tokenIndex >= text.length) throw error("End of file!")

        if (currentValue == '\n') {
            lineNumber++
            positionInLine = 1
        } else {
            positionInLine++
        }
        tokenIndex++
    }

    override fun fallback(prevTokenIndex: Int) {
        checkTokenIndex(prevTokenIndex)
        val fallbackText = text.substring(prevTokenIndex, tokenIndex)

        lineNumber -= fallbackText.count { it == '\n' }
        positionInLine = prevTokenIndex - text.substring(0, prevTokenIndex).lastIndexOf('\n')
        tokenIndex = prevTokenIndex
    }
}