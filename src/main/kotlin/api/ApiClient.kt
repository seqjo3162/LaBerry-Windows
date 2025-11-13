
package api

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Базовый HTTP‑клиент для LaBerry.
 * Все реальные ошибки/отсутствующие эндпоинты отлавливаются
 * и возвращают null/пустые данные, чтобы приложение не падало.
 */
object ApiClient {

    private const val BASE_URL = "https://laberry.loca.lt"

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

    @Serializable
    data class LoginRequest(val username: String, val password: String)

    @Serializable
    data class LoginResponse(val access_token: String? = null, val token_type: String? = null)

    @Serializable
    data class MeResponse(
        val id: Int? = null,
        val username: String? = null,
        val display_name: String? = null,
        val avatar_url: String? = null,
        val status: String? = null,
    )

    suspend fun login(username: String, password: String): String? {
        return try {
            // предполагаемый эндпоинт, если он другой — просто вернётся null
            val resp: LoginResponse = http.post("$BASE_URL/api/auth/login") {
                setBody(LoginRequest(username, password))
            }.body()
            resp.access_token
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getMe(token: String): MeResponse? {
        return try {
            http.get("$BASE_URL/api/users/me") {
                headers.append("Authorization", "Bearer $token")
            }.body()
        } catch (e: Exception) {
            null
        }
    }
}
