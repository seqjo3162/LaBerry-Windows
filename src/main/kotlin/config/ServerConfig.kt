package config

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking

object ServerConfig {

    private const val LOCAL = "http://127.0.0.1:5001"
    private const val REMOTE = "http://195.46.162.142:5001"

    private val client = HttpClient(CIO)

    val BASE_URL: String by lazy {
        if (isLocalAvailable()) LOCAL else REMOTE
    }

    private fun isLocalAvailable(): Boolean {
        return try {
            runBlocking {
                val r = client.get("$LOCAL/health")
                r.status.value in 200..299
            }
        } catch (_: Exception) {
            false
        }
    }
}
