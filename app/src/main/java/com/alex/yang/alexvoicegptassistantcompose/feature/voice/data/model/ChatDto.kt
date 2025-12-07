package com.alex.yang.alexvoicegptassistantcompose.feature.voice.data.model

import kotlinx.serialization.Serializable

/**
 * Created by AlexYang on 2025/12/6.
 *
 *
 */
@Serializable
data class ChatDto(
    val id: String?,
    val created: Long?,
    val choices: List<ChoiceDto>?,
)
