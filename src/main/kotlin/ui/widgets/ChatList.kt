
package ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import state.ChatState
import models.ChatModel
import kotlinx.coroutines.launch
import ws.WebSocketManager

@Composable
fun ChatListUI() {
    val scope = rememberCoroutineScope()
    val chats = ChatState.chats
    val current = ChatState.currentChat

    Column(Modifier.fillMaxHeight().width(240.dp).padding(8.dp)) {
        Text("Chats", color = MaterialTheme.colors.onSurface)

        Spacer(Modifier.height(12.dp))

        chats.forEach { chat ->
            ChatItem(chat, current?.id == chat.id) {
                ChatState.selectChat(chat)
                scope.launch { WebSocketManager.connect(chat.id) }
            }
        }
    }
}

@Composable
fun ChatItem(chat: ChatModel, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) MaterialTheme.colors.primary.copy(alpha = 0.3f) else MaterialTheme.colors.surface

    Column(
        Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() }
    ) {
        Text(chat.name ?: "Chat #${chat.id}", color = MaterialTheme.colors.onSurface)
    }
}
