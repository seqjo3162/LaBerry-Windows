package models

import kotlinx.serialization.Serializable

@Serializable
data class MessageModel(
    val id: Int,
    val chat_id: Int,
    val sender_id: Int,
    val sender_username: String,
    val content: String,
    val timestamp: String
)
