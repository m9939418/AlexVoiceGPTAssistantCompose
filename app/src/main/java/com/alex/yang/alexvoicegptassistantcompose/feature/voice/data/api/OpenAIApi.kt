package com.alex.yang.alexvoicegptassistantcompose.feature.voice.data.api

import com.alex.yang.alexvoicegptassistantcompose.feature.voice.data.model.ChatDto
import com.alex.yang.alexvoicegptassistantcompose.feature.voice.data.model.ChatRequest
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by AlexYang on 2025/12/7.
 *
 *
 */
interface OpenAIApi {
    @POST("/v1/chat/completions")
    suspend fun chatCompletions(@Body request: ChatRequest): ChatDto
}