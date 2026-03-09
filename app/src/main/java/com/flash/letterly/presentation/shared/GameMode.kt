package com.flash.letterly.presentation.shared

enum class GameMode(
    val wordLength: Int,
    val maxGuesses: Int
) {
    CLASSIC(5,6),
    ADVANCED(6,7),
    EXPERT(7,8)
}