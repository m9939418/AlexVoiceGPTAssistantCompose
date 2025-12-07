package com.alex.yang.alexvoicegptassistantcompose.feature.voice.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alex.yang.alexvoicegptassistantcompose.feature.voice.presentation.ChatMessage
import com.alex.yang.alexvoicegptassistantcompose.ui.theme.AlexVoiceGPTAssistantComposeTheme

/**
 * Created by AlexYang on 2025/12/7.
 *
 *
 */
@Composable
fun ChatBubble(
    item: ChatMessage
) {
    val bgColor = if (item.isUser) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.tertiaryContainer
    }

    val horizontalArrangement =
        if (item.isUser) Arrangement.End
        else Arrangement.Start

    val shape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomEnd = if (item.isUser) 0.dp else 16.dp,
        bottomStart = if (item.isUser) 16.dp else 0.dp
    )

    val textAlign = if (item.isUser) TextAlign.End else TextAlign.Start

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement
    ) {
        Box(
            modifier = Modifier
                .background(color = bgColor, shape = shape)
                .padding(10.dp)
        ) {
            Text(
                style = MaterialTheme.typography.bodyMedium,
                textAlign = textAlign,
                text = item.text
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Light Mode"
)
@Composable
fun ChatBubblePreview() {
    AlexVoiceGPTAssistantComposeTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            ChatBubble(
                item = ChatMessage(
                    id = 1,
                    isUser = false,
                    text = "這是一個範例的聊天訊息泡泡。這是一個範例的聊天訊息泡泡。這是一個範例的聊天訊息泡泡。這是一個範例的聊天訊息泡泡。這是一個範例的聊天訊息泡泡。"
                )
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Light Mode"
)
@Composable
fun UserChatBubblePreview() {
    AlexVoiceGPTAssistantComposeTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            ChatBubble(
                item = ChatMessage(
                    id = 2,
                    isUser = true,
                    text = "這是一個範例的聊天訊息泡泡。"
                )
            )
        }
    }
}