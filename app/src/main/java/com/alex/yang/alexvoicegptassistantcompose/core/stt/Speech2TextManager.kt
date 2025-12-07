package com.alex.yang.alexvoicegptassistantcompose.core.stt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

/**
 * Created by AlexYang on 2025/12/6.
 *
 *
 */
class Speech2TextManager @Inject constructor(
    private val context: Context
) {
    private val _events = MutableSharedFlow<Speech2TextEvent>(
        replay = 0,
        extraBufferCapacity = 16,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<Speech2TextEvent> = _events

    private val recognitionListener = object : RecognitionListener {
        override fun onBeginningOfSpeech() = Unit

        override fun onBufferReceived(buffer: ByteArray?) = Unit

        override fun onEndOfSpeech() {
            _events.tryEmit(Speech2TextEvent.EndOfSpeech)
        }

        override fun onError(error: Int) {
            val msg = mapError(error)
            Log.e("DEBUG", "onError: $msg ($error)")
            _events.tryEmit(Speech2TextEvent.Error(msg))
        }

        override fun onEvent(eventType: Int, params: Bundle?) = Unit

        override fun onPartialResults(partialResults: Bundle?) {
            val text = partialResults
                ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.firstOrNull()
                .orEmpty()
            _events.tryEmit(Speech2TextEvent.Partial(text))
        }

        override fun onReadyForSpeech(params: Bundle?) {
            Log.d("DEBUG", "onReadyForSpeech() called")

            _events.tryEmit(Speech2TextEvent.Ready)
        }

        override fun onResults(results: Bundle?) {
            val text = results
                ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.firstOrNull()
                .orEmpty()
            _events.tryEmit(Speech2TextEvent.Final(text))
        }

        override fun onRmsChanged(rmsdB: Float) = Unit
    }

    private val speechRecognizer: SpeechRecognizer by lazy {
        SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(recognitionListener)
        }
    }

    fun startListening(languageTag: String = "zh-TW") {
        Log.d("DEBUG", "startListening() called")

        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            Log.e("DEBUG", "Speech recognition is not available on this device")
            _events.tryEmit(Speech2TextEvent.Error("Speech recognition is not available"))
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageTag)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }

        runCatching {
            speechRecognizer.startListening(intent)
        }.onFailure { e ->
            Log.e("DEBUG", "startListening failed", e)
            _events.tryEmit(Speech2TextEvent.Error("Failed to start listening: ${e.message}"))
        }
    }

    fun stopListening() {
        runCatching { speechRecognizer.stopListening() }
    }

    fun destroy() {
        runCatching { speechRecognizer.destroy() }
    }

    private fun mapError(errorCode: Int): String =
        when (errorCode) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> "Client side error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> "Network error"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            SpeechRecognizer.ERROR_NO_MATCH -> "No match for your speech"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Speech recognizer is busy"
            SpeechRecognizer.ERROR_SERVER -> "Server error"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
            else -> "Unknown error"
        }
}