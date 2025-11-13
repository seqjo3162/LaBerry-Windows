package ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import state.ServerState

@Composable
fun ServerListUI() {
    val servers = ServerState.servers
    val current = ServerState.currentServer

    Column(
        Modifier.width(72.dp).fillMaxHeight().padding(8.dp)
    ) {
        servers.forEach { server ->
            val selected = current?.id == server.id

            Box(
                Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.surface
                    )
                    .clickable { ServerState.selectServer(server) }
                    .padding(8.dp)
            ) {
                Text(
                    server.name.take(1).uppercase(),
                    color = MaterialTheme.colors.onSurface
                )
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}
