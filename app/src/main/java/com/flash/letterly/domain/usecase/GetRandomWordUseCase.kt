package com.flash.letterly.domain.usecase

import com.flash.letterly.domain.model.Word
import com.flash.letterly.domain.repository.WordRepository
import com.flash.letterly.presentation.shared.GameMode
import javax.inject.Inject

class GetRandomWordUseCase @Inject constructor(
    private val repository: WordRepository
) {

    suspend operator fun invoke(mode: GameMode): Word? {
        repeat(10) {
            val word = repository.getRandomWord(mode.wordLength) ?: return null

            if (word.lastAnsweredAt == null || isOlderThanTenDays(word)) {
                return word
            }
        }
        return null
    }

    private fun isOlderThanTenDays(word: Word): Boolean {
        val lastAnswered = word.lastAnsweredAt ?: return true

        val tenDaysInMillis = 10L * 24 * 60 * 60 * 1000
        val now = System.currentTimeMillis()

        return now - lastAnswered >= tenDaysInMillis
    }
}