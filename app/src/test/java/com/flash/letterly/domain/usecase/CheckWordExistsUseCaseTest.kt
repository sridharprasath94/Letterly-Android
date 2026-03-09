package com.flash.letterly.domain.usecase

import com.flash.letterly.domain.model.Word
import com.flash.letterly.domain.repository.WordRepository
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CheckWordExistsUseCaseTest {
    private lateinit var repository: FakeWordRepository
    private lateinit var useCase: CheckWordExistsUseCase

    @Before
    fun setup() {
        repository = FakeWordRepository()
        useCase = CheckWordExistsUseCase(repository)
    }

    @Test
    fun `returns true when word exists in repository`() = runTest {
        val wordValue = "apple"
        repository.wordExists = true

        val result = useCase(wordValue)

        assertTrue(result)
    }

    @Test
    fun `returns false when word does not exist in repository`() = runTest {
        val wordValue = "apple"
        repository.wordExists = false

        val result = useCase(wordValue)

        assertFalse(result)
    }

    private class FakeWordRepository : WordRepository {

        var wordExists: Boolean = false

        override suspend fun getAllWords(): List<Word> {
            return emptyList()
        }

        override suspend fun getRandomWord(length: Int): Word? {
            return null
        }

        override suspend fun getWord(value: String): Word? {
            return null
        }

        override suspend fun updateWord(word: Word) {}

        override suspend fun exists(word: String): Boolean {
            return wordExists
        }
    }
}