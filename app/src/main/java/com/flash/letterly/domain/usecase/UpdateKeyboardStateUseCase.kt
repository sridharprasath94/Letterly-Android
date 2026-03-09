package com.flash.letterly.domain.usecase

import com.flash.letterly.domain.model.LetterState
import javax.inject.Inject

class UpdateKeyboardStateUseCase @Inject constructor() {

    operator fun invoke(
        keyboard: Map<Char, LetterState>,
        guess: String,
        states: List<LetterState>
    ): Map<Char, LetterState> {

        val updatedKeyboard = keyboard.toMutableMap()

        states.forEachIndexed { index, letterState ->
            val letter = guess[index].uppercaseChar()
            val existing = updatedKeyboard[letter]

            val newState = when (existing) {
                LetterState.CORRECT -> LetterState.CORRECT
                LetterState.PRESENT ->
                    if (letterState == LetterState.ABSENT) LetterState.PRESENT else letterState
                else -> letterState
            }

            updatedKeyboard[letter] = newState
        }

        return updatedKeyboard
    }
}