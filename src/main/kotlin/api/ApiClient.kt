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
import models.LoginResponse

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
    data class UserResponse(
        val id: Int? = null,
        val username: String? = null,
        val display_name: String? = null,
        val avatar_url: String? = null,
        val status: String? = null
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

    suspend fun register(username: String, password: String, email: String?): LoginResponse? {
        return try {
            val resp = http.post("$BASE_URL/api/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(username, password, email))
            }

            val raw = resp.bodyAsText()
            println("REGISTER RAW = $raw")

            val parsed = Json.decodeFromString(LoginResponse.serializer(), raw)

            println("TOKEN = ${parsed.access_token}")
            println("2FA = ${parsed.requires_2fa}")
            println("USER ID = ${parsed.user_id}")

            parsed

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
            val resp = http.submitForm(
                url = "$BASE_URL/api/auth/login",
                formParameters = Parameters.build {
                    append("username", username)
                    append("password", password)
                }

            )

            val raw = resp.bodyAsText()
            println("LOGIN RAW RESPONSE = $raw")
            println("LOGIN STATUS = ${resp.status}")
            println("LOGIN HEADERS = ${resp.headers.entries()}")

            resp.body<LoginResponse>()
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
            val resp = http.submitForm(
                url = "$BASE_URL/api/auth/verify-2fa",
                formParameters = Parameters.build {
                    append("user_id", userId.toString())
                    append("code", code)
                }
            )

            val raw = resp.bodyAsText()
            println("VERIFY 2FA RAW RESPONSE = $raw")

            resp.body<LoginResponse>()
        } catch (e: Exception) {
            println("2FA ERROR: ${e.message}")
            null
        }
    }

    // ----------------------------
    // GET ME
    // ----------------------------

    suspend fun getMe(token: String): UserResponse? {
        return try {
            http.get("$BASE_URL/api/users/me") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body<UserResponse>()
        } catch (e: Exception) {
            null
        }
    }

    // ----------------------------
    // GENERIC GET REQUEST (Authorized)
    // ----------------------------
    suspend inline fun <reified T> authGet(path: String, token: String): T? {
        return try {
            http.get("$BASE_URL$path") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body<T>()
        } catch (e: Exception) {
            println("authGet ERROR: ${e.message}")
            null
        }
    }

    // ----------------------------
    // GENERIC GET (no auth)
    // ----------------------------
    suspend inline fun <reified T> get(path: String): T? {
        return try {
            http.get("$BASE_URL$path").body<T>()
        } catch (e: Exception) {
            println("GET ERROR: ${e.message}")
            null
        }
    }
}