package com.alex.yang.alexvoicegptassistantcompose.feature.voice.presentation

/**
 * Created by AlexYang on 2025/12/7.
 *
 *
 */
data class ChatMessage(
    val id: Long,
    val isUser: Boolean,
    val text: String
)
