package models

import kotlinx.serialization.Serializable

@Serializable
data class ChatModel(
    val id: Int,
    val name: String? = null,
    val server_id: Int? = null,
    val is_private: Boolean = false
)
