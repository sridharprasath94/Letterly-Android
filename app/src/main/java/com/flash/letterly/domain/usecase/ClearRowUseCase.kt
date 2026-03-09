package com.flash.letterly.domain.usecase

import com.flash.letterly.domain.model.LetterState
import com.flash.letterly.domain.model.LetterTile
import javax.inject.Inject

class ClearRowUseCase @Inject constructor() {

    operator fun invoke(
        board: List<List<LetterTile>>,
        row: Int,
        wordLength: Int
    ): List<List<LetterTile>> {

        val updated = board.map { it.toMutableList() }.toMutableList()

        for (i in 0 until wordLength) {
            val tile = updated[row][i]
            updated[row][i] = tile.copy(
                letter = null,
                state = LetterState.EMPTY
            )
        }

        return updated
    }
}