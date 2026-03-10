package com.flash.letterly.domain.model

enum class LetterState(val bgColor: Int, val textColor: Int) {
    EMPTY(0xFFFFFFFF.toInt(), 0xFF000000.toInt()),
    CORRECT(0xFF6AAA64.toInt(), 0xFFFFFFFF.toInt()),
    PRESENT(0xFFC9B458.toInt(), 0xFFFFFFFF.toInt()),
    ABSENT(0xFF787C7E.toInt(), 0xFFFFFFFF.toInt())
}