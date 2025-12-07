package com.alex.yang.alexvoicegptassistantcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alex.yang.alexvoicegptassistantcompose.feature.voice.presentation.VoiceScreen
import com.alex.yang.alexvoicegptassistantcompose.feature.voice.presentation.VoiceViewModel
import com.alex.yang.alexvoicegptassistantcompose.ui.theme.AlexVoiceGPTAssistantComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlexVoiceGPTAssistantComposeTheme {
                val viewModel = hiltViewModel<VoiceViewModel>()
                val state by viewModel.uiState.collectAsStateWithLifecycle()

                VoiceScreen(
                    state = state,
                    onMicClick = viewModel::onMicClick,
                )
            }
        }
    }
}