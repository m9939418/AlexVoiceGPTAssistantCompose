package com.alex.yang.alexvoicegptassistantcompose.feature.voice.data.mapper

import android.util.Base64
import com.alex.yang.alexvoicegptassistantcompose.feature.voice.data.model.ChatDto
import com.alex.yang.alexvoicegptassistantcompose.feature.voice.domain.model.Chat

/**
 * Created by AlexYang on 2025/12/7.
 *
 *
 */
fun ChatDto.toDomain(): Chat {
    val audio = choices?.firstOrNull()?.message?.audio
    val text = audio?.transcript.orEmpty()
    val audioBytes = audio?.data?.let {
        try {
            Base64.decode(it, Base64.DEFAULT)
        } catch (_: IllegalArgumentException) {
            null
        }
    }

    return Chat(
        id = id.orEmpty(),
        text = text,
        audioBytes = audioBytes
    )
}