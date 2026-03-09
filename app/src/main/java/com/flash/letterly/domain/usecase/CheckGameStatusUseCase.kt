package com.flash.letterly.domain.usecase

import android.util.Log
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
        Log.d("GameViewModel", "Guesses: $guesses, Target: $targetWord")
        // Player wins if the last guess matches the target
        if (guesses.lastOrNull().equals(targetWord, ignoreCase = true)) {
            return GameStatus.WIN
        }

        Log.d("GameViewModel", "Guesses made: ${guesses.size}, Max guesses: $maxGuesses")
        // Player loses if guesses are exhausted
        if (guesses.size >= maxGuesses) {
            return GameStatus.LOSE
        }
        Log.d("GameViewModel", "Game continues: ${guesses.size} guesses made, ${maxGuesses - guesses.size} remaining")
        // Otherwise the game continues
        return GameStatus.CONTINUE
    }
}