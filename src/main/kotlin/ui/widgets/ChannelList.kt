package ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import state.ChatState
import state.ServerState
import kotlinx.coroutines.launch
import ws.WebSocketManager
import models.ChatModel

@Composable
fun ChannelList() {
    val scope = rememberCoroutineScope()
    val server = ServerState.currentServer
    val chats = ChatState.chats.filter { it.server_id == server?.id }
    val currentChat = ChatState.currentChat

    Column(
        Modifier.width(220.dp).fillMaxHeight().padding(12.dp)
    ) {
        Text(server?.name ?: "Channels", color = MaterialTheme.colors.onSurface)
        Spacer(Modifier.height(12.dp))

        chats.forEach { chat ->
            ChannelItem(chat, currentChat?.id == chat.id) {
                ChatState.selectChat(chat)
                scope.launch { WebSocketManager.connect(chat.id) }
            }
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
fun ChannelItem(chat: ChatModel, selected: Boolean, onClick: () -> Unit) {
    val color = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface

    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(6.dp)
    ) {
        Text(text = chat.name ?: "Chat #${chat.id}", color = color)
    }
}
