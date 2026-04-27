package com.flash.letterly.data.repository

import com.flash.letterly.BuildConfig
import com.flash.letterly.data.remote.GroqApiService
import com.flash.letterly.data.remote.dto.GroqMessage
import com.flash.letterly.data.remote.dto.GroqRequest
import com.flash.letterly.domain.repository.HintRepository
import javax.inject.Inject

class HintRepositoryImpl @Inject constructor(
    private val groqApiService: GroqApiService
) : HintRepository {

    override suspend fun getHint(word: String, previousHints: List<String>): Result<String> {
        return try {
            val avoidClause = if (previousHints.isNotEmpty()) {
                val formatted = previousHints.joinToString("; ") { "\"$it\"" }
                " Do not repeat or rephrase these previous hints: $formatted."
            } else ""
            val response = groqApiService.getChatCompletion(
                authorization = "Bearer ${BuildConfig.GROQ_API_KEY}",
                request = GroqRequest(
                    messages = listOf(
                        GroqMessage(
                            role = "user",
                            content = "Give a single concise hint (under 15 words) for the word \"$word\" in a word guessing game. Describe its meaning or category without revealing the word itself.$avoidClause"
                        )
                    )
                )
            )
            val hint = response.choices.firstOrNull()?.message?.content
            if (hint != null) Result.success(hint.trim())
            else Result.failure(Exception("No hint received"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
