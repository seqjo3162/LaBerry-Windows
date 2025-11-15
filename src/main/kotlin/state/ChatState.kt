package state

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import models.ChatEntry
import models.ChatMessageEntry
import models.ChatModel
import models.MessageModel
import models.SystemMessageModel
import models.ChatSystemEntry
import ws.WebSocketChatManager

object ChatState {

    private val unreadMap = mutableStateMapOf<Int, Int>()

    val chats = mutableStateListOf<ChatModel>()

    val messages = mutableStateMapOf<Int, SnapshotStateList<ChatEntry>>()

    val currentChatId = mutableStateOf<Int?>(null)

    val currentChat
        get() = chats.firstOrNull { it.id == currentChatId.value }

    fun selectChat(chat: ChatModel) {
        currentChatId.value = chat.id
        messages.putIfAbsent(chat.id, mutableStateListOf())
        unreadMap[chat.id] = 0
    }

    fun setChats(list: List<ChatModel>) {
        chats.clear()
        chats.addAll(list)
    }

    fun openChat(id: Int) {
        currentChatId.value = id
        messages.putIfAbsent(id, mutableStateListOf())
        unreadMap[id] = 0
    }

    fun addMessage(chatId: Int, msg: MessageModel) {
        val list = messages.getOrPut(chatId) { mutableStateListOf() }
        list.add(ChatMessageEntry(msg))

        if (currentChatId.value != chatId) {
            unreadMap[chatId] = (unreadMap[chatId] ?: 0) + 1
        }
    }

    fun addSystemMessage(chatId: Int, text: String, timestamp: String) {
        val list = messages.getOrPut(chatId) { mutableStateListOf() }
        list.add(ChatSystemEntry(SystemMessageModel(chatId, text, timestamp)))
    }

    fun getUnread(chatId: Int): Int {
        return unreadMap[chatId] ?: 0
    }

    // ----------------------------- CHAT LIST -----------------------------
    @Composable
    fun ChatList() {
        val chats = ChatState.chats
        val selected = ChatState.currentChatId.value

        LazyColumn {
            items(chats) { chat ->
                val isSel = chat.id == selected

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .background(if (isSel) Color(0xFF40444B) else Color.Transparent)
                        .clickable {
                            openChat(chat.id)
                            WebSocketChatManager.stop()
                            WebSocketChatManager.start(chat.id)
                        }
                        .padding(10.dp)
                ) {
                    Text(
                        chat.name ?: "Chat",
                        color = Color.White
                    )
                }
            }
        }
    }

    // ----------------------------- CHAT TOP BAR -----------------------------
    @Composable
    fun TopBarChat(chat: ChatModel) {
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
                color = Color.White
            )
        }
    }

    // ----------------------------- MESSAGE LIST -----------------------------
    @Composable
    fun MessageList(chatId: Int) {
        val list = ChatState.messages[chatId] ?: emptyList<ChatEntry>()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            items(list) { entry ->
                when (entry) {
                    is ChatMessageEntry -> {
                        Column(Modifier.padding(vertical = 4.dp)) {
                            Text(
                                entry.msg.sender_username + ":",
                                color = Color(0xFF7289DA)
                            )
                            Text(entry.msg.content, color = Color.White)
                        }
                    }

                    is ChatSystemEntry -> {
                        Text(
                            entry.system.text,
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }

    // ----------------------------- MESSAGE INPUT -----------------------------
    @Composable
    fun MessageInput(chatId: Int) {
        var text by remember { mutableStateOf("") }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF40444B))
                .padding(8.dp)
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                placeholder = {
                    Text("Write a message…", color = Color(0xFF999999))
                }
            )

            Spacer(Modifier.width(8.dp))

            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        val safe = text
                        text = ""
                        CoroutineScope(Dispatchers.IO).launch {
                            WebSocketChatManager.send(safe)
                        }
                    }
                }
            ) {
                Text("Send")
            }
        }
    }
}
