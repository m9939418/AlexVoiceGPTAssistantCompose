package com.alex.yang.alexvoicegptassistantcompose.feature.voice.data.model

import kotlinx.serialization.Serializable

/**
 * Created by AlexYang on 2025/12/7.
 *
 *
 */
@Serializable
data class AudioDto(
    val id: String?,
    val data: String?
)
