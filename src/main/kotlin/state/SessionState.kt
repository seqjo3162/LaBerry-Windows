package state

import androidx.compose.runtime.*
import api.ApiClient
import models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object SessionState {

    var token by mutableStateOf<String?>(null)
        private set

    var currentUser by mutableStateOf<User?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var pending2FAUserId by mutableStateOf<Int?>(null)
        private set

    private val sessionFile = File("session.json")

    // -----------------------------
    // MAIN LOGIN (with 2FA support)
    // -----------------------------
    suspend fun login(username: String, password: String): Boolean {
        isLoading = true
        error = null

        return try {
            val resp = ApiClient.login(username, password)

            if (resp == null) {
                error = "Ошибка сервера"
                false
            }
            else if (resp.requires_2fa == true) {
                pending2FAUserId = resp.user_id
                error = "Введите код 2FA"
                false // ещё нельзя заходить
            }
            else if (resp.access_token == null) {
                error = "Неверный логин или пароль"
                false
            }
            else {
                token = resp.access_token
                saveSession()
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

    // -----------------------------
    // VERIFY 2FA
    // -----------------------------
    suspend fun verify2FA(code: String): Boolean {
        val userId = pending2FAUserId ?: return false

        isLoading = true
        error = null

        return try {
            val resp = ApiClient.verify2FA(userId, code)

            if (resp?.access_token != null) {
                token = resp.access_token
                pending2FAUserId = null
                saveSession()
                loadMe()
                true
            } else {
                error = "Неверный код"
                false
            }

        } catch (e: Exception) {
            error = "Ошибка сервера"
            false
        } finally {
            isLoading = false
        }
    }

    // -----------------------------
    // LOAD "ME"
    // -----------------------------
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

    // -----------------------------
    // AUTO-LOGIN
    // -----------------------------
    suspend fun tryAutoLogin(): Boolean {
        if (!sessionFile.exists()) return false

        val saved = sessionFile.readText()
        if (saved.isBlank()) return false

        token = saved.trim()

        val me = ApiClient.getMe(token!!)
        return if (me != null) {
            loadMe()
            true
        } else {
            token = null
            false
        }
    }

    private suspend fun saveSession() = withContext(Dispatchers.IO) {
        if (token != null) {
            sessionFile.writeText(token!!)
        }
    }

    fun logout() {
        token = null
        currentUser = null
        error = null
        pending2FAUserId = null
        if (sessionFile.exists()) sessionFile.delete()
    }
}
