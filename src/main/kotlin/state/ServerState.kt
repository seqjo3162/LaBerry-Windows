package state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import models.ServerModel

object ServerState {
    val servers = mutableStateListOf<ServerModel>()
    val currentServerId = mutableStateOf<Int?>(null)

    val currentServer
        get() = servers.firstOrNull { it.id == currentServerId.value }

    fun setServers(list: List<ServerModel>) {
        servers.clear()
        servers.addAll(list)
        currentServerId.value = list.firstOrNull()?.id
        if (list.isEmpty()) {
            UiState.activeSection.value = UiState.Section.FRIENDS
        }
    }

    fun selectServer(id: Int) {
        currentServerId.value = id
        UiState.activeSection.value = UiState.Section.SERVERS
    }
}
