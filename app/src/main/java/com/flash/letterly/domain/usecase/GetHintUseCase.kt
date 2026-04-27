package com.flash.letterly.domain.usecase

import com.flash.letterly.domain.repository.HintRepository
import javax.inject.Inject

class GetHintUseCase @Inject constructor(
    private val repository: HintRepository
) {
    suspend operator fun invoke(word: String, previousHints: List<String>): Result<String> {
        return repository.getHint(word, previousHints)
    }
}
