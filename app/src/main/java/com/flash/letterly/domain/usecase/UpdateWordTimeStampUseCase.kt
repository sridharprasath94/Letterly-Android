package com.flash.letterly.domain.usecase

import com.flash.letterly.domain.repository.WordRepository
import com.flash.letterly.domain.model.Word
import com.flash.letterly.presentation.shared.GameMode
import javax.inject.Inject

class UpdateWordTimestampUseCase @Inject constructor(
    private val repository: WordRepository
) {
    suspend operator fun invoke(word: String, mode: GameMode) {
        val domainWord = Word(
            value = word.lowercase(),
            length = mode.wordLength,
            lastAnsweredAt = System.currentTimeMillis()
        )

        repository.updateWord(domainWord)
    }
}