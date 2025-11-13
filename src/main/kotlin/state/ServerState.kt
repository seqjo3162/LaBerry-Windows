package state

import androidx.compose.runtime.*
import api.ServerApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import models.ServerModel

object ServerState {

    var servers by mutableStateOf<List<ServerModel>>(emptyList())
    var currentServer by mutableStateOf<ServerModel?>(null)

    suspend fun loadServers() {
        val token = SessionState.token ?: return
        servers = withContext(Dispatchers.IO) {
            ServerApi.getServers(token)
        }
    }

    fun selectServer(server: ServerModel) {
        currentServer = server
    }
}
