package com.flash.letterly.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GroqRequest(
    val model: String = "llama-3.1-8b-instant",
    val messages: List<GroqMessage>,
    @SerializedName("max_tokens") val maxTokens: Int = 60
)

data class GroqMessage(
    val role: String,
    val content: String
)
