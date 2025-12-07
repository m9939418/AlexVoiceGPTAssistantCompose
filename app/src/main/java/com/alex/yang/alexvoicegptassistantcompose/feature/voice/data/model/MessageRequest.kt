package com.alex.yang.alexvoicegptassistantcompose.feature.voice.data.model

import kotlinx.serialization.Serializable

/**
 * Created by AlexYang on 2025/12/6.
 *
 *
 */
@Serializable
data class MessageRequest(
    val role: String,
    val content: String
)
