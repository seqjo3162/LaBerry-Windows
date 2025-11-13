package models

import kotlinx.serialization.Serializable

@Serializable
data class ServerModel(
    val id: Int,
    val name: String,
    val owner_id: Int,
    val created_at: String
)
