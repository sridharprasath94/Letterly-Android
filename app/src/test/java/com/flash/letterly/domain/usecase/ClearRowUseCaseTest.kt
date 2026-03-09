package com.flash.letterly.domain.usecase

import com.flash.letterly.domain.model.LetterState
import com.flash.letterly.domain.model.LetterTile
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ClearRowUseCaseTest {

    private lateinit var useCase: ClearRowUseCase

    @Before
    fun setup() {
        useCase = ClearRowUseCase()
    }

    @Test
    fun `clears letters and states for the specified row`() {

        val board = listOf(
            listOf(
                LetterTile(letter = 'a', state = LetterState.CORRECT),
                LetterTile(letter = 'b', state = LetterState.PRESENT),
                LetterTile(letter = 'c', state = LetterState.ABSENT)
            ),
            listOf(
                LetterTile(letter = 'd', state = LetterState.CORRECT),
                LetterTile(letter = 'e', state = LetterState.PRESENT),
                LetterTile(letter = 'f', state = LetterState.ABSENT)
            )
        )

        val result = useCase(
            board = board,
            row = 0,
            wordLength = 3
        )

        assertEquals(null, result[0][0].letter)
        assertEquals(null, result[0][1].letter)
        assertEquals(null, result[0][2].letter)

        assertEquals(LetterState.EMPTY, result[0][0].state)
        assertEquals(LetterState.EMPTY, result[0][1].state)
        assertEquals(LetterState.EMPTY, result[0][2].state)
    }

    @Test
    fun `other rows remain unchanged`() {

        val board = listOf(
            listOf(
                LetterTile(letter = 'a', state = LetterState.CORRECT),
                LetterTile(letter = 'b', state = LetterState.PRESENT)
            ),
            listOf(
                LetterTile(letter = 'c', state = LetterState.ABSENT),
                LetterTile(letter = 'd', state = LetterState.CORRECT)
            )
        )

        val result = useCase(
            board = board,
            row = 0,
            wordLength = 2
        )

        assertEquals('c', result[1][0].letter)
        assertEquals(LetterState.ABSENT, result[1][0].state)

        assertEquals('d', result[1][1].letter)
        assertEquals(LetterState.CORRECT, result[1][1].state)
    }
}