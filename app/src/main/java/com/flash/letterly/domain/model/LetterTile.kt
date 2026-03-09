package com.flash.letterly.domain.model

data class LetterTile(
    val letter: Char? = null,
    val state: LetterState = LetterState.EMPTY
)