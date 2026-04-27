package com.flash.letterly.domain.repository

interface HintRepository {
    suspend fun getHint(word: String, previousHints: List<String>): Result<String>
}
