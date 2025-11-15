package ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import models.ChatEntry
import models.ChatMessageEntry
import models.ChatModel
import models.ChatSystemEntry
import state.ChatState
import ws.WebSocketChatManager

@Composable
fun ChatView(chat: ChatModel) {
    Column(modifier = Modifier.fillMaxSize()) {

        ChatTopBar(chat)

        Divider(color = Color(0xFF4F545C))

        MessageListView(chat.id)

        Divider(color = Color(0xFF4F545C))

        MessageInput(chat.id)
    }
}

@Composable
fun ChatTopBar(chat: ChatModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(0xFF36393F))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            chat.name ?: "Chat",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
    }
}

@Composable
fun MessageListView(chatId: Int) {
    val list: List<ChatEntry> = ChatState.messages[chatId] ?: emptyList()
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Auto-scroll on new message
    LaunchedEffect(list.size) {
        if (list.isNotEmpty()) {
            delay(50)
            scope.launch {
                state.animateScrollToItem(list.lastIndex)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2F3136))
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            itemsIndexed(list) { index, entry ->
                when (entry) {
                    is ChatMessageEntry -> {
                        val prev = list.getOrNull(index - 1)
                        val prevMsg = (prev as? ChatMessageEntry)?.msg

                        val grouped = prevMsg != null &&
                                prevMsg.sender_id == entry.msg.sender_id &&
                                prevMsg.timestamp.substring(0, 16) ==
                                entry.msg.timestamp.substring(0, 16)

                        MessageBubble(entry.msg, grouped)
                    }

                    is ChatSystemEntry -> {
                        SystemMessageBubble(entry.system.text)
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(msg: models.MessageModel, grouped: Boolean) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = if (grouped) 2.dp else 6.dp)
    ) {

        if (!grouped) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF5865F2)),
                contentAlignment = Alignment.Center
            ) {
                val letter = msg.sender_username.firstOrNull()?.uppercaseChar() ?: '?'
                Text(
                    letter.toString(),
                    color = Color.White,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }
            Spacer(Modifier.width(10.dp))
        } else {
            Spacer(Modifier.width(46.dp))
        }

        Column(modifier = Modifier.weight(1f)) {

            if (!grouped) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        msg.sender_username,
                        color = Color.White,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        msg.timestamp.substring(11, 16),
                        color = Color(0xFFBBBBBB),
                        fontSize = 12.sp
                    )
                }
            }

            Text(
                msg.content,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun MessageInput(chatId: Int) {
    var text by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF40444B))
            .padding(10.dp)
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Write a message…", color = Color(0xFF999999)) },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.White,
                focusedContainerColor = Color(0xFF40444B),
                unfocusedContainerColor = Color(0xFF40444B),
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
        )

        Spacer(Modifier.width(10.dp))

        Button(onClick = {
            if (text.isNotBlank()) {
                val safe = text
                text = ""
                scope.launch {
                    WebSocketChatManager.send(safe)
                }
            }
        }) {
            Text("Send")
        }
    }
}
