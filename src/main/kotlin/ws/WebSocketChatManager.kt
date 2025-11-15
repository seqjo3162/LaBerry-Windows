package ws

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import state.ChatState
import state.SessionState
import models.MessageModel
import kotlin.math.min

object WebSocketChatManager {

    private const val HOST = "195.46.162.142"
    private const val PORT = 5001
    private const val PATH = "/ws/chat"

    private var client: HttpClient? = null
    private var session: WebSocketSession? = null

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var reconnectJob: Job? = null
    private var pingJob: Job? = null

    private var chatId: Int? = null

    private var backoff = 1_000L
    private val backoffMax = 30_000L

    // -----------------------------------------------------------------------
    // START
    // -----------------------------------------------------------------------
    fun start(newChatId: Int) {
        chatId = newChatId

        reconnectJob?.cancel()
        reconnectJob = scope.launch {
            connectLoop()
        }
    }

    // -----------------------------------------------------------------------
    // STOP
    // -----------------------------------------------------------------------
    fun stop() {
        reconnectJob?.cancel()
        reconnectJob = null

        pingJob?.cancel()
        pingJob = null

        scope.launch {
            try { session?.close() } catch (_: Exception) {}
            session = null
        }
    }

    // -----------------------------------------------------------------------
    // CONNECT LOOP (exp backoff)
    // -----------------------------------------------------------------------
    private suspend fun connectLoop() {
        while (true) {
            val ok = tryConnect()
            if (ok) {
                backoff = 1_000
                listen()
            } else {
                delay(backoff)
                backoff = min(backoff * 2, backoffMax)
            }
        }
    }

    // -----------------------------------------------------------------------
    // SINGLE CONNECT ATTEMPT
    // -----------------------------------------------------------------------
    private suspend fun tryConnect(): Boolean {
        val token = SessionState.token ?: return false
        val cid = chatId ?: return false

        try {
            client = HttpClient(CIO) {
                install(WebSockets) {
                    pingInterval = 30_000 // <<<<<< FIX (Duration → Long)
                }
            }

            session = client!!.webSocketSession {
                url {
                    protocol = URLProtocol.WSS
                    host = HOST
                    port = PORT
                    encodedPath = "$PATH/$cid"
                    parameters.append("token", token)
                }
            }

            if (session?.isActive == true) {
                startPing()
                return true
            }

        } catch (_: Exception) {}

        return false
    }

    // -----------------------------------------------------------------------
    // MANUAL HEARTBEAT (PING)
    // -----------------------------------------------------------------------
    private fun startPing() {
        pingJob?.cancel()
        pingJob = scope.launch {
            val s = session ?: return@launch
            while (true) {
                try {
                    s.send(Frame.Ping(ByteArray(0)))
                } catch (_: Exception) {
                    return@launch
                }
                delay(20_000)
            }
        }
    }

    // -----------------------------------------------------------------------
    // LISTEN INCOMING
    // -----------------------------------------------------------------------
    private suspend fun listen() {
        val s = session ?: return
        try {
            for (frame in s.incoming) {
                frame as? Frame.Text ?: continue
                val raw = frame.readText()
                handleMessage(raw)
            }
        } catch (_: Exception) {
        } finally {
            session = null
        }
    }

    // -----------------------------------------------------------------------
    // PARSE INCOMING MESSAGE
    // -----------------------------------------------------------------------
    private fun handleMessage(raw: String) {
        val json = try {
            Json.parseToJsonElement(raw).jsonObject
        } catch (_: Exception) { return }

        // SYSTEM EVENT
        if (json["type"]?.jsonPrimitive?.content == "system") {
            val cid = json["chat_id"]?.jsonPrimitive?.int ?: return
            val text = json["text"]?.jsonPrimitive?.content ?: return
            val ts = json["timestamp"]?.jsonPrimitive?.content ?: ""
            ChatState.addSystemMessage(cid, text, ts)
            return
        }

        val cid = json["chat_id"]?.jsonPrimitive?.int ?: return
        val sender = json["sender_id"]?.jsonPrimitive?.int ?: return
        val username = json["sender_username"]?.jsonPrimitive?.content ?: return
        val content = json["content"]?.jsonPrimitive?.content ?: return
        val timestamp = json["timestamp"]?.jsonPrimitive?.content ?: return

        val msg = MessageModel(
            id = 0,
            chat_id = cid,
            sender_id = sender,
            sender_username = username,
            content = content,
            timestamp = timestamp
        )

        ChatState.addMessage(cid, msg)
    }

    // -----------------------------------------------------------------------
    // SEND MESSAGE (suspend)
    // -----------------------------------------------------------------------
    suspend fun send(text: String) {
        val s = session ?: return
        val safe = text.replace("\"", "'")

        val body = """{"content":"$safe"}"""

        try {
            s.send(Frame.Text(body))
        } catch (_: Exception) {}
    }

    // -----------------------------------------------------------------------
    // SEND TYPING EVENT
    // -----------------------------------------------------------------------
    suspend fun sendTyping() {
        val s = session ?: return
        val body = """{"type":"typing"}"""
        try {
            s.send(Frame.Text(body))
        } catch (_: Exception) {}
    }
}
