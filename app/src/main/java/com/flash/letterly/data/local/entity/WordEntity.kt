package com.flash.letterly.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Local database representation of a word used in the game.
 *
 * lastAnsweredAt stores the timestamp (epoch millis) of when the user
 * last successfully solved this word. This allows the domain layer to
 * decide whether the word can be reused or if a new random word should
 * be generated (e.g., if it was solved within the last two months).
 */
@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey
    val word: String,
    val length: Int,
    // Epoch time in milliseconds when the word was last answered
    val lastAnsweredAt: Long? = null
)