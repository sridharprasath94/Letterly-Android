package com.flash.letterly.domain.usecase

import com.flash.letterly.domain.model.GuessResult
import com.flash.letterly.domain.model.LetterState


import javax.inject.Inject


class EvaluateGuessUseCase @Inject constructor() {

    operator fun invoke(
        guess: String,
        target: String
    ): GuessResult {
        val normalizedGuess = guess.lowercase()
        val normalizedTarget = target.lowercase()

        val result = MutableList(normalizedGuess.length) { LetterState.ABSENT }
        val targetChars = normalizedTarget.toCharArray()
        val guessChars = normalizedGuess.toCharArray()

        // First pass → correct letters in correct position
        for (i in guessChars.indices) {
            if (guessChars[i] == targetChars[i]) {
                result[i] = LetterState.CORRECT
                targetChars[i] = '*' // mark as used
                guessChars[i] = '#'
            }
        }

        // Second pass → correct letter but wrong position
        for (i in guessChars.indices) {

            if (result[i] == LetterState.CORRECT) continue

            val index = targetChars.indexOf(guessChars[i])

            if (index != -1) {
                result[i] = LetterState.PRESENT
                targetChars[index] = '*'
            }
        }

        return GuessResult(
            guess = guess,
            states = result
        )
    }
}