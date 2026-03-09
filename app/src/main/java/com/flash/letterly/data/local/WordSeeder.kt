package com.flash.letterly.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import com.flash.letterly.data.local.dao.WordListDao
import com.flash.letterly.data.local.entity.WordEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WordSeeder @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val dao: WordListDao
) {

    suspend fun seedIfNeeded() = withContext(Dispatchers.IO) {

        val count = dao.countWords()

        // Already seeded
        if (count > 0) return@withContext

        seedFile("words_5.txt", 5)
        seedFile("words_6.txt", 6)
        seedFile("words_7.txt", 7)
    }

    private suspend fun seedFile(file: String, length: Int) {

        val words = context.assets
            .open(file)
            .bufferedReader()
            .readLines()
            .map {
                WordEntity(
                    word = it.lowercase(),
                    length = length,
                    lastAnsweredAt = null
                )
            }

        dao.insertWords(words)
    }
}