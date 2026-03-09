package com.flash.letterly.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CheckGameStatusUseCaseTest {

    private lateinit var useCase: CheckGameStatusUseCase

    @Before
    fun setup() {
        useCase = CheckGameStatusUseCase()
    }

    @Test
    fun `returns WIN when last guess equals target word`() {

        val guesses = listOf("crane", "apple")

        val result = useCase(
            guesses = guesses,
            targetWord = "apple",
            maxGuesses = 6
        )

        assertEquals(GameStatus.WIN, result)
    }

    @Test
    fun `returns LOSE when max guesses reached and target not guessed`() {

        val guesses = listOf(
            "crane",
            "table",
            "chair",
            "mouse",
            "plant",
            "stone"
        )

        val result = useCase(
            guesses = guesses,
            targetWord = "apple",
            maxGuesses = 6
        )

        assertEquals(GameStatus.LOSE, result)
    }

    @Test
    fun `returns CONTINUE when guesses remain and target not guessed`() {

        val guesses = listOf(
            "crane",
            "table"
        )

        val result = useCase(
            guesses = guesses,
            targetWord = "apple",
            maxGuesses = 6
        )

        assertEquals(GameStatus.CONTINUE, result)
    }

    @Test
    fun `returns CONTINUE when no guesses yet`() {

        val guesses = emptyList<String>()

        val result = useCase(
            guesses = guesses,
            targetWord = "apple",
            maxGuesses = 6
        )

        assertEquals(GameStatus.CONTINUE, result)
    }
}