package org.hanuna.parser

interface FilePosition {
    val lineNumber: Int
    val index: Int
}

interface MyCharSequence {
    val currentFilePosition: FilePosition
    val current: Char? // null if it is the end
    val currentIndex: Int


    fun moveCursor(count: Int)
    fun future(count: Int): Char?
}

