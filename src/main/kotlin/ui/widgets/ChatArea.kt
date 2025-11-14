package ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import state.ChatState
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

    // --- Заглушка, если чат не выбран вообще ---
    if (chat == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Похоже, у Ла Ягодки пока нет собеседников.",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Откройте чат на сервере или найдите друзей в панели слева.",
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
        }
        return
    }

    // --- Обычный экран чата ---
    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            chat.name ?: "Чат #${chat.id}",
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h6
        )

        Spacer(Modifier.height(8.dp))

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(scroll)
        ) {
            msgs.forEach { msg ->
                Text(
                    "${msg.sender_username}: ${msg.content}",
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(Modifier.height(4.dp))
            }

            LaunchedEffect(msgs.size) {
                scroll.animateScrollTo(scroll.maxValue)
            }
        }

        Row {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Написать сообщение...") }
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
