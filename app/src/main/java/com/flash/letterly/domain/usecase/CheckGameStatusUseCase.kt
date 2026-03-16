package com.flash.letterly.domain.usecase

import javax.inject.Inject

/**
 * Represents the current status of the game.
 */
enum class GameStatus {
    WIN,
    LOSE,
    CONTINUE
}

/**
 * Determines whether the game has been won, lost,
 * or should continue.
 */
class CheckGameStatusUseCase @Inject constructor() {

    operator fun invoke(
        guesses: List<String>,
        targetWord: String,
        maxGuesses: Int
    ): GameStatus {
        // Player wins if the last guess matches the target
        if (guesses.lastOrNull().equals(targetWord, ignoreCase = true)) {
            return GameStatus.WIN
        }

        // Player loses if guesses are exhausted
        if (guesses.size >= maxGuesses) {
            return GameStatus.LOSE
        }
        // Otherwise the game continues
        return GameStatus.CONTINUE
    }
}