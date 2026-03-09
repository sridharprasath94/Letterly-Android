package com.flash.letterly.domain.usecase

import javax.inject.Inject

class CheckDuplicateGuessUseCase @Inject constructor() {

    operator fun invoke(
        guess: String,
        guesses: List<String>
    ): Boolean {
        val normalizedGuess = guess.lowercase()
        return guesses.any { it.lowercase() == normalizedGuess }
    }
}