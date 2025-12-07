package com.alex.yang.alexvoicegptassistantcompose.core.stt

/**
 * Created by AlexYang on 2025/12/6.
 *
 *
 */
sealed interface Speech2TextEvent {
    object Ready : Speech2TextEvent
    object EndOfSpeech : Speech2TextEvent
    data class Partial(val text: String) : Speech2TextEvent
    data class Final(val text: String) : Speech2TextEvent
    data class Error(val message: String) : Speech2TextEvent
}