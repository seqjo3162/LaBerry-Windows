package api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.request.forms.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import config.ServerConfig

object ApiClient {

    val BASE_URL: String
        get() = ServerConfig.BASE_URL

    val http = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
    }

    // ----------------------------
    // MODELS
    // ----------------------------

    @Serializable
    data class LoginResponse(
        val access_token: String? = null,
        val token_type: String? = null,
        val user_id: Int? = null,
        val requires_2fa: Boolean? = null
    )

    @Serializable
    data class RegisterRequest(
        val username: String,
        val password: String,
        val email: String? = null
    )

    @Serializable
    data class MeResponse(
        val id: Int? = null,
        val username: String? = null,
        val display_name: String? = null,
        val avatar_url: String? = null,
        val status: String? = null,
    )

    // ----------------------------
    // REGISTER (JSON)
    // ----------------------------

    suspend fun register(username: String, password: String, email: String? = null): LoginResponse? {
        return try {
            http.post("$BASE_URL/api/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(username, password, email))
            }.body()
        } catch (e: Exception) {
            println("REGISTER ERROR: ${e.message}")
            null
        }
    }

    // ----------------------------
    // LOGIN (FormUrlEncoded)
    // ----------------------------

    suspend fun login(username: String, password: String): LoginResponse? {
        return try {
            http.submitForm(
                url = "$BASE_URL/api/auth/login",
                formParameters = Parameters.build {
                    append("username", username)
                    append("password", password)
                }
            ).body()
        } catch (e: Exception) {
            println("LOGIN ERROR: ${e.message}")
            null
        }
    }

    // ----------------------------
    // VERIFY 2FA (FormUrlEncoded)
    // ----------------------------

    suspend fun verify2FA(userId: Int, code: String): LoginResponse? {
        return try {
            http.submitForm(
                url = "$BASE_URL/api/auth/verify-2fa",
                formParameters = Parameters.build {
                    append("user_id", userId.toString())
                    append("code", code)
                }
            ).body()
        } catch (e: Exception) {
            println("2FA VERIFY ERROR: ${e.message}")
            null
        }
    }

    // ----------------------------
    // GET ME
    // ----------------------------

    suspend fun getMe(token: String): MeResponse? {
        return try {
            http.get("$BASE_URL/api/users/me") {
                header("Authorization", "Bearer $token")
            }.body()
        } catch (e: Exception) {
            println("GET ME ERROR: ${e.message}")
            null
        }
    }
}
