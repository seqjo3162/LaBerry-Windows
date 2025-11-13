package ws

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import state.SessionState

/**
 * WebSocket для live сообщений, статусов и т.п.
 * Если сервер не реализует часть событий — код не падает.
 */
object WebSocketManager {

    private const val WS_URL = "wss://laberry.loca.lt/ws"

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var session: WebSocketSession? = null
    private var reconnectJob: Job? = null

    suspend fun connect(chatId: Int) {
        disconnect()
        val token = SessionState.token ?: return
        val client = HttpClient(CIO) { install(WebSockets) }
        session = client.webSocketSession {
            url {
                protocol = URLProtocol.WSS
                host = "laberry.loca.lt"
                encodedPath = "/ws/chat/$chatId"
                parameters.append("token", token)
            }
        }

        scope.launch { listen() }
    }

    private suspend fun listen() {
        val s = session ?: return
        for (frame in s.incoming) {
            if (frame is Frame.Text) {
                val raw = frame.readText()
                try {
                    val json = Json.parseToJsonElement(raw).jsonObject
                    val msg = MessageModel(
                        id = json["id"]!!.jsonPrimitive.int,
                        chat_id = json["chat_id"]!!.jsonPrimitive.int,
                        sender_id = json["sender_id"]!!.jsonPrimitive.int,
                        sender_username = json["sender_username"]!!.jsonPrimitive.content,
                        content = json["content"]!!.jsonPrimitive.content,
                        timestamp = json["timestamp"]!!.jsonPrimitive.content
                    )
                    ChatState.addMessage(msg)
                } catch(e: Exception) {}
            }
        }
    }

    suspend fun send(text: String) {
        val s = session ?: return
        val body = "{\"content\":\"" + text.replace("\"","'" ) + "\"}"
        s.send(Frame.Text(body))
    }

    fun disconnect() {
        try { session?.close() } catch(e: Exception) {}
        session = null
    }
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    fun start() {
        if (reconnectJob != null) return
        reconnectJob = scope.launch { loop() }
    }

    fun stop() {
        reconnectJob?.cancel()
        reconnectJob = null
        session?.close()
        session = null
    }

    private suspend fun loop() {
        while (true) {
            try {
                connect()
                listen()
            } catch (_: Exception) {
            }

            delay(5000)
        }
    }

    private suspend fun connect() {
        val token = SessionState.token ?: return

        val client = HttpClient(CIO) {
            install(WebSockets)
        }

        session = client.webSocketSession {
            url("$WS_URL?token=$token")
        }
    }

    @Serializable
    private data class WSBase(val type: String? = null)

    private suspend fun handle(msg: String) {
        val base = try { json.decodeFromString<WSBase>(msg) } catch (_: Exception) { return }

        when (base.type) {
            "user_status" -> handleUserStatus(msg)
            "message" -> handleMessage(msg)
            "typing" -> {}
        }
    }

    private suspend fun handleUserStatus(msg: String) {
        // TODO Step 5: integrate into FriendsState
    }

    private suspend fun handleMessage(msg: String) {
        // TODO Step 5: integrate into ChatState
    }

    suspend fun sendMessage(channelId: Int, text: String) {
        val s = session ?: return
        val body = json.encodeToString(
            mapOf(
                "type" to "message",
                "channel_id" to channelId,
                "text" to text
            )
        )
        try { s.send(Frame.Text(body)) } catch (_: Exception) {}
    }

    suspend fun sendPresence(status: String) {
        val s = session ?: return
        val body = json.encodeToString(
            mapOf(
                "type" to "presence",
                "status" to status
            )
        )
        try { s.send(Frame.Text(body)) } catch (_: Exception) {}
    }
}
