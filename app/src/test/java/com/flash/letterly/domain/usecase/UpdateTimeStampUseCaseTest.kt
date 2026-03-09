package com.flash.letterly.domain.usecase

import com.flash.letterly.domain.model.Word
import com.flash.letterly.domain.repository.WordRepository
import com.flash.letterly.presentation.shared.GameMode
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UpdateTimeStampUseCaseTest {

    private lateinit var repository: FakeWordRepository
    private lateinit var useCase: UpdateWordTimestampUseCase

    @Before
    fun setup() {
        repository = FakeWordRepository()
        useCase = UpdateWordTimestampUseCase(repository)
    }

    @Test
    fun `updates word timestamp in repository`() = runTest {

        val wordValue = "apple"
        val mode = GameMode.CLASSIC

        useCase(wordValue, mode)

        val updatedWord = repository.updatedWord

        assertEquals(wordValue, updatedWord?.value)
        assertEquals(mode.wordLength, updatedWord?.length)
        assert(updatedWord?.lastAnsweredAt != null)
    }

    private class FakeWordRepository : WordRepository {

        var updatedWord: Word? = null

        override suspend fun getAllWords(): List<Word> {
            return emptyList()
        }

        override suspend fun getRandomWord(length: Int): Word? {
            return null
        }

        override suspend fun getWord(value: String): Word? {
            return null
        }

        override suspend fun updateWord(word: Word) {
            updatedWord = word
        }

        override suspend fun exists(word: String): Boolean {
            return updatedWord?.value == word
        }
    }
}