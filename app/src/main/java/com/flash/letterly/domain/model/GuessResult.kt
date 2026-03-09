package com.flash.letterly.domain.model

/**
 * Represents the result of a guess
 */
data class GuessResult(
    val guess: String,
    val states: List<LetterState>
)
