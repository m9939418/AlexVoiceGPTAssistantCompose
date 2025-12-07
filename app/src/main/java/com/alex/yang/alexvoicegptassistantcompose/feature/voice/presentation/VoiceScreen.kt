package com.alex.yang.alexvoicegptassistantcompose.feature.voice.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.alex.yang.alexvoicegptassistantcompose.feature.voice.presentation.component.ChatBubble
import com.alex.yang.alexvoicegptassistantcompose.ui.theme.AlexVoiceGPTAssistantComposeTheme

/**
 * Created by AlexYang on 2025/12/7.
 *
 *
 */
@Composable
fun VoiceScreen(
    modifier: Modifier = Modifier,
    state: VoiceViewModel.UiState,
    onMicClick: () -> Unit = {}
) {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {}
    )

    val requestRecordPermissionIfNeeded = remember {
        { onGranted: () -> Unit ->
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED

            if (granted) {
                onGranted()
            } else {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 48.dp)
        ) {
            items(
                items = state.messages,
                key = { it.id }
            ) { item ->
                ChatBubble(item = item)
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp),
            onClick = {
                requestRecordPermissionIfNeeded {
                    onMicClick()
                }
            }
        ) {
            Text(text = if (state.isListening) "Stop" else "Mic")
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Light Mode"
)
@Preview(
    showBackground = true,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun VoiceScreenPreview() {
    AlexVoiceGPTAssistantComposeTheme {
        VoiceScreen(
            state = VoiceViewModel.UiState(
                isListening = false,
                isLoading = false,
                messages = listOf(
                    ChatMessage(
                        id = 1,
                        isUser = true,
                        text = "Hello, how are you?"
                    ),
                    ChatMessage(
                        id = 2,
                        isUser = false,
                        text = "I'm fine, thank you!I'm fine, thank you!I'm fine, thank you!I'm fine, thank you!I'm fine, thank you!"
                    ),
                    ChatMessage(
                        id = 3,
                        isUser = true,
                        text = "What's the weather like today?"
                    ),
                    ChatMessage(
                        id = 4,
                        isUser = false,
                        text = "It's sunny and warm."
                    )
                )
            )
        )
    }
}