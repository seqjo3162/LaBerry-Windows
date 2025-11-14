package ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import models.ChatModel
import state.ChatState
import state.ServerState
import ws.WebSocketManager

@Composable
fun ChannelList() {
    val scope = rememberCoroutineScope()

    val server = ServerState.currentServer
    val chats = ChatState.chats.filter { chat ->
        server != null && chat.server_id == server.id
    }
    val currentChatId = ChatState.currentChat?.id

    Column(
        modifier = Modifier
            .width(220.dp)
            .fillMaxHeight()
            .padding(12.dp)
    ) {
        Text(
            text = server?.name ?: "Channels",
            color = MaterialTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        chats.forEach { chat ->
            ChannelItem(
                chat = chat,
                selected = (currentChatId == chat.id),
                onClick = {
                    ChatState.selectChat(chat)
                    scope.launch {
                        WebSocketManager.connect(chat.id)
                    }
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun ChannelItem(
    chat: ChatModel,
    selected: Boolean,
    onClick: () -> Unit
) {
    val color =
        if (selected) MaterialTheme.colors.primary
        else MaterialTheme.colors.onSurface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(6.dp)
    ) {
        Text(
            text = chat.name ?: "Chat #${chat.id}",
            color = color
        )
    }
}
