package com.flash.letterly.domain.usecase

import com.flash.letterly.domain.model.LetterState
import com.flash.letterly.domain.model.LetterTile
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ApplyGuessResultUseCaseTest {

    private lateinit var useCase: ApplyGuessResultUseCase

    @Before
    fun setup() {
        useCase = ApplyGuessResultUseCase()
    }

    @Test
    fun `applies states to correct row`() {

        val board = listOf(
            listOf(
                LetterTile(letter = 'A', state = LetterState.EMPTY),
                LetterTile(letter = 'B', state = LetterState.EMPTY),
                LetterTile(letter = 'C', state = LetterState.EMPTY)
            ),
            listOf(
                LetterTile(letter = 'D', state = LetterState.EMPTY),
                LetterTile(letter = 'E', state = LetterState.EMPTY),
                LetterTile(letter = 'F', state = LetterState.EMPTY)
            )
        )

        val states = listOf(
            LetterState.CORRECT,
            LetterState.PRESENT,
            LetterState.ABSENT
        )

        val result = useCase(
            board = board,
            row = 0,
            states = states
        )

        assertEquals(LetterState.CORRECT, result[0][0].state)
        assertEquals(LetterState.PRESENT, result[0][1].state)
        assertEquals(LetterState.ABSENT, result[0][2].state)
    }

    @Test
    fun `letters are converted to lowercase`() {

        val board = listOf(
            listOf(
                LetterTile(letter = 'A', state = LetterState.EMPTY),
                LetterTile(letter = 'B', state = LetterState.EMPTY)
            )
        )

        val states = listOf(
            LetterState.CORRECT,
            LetterState.ABSENT
        )

        val result = useCase(
            board = board,
            row = 0,
            states = states
        )

        assertEquals('a', result[0][0].letter)
        assertEquals('b', result[0][1].letter)
    }

    @Test
    fun `other rows remain unchanged`() {

        val board = listOf(
            listOf(
                LetterTile(letter = 'A', state = LetterState.EMPTY),
                LetterTile(letter = 'B', state = LetterState.EMPTY)
            ),
            listOf(
                LetterTile(letter = 'C', state = LetterState.EMPTY),
                LetterTile(letter = 'D', state = LetterState.EMPTY)
            )
        )

        val states = listOf(
            LetterState.CORRECT,
            LetterState.ABSENT
        )

        val result = useCase(
            board = board,
            row = 0,
            states = states
        )

        assertEquals(LetterState.EMPTY, result[1][0].state)
        assertEquals(LetterState.EMPTY, result[1][1].state)
    }
}