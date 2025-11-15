package models

data class SystemMessageModel(
    val chat_id: Int,
    val text: String,
    val timestamp: String
)
