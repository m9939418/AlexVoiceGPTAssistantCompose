package com.alex.yang.alexvoicegptassistantcompose.feature.voice.data.repository

import com.alex.yang.alexvoicegptassistantcompose.core.utils.network.Resource
import com.alex.yang.alexvoicegptassistantcompose.feature.voice.data.api.OpenAIApi
import com.alex.yang.alexvoicegptassistantcompose.feature.voice.data.mapper.toDomain
import com.alex.yang.alexvoicegptassistantcompose.feature.voice.data.model.ChatRequest
import com.alex.yang.alexvoicegptassistantcompose.feature.voice.data.model.MessageRequest
import com.alex.yang.alexvoicegptassistantcompose.feature.voice.domain.repository.VoiceRepository
import javax.inject.Inject

/**
 * Created by AlexYang on 2025/12/7.
 *
 *
 */
class VoiceRepositoryImpl @Inject constructor(
    private val openAIApi: OpenAIApi
) : VoiceRepository {
    override suspend fun fetchChatCompletions(messages: List<MessageRequest>) =
        try {
            val request = ChatRequest(messages = messages)

            val response = openAIApi.chatCompletions(request)

            Resource.Success(data = response.toDomain())
        } catch (e: Exception) {
            Resource.Error(
                message = e.message ?: "Unknown error",
                throwable = e
            )
        }
}