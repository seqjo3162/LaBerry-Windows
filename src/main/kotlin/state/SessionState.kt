
package state

import androidx.compose.runtime.*
import api.ApiClient
import models.User

/**
 * Глобальное состояние сессии:
 * токен, текущий пользователь, флаги загрузки/ошибок.
 */
object SessionState {

    var token by mutableStateOf<String?>(null)
        private set

    var currentUser by mutableStateOf<User?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    suspend fun login(username: String, password: String): Boolean {
        isLoading = true
        error = null
        return try {
            val t = ApiClient.login(username, password)
            if (t.isNullOrBlank()) {
                error = "Неверный логин или пароль (или сервер недоступен)"
                false
            } else {
                token = t
                loadMe()
                true
            }
        } catch (e: Exception) {
            error = "Ошибка подключения к серверу"
            false
        } finally {
            isLoading = false
        }
    }

    private suspend fun loadMe() {
        val t = token ?: return
        val me = ApiClient.getMe(t)
        if (me != null && me.id != null && me.username != null) {
            currentUser = User(
                id = me.id,
                username = me.username,
                displayName = me.display_name,
                avatarUrl = me.avatar_url,
                status = me.status
            )
        }
    }

    fun logout() {
        token = null
        currentUser = null
        error = null
    }
}
