package com.alex.yang.alexvoicegptassistantcompose.feature.voice.domain.usecase

import com.alex.yang.alexvoicegptassistantcompose.feature.voice.data.model.MessageRequest
import com.alex.yang.alexvoicegptassistantcompose.feature.voice.domain.repository.VoiceRepository
import javax.inject.Inject

/**
 * Created by AlexYang on 2025/12/7.
 *
 *
 */
class FetchChatMessagesUseCase @Inject constructor(
    private val repository: VoiceRepository
) {
    suspend operator fun invoke(messages: List<MessageRequest>) =
        repository.fetchChatCompletions(messages)
}