package api

import api.ApiClient

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.*

import models.ServerModel

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.engine.cio.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.plugins.contentnegotiation.*

object ServerApi {

    suspend fun getServers(token: String): List<ServerModel> {
        return try {
            val url = ApiClient.BASE_URL + "/api/servers"

            val raw = ApiClient.http.get(url) {
                headers.append("Authorization", "Bearer $token")
            }.body<String>()

            Json.decodeFromString(
                ListSerializer(ServerModel.serializer()),
                raw
            )
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun createServer(token: String, name: String): ServerModel? {
        return try {
            val url = ApiClient.BASE_URL + "/api/servers"

            val body = buildJsonObject {
                put("name", name)
            }.toString()

            val raw = ApiClient.http.post(url) {
                headers.append("Authorization", "Bearer $token")
                setBody(body)
            }.body<String>()

            Json.decodeFromString(ServerModel.serializer(), raw)
        } catch (_: Exception) {
            null
        }
    }
}
