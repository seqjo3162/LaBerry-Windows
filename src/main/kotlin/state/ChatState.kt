package state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import models.ChatModel
import models.MessageModel

object ChatState {
    val chats = mutableStateListOf<ChatModel>()

    val messages = mutableStateMapOf<Int, SnapshotStateList<MessageModel>>()

    val currentChatId = mutableStateOf<Int?>(null)

    val currentChat
        get() = chats.firstOrNull { it.id == currentChatId.value }

    fun selectChat(chat: ChatModel) {
        currentChatId.value = chat.id
        messages.putIfAbsent(chat.id, mutableStateListOf())
    }

    fun setChats(list: List<ChatModel>) {
        chats.clear()
        chats.addAll(list)
    }

    fun openChat(id: Int) {
        currentChatId.value = id
        messages.putIfAbsent(id, mutableStateListOf())
    }

    fun addMessage(chatId: Int, msg: MessageModel) {
        val list = messages.getOrPut(chatId) { mutableStateListOf() }
        list.add(msg)
    }
}
