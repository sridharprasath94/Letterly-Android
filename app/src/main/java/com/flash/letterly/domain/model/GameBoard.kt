package com.flash.letterly.domain.model

data class GameBoard(
    val rows: List<List<LetterTile>>
)

fun createBoard(wordLength: Int, maxGuesses: Int): List<List<LetterTile>> {
    return List(maxGuesses) {
        List(wordLength) {
            LetterTile()
        }
    }
}