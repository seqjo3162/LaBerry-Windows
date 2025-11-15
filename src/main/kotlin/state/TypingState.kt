package state

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*
import kotlin.system.*

object TypingState {

    // chatId -> map of userId -> expirationTimeMillis
    private val typing = mutableStateMapOf<Int, MutableMap<Int, Long>>()

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    // interval to auto-clean expired typing events
    init {
        scope.launch {
            while (true) {
                val now = System.currentTimeMillis()

                typing.forEach { (chatId, map) ->
                    val expired = map.filter { it.value < now }.keys
                    expired.forEach { map.remove(it) }
                }

                delay(600) // cleanup every ~0.6s
            }
        }
    }

    // add typing event from WS
    fun onTyping(chatId: Int, userId: Int) {
        val map = typing.getOrPut(chatId) { mutableMapOf() }
        val expire = System.currentTimeMillis() + 5000 // 5 seconds timeout
        map[userId] = expire
    }

    // read typing users for a chat
    fun getTypingUsers(chatId: Int): List<Int> {
        return typing[chatId]?.keys?.toList() ?: emptyList()
    }
}
