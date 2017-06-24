package org.hanuna.parser

data class FilePosition(val lineNumber: Int, val positionInLine: Int) {
    override fun toString() = "$lineNumber:$positionInLine"
}

interface MyCharSequence {
    val currentFilePosition: FilePosition
    val current: Char? // null if it is the end
    val currentIndex: Int


    fun moveCursor(count: Int)
    fun future(count: Int): Char?
}

