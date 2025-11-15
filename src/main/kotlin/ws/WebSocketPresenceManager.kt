package ws

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import state.FriendsState
import state.SessionState
import kotlin.math.min

object WebSocketPresenceManager {

    private const val HOST = "195.46.162.142"
    private const val PORT = 5001
    private const val PATH = "/ws/presence"

    private var client: HttpClient? = null
    private var session: WebSocketSession? = null

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var reconnectJob: Job? = null
    private var pingJob: Job? = null

    private var backoff = 1_000L
    private val backoffMax = 30_000L

    // ---------------------------------------------------------------------
    // START
    // ---------------------------------------------------------------------
    fun start() {
        if (reconnectJob != null) return
        reconnectJob = scope.launch { connectLoop() }
    }

    // ---------------------------------------------------------------------
    // STOP
    // ---------------------------------------------------------------------
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

    // ---------------------------------------------------------------------
    // MAIN CONNECT LOOP (exp backoff)
    // ---------------------------------------------------------------------
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

    // ---------------------------------------------------------------------
    // SINGLE ATTEMPT
    // ---------------------------------------------------------------------
    private suspend fun tryConnect(): Boolean {
        val token = SessionState.token ?: return false

        try {
            client = HttpClient(CIO) {
                install(WebSockets) {
                    // FIX: Compose Desktop requires Long, not Duration
                    pingInterval = 30_000
                }
            }

            session = client!!.webSocketSession {
                url {
                    protocol = URLProtocol.WSS
                    host = HOST
                    port = PORT
                    encodedPath = PATH
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

    // ---------------------------------------------------------------------
    // PING LOOP
    // ---------------------------------------------------------------------
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

    // ---------------------------------------------------------------------
    // LISTEN
    // ---------------------------------------------------------------------
    private suspend fun listen() {
        val s = session ?: return
        try {
            for (frame in s.incoming) {
                frame as? Frame.Text ?: continue
                val text = frame.readText()
                handleEvent(text)
            }
        } catch (_: Exception) {
        } finally {
            session = null
        }
    }

    // ---------------------------------------------------------------------
    // PARSE EVENTS
    // ---------------------------------------------------------------------
    private fun handleEvent(text: String) {
        val json = try {
            Json.parseToJsonElement(text).jsonObject
        } catch (_: Exception) {
            return
        }

        when (json["event"]?.jsonPrimitive?.content) {

            "user_online" -> {
                val id = json["user_id"]?.jsonPrimitive?.int ?: return
                FriendsState.setOnline(id, true)
            }

            "user_offline" -> {
                val id = json["user_id"]?.jsonPrimitive?.int ?: return
                FriendsState.setOnline(id, false)
            }
        }
    }
}
