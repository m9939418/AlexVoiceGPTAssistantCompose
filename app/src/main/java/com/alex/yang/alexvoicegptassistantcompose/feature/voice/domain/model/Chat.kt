package com.alex.yang.alexvoicegptassistantcompose.feature.voice.domain.model

/**
 * Created by AlexYang on 2025/12/7.
 *
 *
 */
data class Chat(
    val id: String,
    val text: String,
    val audioBytes: ByteArray?
)
