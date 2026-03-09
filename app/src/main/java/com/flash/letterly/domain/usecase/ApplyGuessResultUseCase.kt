package com.flash.letterly.domain.usecase

import com.flash.letterly.domain.model.LetterState
import com.flash.letterly.domain.model.LetterTile
import javax.inject.Inject

class ApplyGuessResultUseCase @Inject constructor() {

    operator fun invoke(
        board: List<List<LetterTile>>,
        row: Int,
        states: List<LetterState>
    ): List<List<LetterTile>> {

        val updated = board.map { it.toMutableList() }.toMutableList()

        states.forEachIndexed { index, letterState ->
            val tile = updated[row][index]
            updated[row][index] = tile.copy(
                letter = tile.letter?.lowercaseChar(),
                state = letterState
            )
        }

        return updated
    }
}