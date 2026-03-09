package com.flash.letterly.domain.usecase

import com.flash.letterly.domain.model.LetterState
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EvaluateGuessUseCaseTest {

    private lateinit var evaluateGuessUseCase: EvaluateGuessUseCase

    @Before
    fun setup() {
        evaluateGuessUseCase = EvaluateGuessUseCase()
    }

    @Test
    fun `all letters correct`() {

        val result = evaluateGuessUseCase(
            guess = "apple",
            target = "apple"
        )

        val expected = listOf(
            LetterState.CORRECT,
            LetterState.CORRECT,
            LetterState.CORRECT,
            LetterState.CORRECT,
            LetterState.CORRECT
        )

        assertEquals(expected, result.states)
    }

    @Test
    fun `all letters absent`() {

        val result = evaluateGuessUseCase(
            guess = "aaaaa",
            target = "lurch"
        )

        val expected = listOf(
            LetterState.ABSENT,
            LetterState.ABSENT,
            LetterState.ABSENT,
            LetterState.ABSENT,
            LetterState.ABSENT
        )

        assertEquals(expected, result.states)
    }

    @Test
    fun `correct and present letters mixed`() {

        val result = evaluateGuessUseCase(
            guess = "apple",
            target = "angle"
        )

        val expected = listOf(
            LetterState.CORRECT,
            LetterState.ABSENT,
            LetterState.ABSENT,
            LetterState.CORRECT,
            LetterState.CORRECT
        )

        assertEquals(expected, result.states)
    }

    @Test
    fun `present letters only`() {

        val result = evaluateGuessUseCase(
            guess = "abcde",
            target = "eabcd"
        )

        val expected = listOf(
            LetterState.PRESENT,
            LetterState.PRESENT,
            LetterState.PRESENT,
            LetterState.PRESENT,
            LetterState.PRESENT
        )

        assertEquals(expected, result.states)
    }

    @Test
    fun `handles duplicate letters correctly`() {

        val result = evaluateGuessUseCase(
            guess = "alley",
            target = "apple"
        )

        val expected = listOf(
            LetterState.CORRECT,
            LetterState.PRESENT,
            LetterState.ABSENT,
            LetterState.PRESENT,
            LetterState.ABSENT
        )

        assertEquals(expected, result.states)
    }

    @Test
    fun `duplicate letters in target`() {

        val result = evaluateGuessUseCase(
            guess = "level",
            target = "lever"
        )

        val expected = listOf(
            LetterState.CORRECT,
            LetterState.CORRECT,
            LetterState.CORRECT,
            LetterState.CORRECT,
            LetterState.ABSENT
        )

        assertEquals(expected, result.states)
    }

    @Test
    fun `single present letter`() {

        val result = evaluateGuessUseCase(
            guess = "aaaaa",
            target = "bacon"
        )

        val expected = listOf(
            LetterState.ABSENT,
            LetterState.CORRECT,
            LetterState.ABSENT,
            LetterState.ABSENT,
            LetterState.ABSENT
        )

        assertEquals(expected, result.states)
    }
}