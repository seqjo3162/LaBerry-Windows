package models

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val access_token: String? = null,
    val token_type: String? = null,
    val user_id: Int? = null,
    val requires_2fa: Boolean? = null
)

