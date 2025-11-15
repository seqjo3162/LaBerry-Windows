package ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import models.ChatModel
import models.ChatMessageEntry
import models.ChatSystemEntry
import state.ChatState
import ws.WebSocketChatManager

@Composable
fun ChatListView() {
    val chats = ChatState.chats
    val current = ChatState.currentChatId.value

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .width(260.dp)
            .background(Color(0xFF2F3136))
            .padding(12.dp)
    ) {
        item {
            Text(
                "Chats",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        items(chats) { chat ->
            ChatListItem(chat, isSelected = chat.id == current)
        }
    }
}

@Composable
fun ChatListItem(chat: ChatModel, isSelected: Boolean) {

    val lastEntry = ChatState.messages[chat.id]?.lastOrNull()

    // ---------- Fix: lastEntry is ChatEntry, must unwrap it ----------
    val preview: String = when (lastEntry) {
        is ChatMessageEntry ->
            "${lastEntry.msg.sender_username}: ${
                trimPreview(lastEntry.msg.content)
            }"

        is ChatSystemEntry ->
            "[System] ${trimPreview(lastEntry.system.text)}"

        else -> "No messages yet"
    }

    val unread = ChatState.getUnread(chat.id)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(if (isSelected) Color(0xFF393C43) else Color.Transparent)
            .clickable {
                ChatState.openChat(chat.id)
                WebSocketChatManager.stop()
                WebSocketChatManager.start(chat.id)
            }
            .padding(8.dp)
    ) {

        // -------------- Avatar --------------
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF5865F2)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = (chat.name?.firstOrNull()?.uppercaseChar()?.toString() ?: "C"),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.width(10.dp))

        // -------------- Chat title + last message --------------
        Column(modifier = Modifier.weight(1f)) {

            Text(
                chat.name ?: "Chat",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Text(
                text = preview,
                color = Color(0xFFBBBBBB),
                fontSize = 12.sp,
                maxLines = 1
            )
        }

        // -------------- Time + unread --------------
        Column(horizontalAlignment = Alignment.End) {

            // Timestamp (if exists)
            if (lastEntry is ChatMessageEntry) {
                Text(
                    formatTimeShort(lastEntry.msg.timestamp),
                    color = Color(0xFF999999),
                    fontSize = 11.sp
                )
            }

            // Unread bubble
            if (unread > 0) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFED4245))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        unread.toString(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

fun formatTimeShort(ts: String): String {
    return try {
        ts.substring(11, 16) // HH:mm
    } catch (_: Exception) {
        ""
    }
}

fun trimPreview(text: String): String {
    return if (text.length <= 20) text else text.substring(0, 20) + "…"
}
