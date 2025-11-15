package state

import androidx.compose.runtime.*
import api.ApiClient
import models.User
import models.LoginResponse // ← ВАЖНЫЙ ИМПОРТ!
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import java.security.KeyStore
import java.util.Base64

object SessionState {

    // -----------------------------
    // PUBLIC OBSERVABLE STATE
    // -----------------------------
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

    var rememberMe by mutableStateOf(false)

    // -----------------------------
    // STORAGE
    // -----------------------------
    private val file = File("session.enc")
    private const val KEY_ALIAS = "laberry_aes_key"

    private fun getAESKey(): SecretKey {
        val ksFile = File("laberry_keystore.jks")
        val password = "laberry".toCharArray()

        val ks = KeyStore.getInstance("JCEKS")

        if (ksFile.exists()) {
            ks.load(ksFile.inputStream(), password)
        } else {
            ks.load(null, password)
        }

        // если ключа нет — создаём и записываем
        if (!ks.containsAlias(KEY_ALIAS)) {
            val gen = KeyGenerator.getInstance("AES")
            gen.init(256)
            val key = gen.generateKey()

            ks.setEntry(
                KEY_ALIAS,
                KeyStore.SecretKeyEntry(key),
                KeyStore.PasswordProtection(password)
            )

            ks.store(ksFile.outputStream(), password)
            return key
        }

        // если есть — читаем
        val entry = ks.getEntry(
            KEY_ALIAS,
            KeyStore.PasswordProtection(password)
        ) as KeyStore.SecretKeyEntry

        return entry.secretKey
    }

    private fun encrypt(text: String): String {
        val key = getAESKey()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key)

        val iv = cipher.iv
        val encrypted = cipher.doFinal(text.toByteArray())
        val encoder = Base64.getEncoder()

        return encoder.encodeToString(iv + encrypted)
    }

    private fun decrypt(data: String): String? {
        return try {
            val raw = Base64.getDecoder().decode(data)
            val iv = raw.copyOfRange(0, 16)
            val content = raw.copyOfRange(16, raw.size)

            val key = getAESKey()
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))

            String(cipher.doFinal(content))
        } catch (_: Exception) {
            null
        }
    }

    // -----------------------------
    // LOGIN
    // -----------------------------
    suspend fun login(username: String, password: String): Boolean {
        isLoading = true
        error = null

        return try {
            val resp: LoginResponse? = ApiClient.login(username, password)

            if (resp == null) {
                error = "Ошибка сервера"
                false
            }
            else if (resp.requires_2fa == true) {
                pending2FAUserId = resp.user_id
                error = "Введите код 2FA"
                false
            }
            else if (resp.access_token == null) {
                error = "Неверный логин или пароль"
                false
            }
            else {
                token = resp.access_token

                if (rememberMe)
                    saveEncryptedSession()

                pending2FAUserId = null
                loadMe()
                true
            }

        } catch (e: Exception) {
            error = "Ошибка подключения"
            false
        } finally {
            isLoading = false
        }
    }

    // -----------------------------
    // REGISTER
    // -----------------------------
    suspend fun register(username: String, password: String, email: String? = null): Boolean {
        isLoading = true
        error = null

        return try {
            val resp: LoginResponse? = ApiClient.register(username, password, email)

            if (resp == null) {
                error = "Ошибка сервера"
                false
            }
            else if (resp.access_token == null) {
                error = "Не удалось создать аккаунт"
                false
            }
            else {
                token = resp.access_token

                if (rememberMe)
                    saveEncryptedSession()

                loadMe()
                true
            }

        } catch (e: Exception) {
            error = "Ошибка подключения"
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
            val resp: LoginResponse? = ApiClient.verify2FA(userId, code)

            if (resp?.access_token != null) {
                token = resp.access_token

                if (rememberMe)
                    saveEncryptedSession()

                pending2FAUserId = null
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
    // LOAD PROFILE
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
    // AUTO LOGIN
    // -----------------------------
    suspend fun tryAutoLogin(): Boolean {
        if (!file.exists()) return false

        val decrypted = decrypt(file.readText()) ?: return false
        token = decrypted

        val me = ApiClient.getMe(token!!)
        return if (me != null) {
            loadMe()
            true
        } else {
            token = null
            false
        }
    }

    // -----------------------------
    // LOGOUT
    // -----------------------------
    fun logout() {
        token = null
        currentUser = null
        error = null
        pending2FAUserId = null

        if (file.exists())
            file.delete()
    }

    private suspend fun saveEncryptedSession() {
        withContext(Dispatchers.IO) {
            try {
                val t = token ?: return@withContext

                // шифруем токен
                val encrypted = encrypt(t)

                // сохраняем в session.enc
                file.writeText(encrypted)

            } catch (e: Exception) {
                println("SAVE SESSION ERROR: ${e.message}")
            }
        }
    }

}
