package com.flash.letterly.domain.usecase

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CheckDuplicateGuessUseCaseTest {

    private lateinit var useCase: CheckDuplicateGuessUseCase

    @Before
    fun setup() {
        useCase = CheckDuplicateGuessUseCase()
    }

    @Test
    fun `returns true when guess already exists`() {
        val guesses = listOf("apple", "bread", "chair")

        val result = useCase("apple", guesses)

        assertTrue(result)
    }

    @Test
    fun `returns true when guess exists with different case`() {
        val guesses = listOf("apple", "bread", "chair")

        val result = useCase("APPLE", guesses)

        assertTrue(result)
    }

    @Test
    fun `returns false when guess does not exist`() {
        val guesses = listOf("apple", "bread", "chair")

        val result = useCase("table", guesses)

        assertFalse(result)
    }

    @Test
    fun `returns false when guesses list is empty`() {
        val guesses = emptyList<String>()

        val result = useCase("apple", guesses)

        assertFalse(result)
    }
}