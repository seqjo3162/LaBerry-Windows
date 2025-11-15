package ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import state.ChatState
import models.ChatMessageEntry
import models.ChatSystemEntry
import ws.WebSocketManager

@Composable
fun ChatArea() {
    val scope = rememberCoroutineScope()

    val currentChatId = ChatState.currentChatId.value
    val chat = ChatState.chats.firstOrNull { it.id == currentChatId }
    val msgs = if (currentChatId != null) {
        ChatState.messages[currentChatId] ?: emptyList()
    } else {
        emptyList()
    }

    var text by remember(currentChatId) { mutableStateOf("") }
    val scroll = rememberScrollState()

    // ---------- Placeholder when no chat selected ----------
    if (chat == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Похоже, у Ла Ягодки пока нет собеседников.",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Откройте чат на сервере или найдите друзей в панели слева.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
        return
    }

    // ---------- Chat UI ----------
    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            chat.name ?: "Чат #${chat.id}",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(8.dp))

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(scroll)
        ) {
            msgs.forEach { entry ->
                when (entry) {

                    is ChatMessageEntry -> {
                        Text(
                            "${entry.msg.sender_username}: ${entry.msg.content}",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    is ChatSystemEntry -> {
                        Text(
                            "[System] ${entry.system.text}",
                            color = Color.Gray
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
            }

            LaunchedEffect(msgs.size) {
                scroll.animateScrollTo(scroll.maxValue)
            }
        }

        Row {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text("Написать сообщение...")
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )

            Button(
                onClick = {
                    val content = text.trim()
                    if (content.isNotEmpty()) {
                        scope.launch { WebSocketManager.send(content) }
                        text = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Send")
            }
        }
    }
}
