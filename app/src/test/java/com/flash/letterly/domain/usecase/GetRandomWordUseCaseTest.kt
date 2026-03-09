package com.flash.letterly.domain.usecase

import com.flash.letterly.domain.model.Word
import com.flash.letterly.domain.repository.WordRepository
import com.flash.letterly.presentation.shared.GameMode
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetRandomWordUseCaseTest {

    private lateinit var repository: WordRepository
    private lateinit var useCase: GetRandomWordUseCase

    private class FakeWordRepository : WordRepository {

        var wordToReturn: Word? = null

        override suspend fun getAllWords(): List<Word> {
            return wordToReturn?.let { listOf(it) } ?: emptyList()
        }

        override suspend fun getRandomWord(length: Int): Word? {
            return wordToReturn
        }

        override suspend fun getWord(value: String): Word? {
            return wordToReturn?.takeIf { it.value == value }
        }

        override suspend fun updateWord(word: Word) {
            // no-op for unit test
        }

        override suspend fun exists(word: String): Boolean {
            return wordToReturn?.value == word
        }
    }

    @Before
    fun setup() {
        repository = FakeWordRepository()
        useCase = GetRandomWordUseCase(repository)
    }

    @Test
    fun `returns word when never answered`() = runTest {
        val word = Word(
            value = "apple",
            length = 5,
            lastAnsweredAt = null
        )
        (repository as FakeWordRepository).wordToReturn = word
        val result = useCase(GameMode.CLASSIC)
        assertEquals(word, result)
    }

    @Test
    fun `returns word when last answered more than two months ago`() = runTest {
        val oldTimestamp = System.currentTimeMillis() - (61L * 24 * 60 * 60 * 1000)
        val word = Word(
            value = "apple",
            length = 5,
            lastAnsweredAt = oldTimestamp
        )
        (repository as FakeWordRepository).wordToReturn = word
        val result = useCase(GameMode.CLASSIC)
        assertEquals(word, result)
    }

    @Test
    fun `returns null when repository returns null`() = runTest {
        (repository as FakeWordRepository).wordToReturn = null
        val result = useCase(GameMode.CLASSIC)
        assertNull(result)
    }

    @Test
    fun `returns null when all fetched words are recently answered`() = runTest {
        val recentTimestamp = System.currentTimeMillis()
        val word = Word(
            value = "apple",
            length = 5,
            lastAnsweredAt = recentTimestamp
        )
        (repository as FakeWordRepository).wordToReturn = word
        val result = useCase(GameMode.CLASSIC)
        assertNull(result)
    }
}