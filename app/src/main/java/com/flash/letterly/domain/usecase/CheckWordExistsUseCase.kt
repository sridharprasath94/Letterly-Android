package com.flash.letterly.domain.usecase

import com.flash.letterly.domain.repository.WordRepository
import javax.inject.Inject

class CheckWordExistsUseCase @Inject constructor(
    private val repository: WordRepository
) {

    suspend operator fun invoke(word: String): Boolean {
        return repository.exists(word.lowercase())
    }
}