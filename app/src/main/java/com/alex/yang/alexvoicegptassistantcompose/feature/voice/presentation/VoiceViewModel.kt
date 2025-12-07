package com.alex.yang.alexvoicegptassistantcompose.feature.voice.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.yang.alexvoicegptassistantcompose.core.audio.AudioPlayer
import com.alex.yang.alexvoicegptassistantcompose.core.stt.Speech2TextEvent
import com.alex.yang.alexvoicegptassistantcompose.core.stt.Speech2TextManager
import com.alex.yang.alexvoicegptassistantcompose.core.utils.network.Resource
import com.alex.yang.alexvoicegptassistantcompose.feature.voice.data.model.MessageRequest
import com.alex.yang.alexvoicegptassistantcompose.feature.voice.domain.usecase.FetchChatMessagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by AlexYang on 2025/12/7.
 *
 *
 */
@HiltViewModel
class VoiceViewModel @Inject constructor(
    private val stt: Speech2TextManager,
    private val audioPlayer: AudioPlayer,
    private val useCase: FetchChatMessagesUseCase
) : ViewModel() {
    // UI 狀態
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val messagesHistory = mutableListOf<MessageRequest>()
    private var messageId = 0L
    private var sttJob: Job? = null

    init {
        messagesHistory += MessageRequest(
            role = "developer",
            content = "You are a helpful voice assistant. Reply in Traditional Chinese."
        )

        observeSttEvents()
    }

    private fun observeSttEvents() {
        sttJob = viewModelScope.launch {
            stt.events.collectLatest { event ->
                when (event) {
                    is Speech2TextEvent.Ready -> {
                        _uiState.update { it.copy(isListening = true, errorMessage = null) }
                    }

                    is Speech2TextEvent.EndOfSpeech -> {
                        _uiState.update { it.copy(isListening = false) }
                    }

                    is Speech2TextEvent.Partial -> { }

                    is Speech2TextEvent.Final -> {
                        sendMessages(event.text)
                    }

                    is Speech2TextEvent.Error -> {
                        _uiState.update {
                            it.copy(isListening = false, errorMessage = event.message)
                        }
                    }
                }
            }
        }
    }

    private fun sendMessages(text: String) {
        if (text.isBlank()) return

        // 1. 加入 User 泡泡
        val userMessage = ChatMessage(
            id = ++messageId,
            isUser = true,
            text = text
        )
        _uiState.update { it.copy(messages = it.messages + userMessage) }

        // 2. 放進 request history
        messagesHistory += MessageRequest(
            role = "user",
            content = text
        )

        // 3. 呼叫 OpenAI
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val resource = useCase(messagesHistory.toList())) {
                is Resource.Success -> {
                    val chat = resource.data

                    val replyText = chat.text.ifBlank { "(空回覆)" }

                    // 4-1. 把 AI 回覆也放進 history
                    messagesHistory += MessageRequest(
                        role = "assistant",
                        content = replyText
                    )

                    // 4-2. 加入 Assistant 泡泡
                    val assistantMessage = ChatMessage(
                        id = ++messageId,
                        isUser = false,
                        text = replyText
                    )

                    _uiState.update {
                        it.copy(
                            messages = it.messages + assistantMessage,
                            isLoading = false
                        )
                    }

                    // 4-3. 播放語音
                    chat.audioBytes?.let { bytes ->
                        audioPlayer.play(bytes)
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = resource.message)
                    }
                }
            }
        }
    }

    // Mic 按鈕：開始 / 停止聽
    fun onMicClick() {
        if (_uiState.value.isListening) {
            stt.stopListening()
            _uiState.update { it.copy(isListening = false) }
        } else {
            _uiState.update { it.copy(errorMessage = null) }
            stt.startListening()
        }
    }

    override fun onCleared() {
        super.onCleared()
        stt.destroy()
        audioPlayer.stop()
        sttJob?.cancel()
    }

    data class UiState(
        val isListening: Boolean = false,
        val isLoading: Boolean = false,
        val messages: List<ChatMessage> = emptyList(),
        val errorMessage: String? = null
    )
}