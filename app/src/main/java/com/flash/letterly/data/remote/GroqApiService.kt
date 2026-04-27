package com.flash.letterly.data.remote

import com.flash.letterly.data.remote.dto.GroqRequest
import com.flash.letterly.data.remote.dto.GroqResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GroqApiService {

    @POST("openai/v1/chat/completions")
    suspend fun getChatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: GroqRequest
    ): GroqResponse
}
