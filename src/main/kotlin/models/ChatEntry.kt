package models

sealed class ChatEntry {
    abstract val chatId: Int
    abstract val timestamp: String
}

data class ChatMessageEntry(
    val msg: MessageModel
) : ChatEntry() {
    override val chatId: Int = msg.chat_id
    override val timestamp: String = msg.timestamp
}

data class ChatSystemEntry(
    val system: SystemMessageModel
) : ChatEntry() {
    override val chatId: Int = system.chat_id
    override val timestamp: String = system.timestamp
}
