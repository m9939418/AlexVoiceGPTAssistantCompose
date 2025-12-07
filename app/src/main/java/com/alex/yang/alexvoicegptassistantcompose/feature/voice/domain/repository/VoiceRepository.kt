package com.alex.yang.alexvoicegptassistantcompose.feature.voice.domain.repository

import com.alex.yang.alexvoicegptassistantcompose.core.utils.network.Resource
import com.alex.yang.alexvoicegptassistantcompose.feature.voice.data.model.MessageRequest
import com.alex.yang.alexvoicegptassistantcompose.feature.voice.domain.model.Chat

/**
 * Created by AlexYang on 2025/12/7.
 *
 *
 */
interface VoiceRepository {
    suspend fun fetchChatCompletions(messages: List<MessageRequest>): Resource<Chat>
}