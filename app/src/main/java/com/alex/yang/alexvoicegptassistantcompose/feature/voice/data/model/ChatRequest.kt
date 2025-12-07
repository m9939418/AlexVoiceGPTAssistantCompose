package com.alex.yang.alexvoicegptassistantcompose.feature.voice.data.model

import kotlinx.serialization.Serializable

/**
 * Created by AlexYang on 2025/12/6.
 *
 *
 */
@Serializable
data class ChatRequest(
    val model: String = "gpt-4o-mini-audio-preview",
    val modalities: List<String> = listOf("text", "audio"),
    val audio: AudioRequest,
    val messages: List<MessageRequest>
)
