package com.flash.letterly.domain.usecase

import com.flash.letterly.domain.model.LetterState
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UpdateKeyboardStateUseCaseTest {

    private lateinit var useCase: UpdateKeyboardStateUseCase

    @Before
    fun setup() {
        useCase = UpdateKeyboardStateUseCase()
    }

    @Test
    fun `updates keyboard when it is initially empty`() {

        val keyboard = emptyMap<Char, LetterState>()
        val guess = "APPLE"
        val states = listOf(
            LetterState.ABSENT,
            LetterState.PRESENT,
            LetterState.CORRECT,
            LetterState.ABSENT,
            LetterState.PRESENT
        )

        val result = useCase(keyboard, guess, states)

        assertEquals(LetterState.ABSENT, result['A'])
        assertEquals(LetterState.CORRECT, result['P']) // later position overrides earlier PRESENT
        assertEquals(LetterState.ABSENT, result['L'])
        assertEquals(LetterState.PRESENT, result['E'])
    }

    @Test
    fun `does not downgrade correct letters`() {

        val keyboard = mapOf(
            'A' to LetterState.CORRECT
        )

        val guess = "ALERT"
        val states = listOf(
            LetterState.ABSENT,
            LetterState.ABSENT,
            LetterState.ABSENT,
            LetterState.ABSENT,
            LetterState.ABSENT
        )

        val result = useCase(keyboard, guess, states)

        assertEquals(LetterState.CORRECT, result['A'])
    }

    @Test
    fun `does not downgrade present to absent`() {

        val keyboard = mapOf(
            'P' to LetterState.PRESENT
        )

        val guess = "PEACH"
        val states = listOf(
            LetterState.ABSENT,
            LetterState.ABSENT,
            LetterState.ABSENT,
            LetterState.ABSENT,
            LetterState.ABSENT
        )

        val result = useCase(keyboard, guess, states)

        assertEquals(LetterState.PRESENT, result['P'])
    }

    @Test
    fun `upgrades absent to correct`() {

        val keyboard = mapOf(
            'B' to LetterState.ABSENT
        )

        val guess = "BRICK"
        val states = listOf(
            LetterState.CORRECT,
            LetterState.ABSENT,
            LetterState.ABSENT,
            LetterState.ABSENT,
            LetterState.ABSENT
        )

        val result = useCase(keyboard, guess, states)

        assertEquals(LetterState.CORRECT, result['B'])
    }
}