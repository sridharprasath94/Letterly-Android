package com.flash.letterly.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.flash.letterly.data.local.entity.WordEntity

@Dao
interface WordListDao {

    @Query("SELECT * FROM words")
    suspend fun getAllWords(): List<WordEntity>

    @Query("SELECT * FROM words WHERE word = :word LIMIT 1")
    suspend fun getWord(word: String): WordEntity?

    @Query("SELECT * FROM words WHERE length = :length ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomWord(length: Int): WordEntity?

    @Query("SELECT COUNT(*) FROM words")
    suspend fun countWords(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<WordEntity>)

    @Query("SELECT EXISTS(SELECT 1 FROM words WHERE word = :word)")
    suspend fun exists(word: String): Boolean

    @Update
    suspend fun updateWord(word: WordEntity)

    @Query("DELETE FROM words")
    suspend fun clearWords()
}