package com.flash.letterly.presentation.shared

enum class GameMode(
    val wordLength: Int,
    val maxGuesses: Int,
    val maxHints: Int
) {
    CLASSIC(5, 6, 1),
    ADVANCED(6, 7, 2),
    EXPERT(7, 8, 3)
}