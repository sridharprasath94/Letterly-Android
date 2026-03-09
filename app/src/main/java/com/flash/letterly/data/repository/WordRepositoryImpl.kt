package com.flash.letterly.data.repository

import com.flash.letterly.data.local.dao.WordListDao
import com.flash.letterly.data.local.mapper.toDomain
import com.flash.letterly.data.local.mapper.toEntity
import com.flash.letterly.domain.model.Word
import com.flash.letterly.domain.repository.WordRepository
import javax.inject.Inject

class WordRepositoryImpl @Inject constructor(
    private val dao: WordListDao
) : WordRepository {

    override suspend fun getAllWords(): List<Word> {
        return dao.getAllWords()
            .map { it.toDomain() }
    }

    override suspend fun getRandomWord(length: Int): Word? {
        return dao.getRandomWord(length)?.toDomain()
    }

    override suspend fun getWord(value: String): Word? {
        return dao.getWord(value)?.toDomain()
    }

    override suspend fun updateWord(word: Word) {
        dao.updateWord(word.toEntity())
    }

    override suspend fun exists(word: String): Boolean {
        return dao.exists(word)
    }
}