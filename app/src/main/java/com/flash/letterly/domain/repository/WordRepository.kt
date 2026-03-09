package com.flash.letterly.domain.repository

import com.flash.letterly.domain.model.Word

/**
 * Domain repository abstraction.
 * The domain layer depends only on this interface and does not know
 * whether the data comes from Room, network, or any other source.
 */
interface WordRepository {

    /**
     * Returns all words available in the local database.
     */
    suspend fun getAllWords(): List<Word>

    /**
     * Returns a random word for the given word length.
     * Example: 5 for CLASSIC, 6 for ADVANCED, 7 for EXPERT.
     */
    suspend fun getRandomWord(length: Int): Word?

    /**
     * Returns a specific word if it exists.
     */
    suspend fun getWord(value: String): Word?

    /**
     * Updates the timestamp when a word was last answered.
     */
    suspend fun updateWord(word: Word)

    /**
     *  Checks if a word exists in the database.
     */
    suspend fun exists(word: String): Boolean
}