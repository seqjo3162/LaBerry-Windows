package state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import models.ChatModel
import models.MessageModel

object ChatState {
    val chats = mutableStateListOf<ChatModel>()
    val messages = mutableMapOf<Int, MutableList<MessageModel>>()

    val currentChatId = mutableStateOf<Int?>(null)

    fun setChats(list: List<ChatModel>) {
        chats.clear()
        chats.addAll(list)
    }

    fun openChat(id: Int) {
        currentChatId.value = id
        messages.putIfAbsent(id, mutableListOf())
    }

    fun addMessage(chatId: Int, msg: MessageModel) {
        messages.getOrPut(chatId) { mutableListOf() }.add(msg)
    }
}
