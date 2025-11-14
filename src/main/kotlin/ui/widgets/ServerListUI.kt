package ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import state.ServerState
import state.UiState

@Composable
fun ServerListUI(
    onAddServerClick: () -> Unit = {}
) {
    val servers = ServerState.servers
    val currentId = ServerState.currentServerId.value
    val section = UiState.activeSection.value

    Column(
        modifier = Modifier
            .width(80.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colors.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(8.dp))

        // Большой верхний круг – «Дом / Друзья»
        HomeButton(
            selected = section == UiState.Section.FRIENDS,
            onClick = { UiState.activeSection.value = UiState.Section.FRIENDS }
        )

        Spacer(Modifier.height(12.dp))

        if (servers.isEmpty()) {
            // Нет серверов – только плюс
            AddServerCircle(onClick = onAddServerClick)
        } else {
            servers.forEachIndexed { index, server ->
                ServerCircle(
                    label = "#${index + 1}",
                    selected = section == UiState.Section.SERVERS &&
                            currentId == server.id,
                    onClick = { ServerState.selectServer(server.id) }
                )
            }

            Spacer(Modifier.height(12.dp))
            AddServerCircle(onClick = onAddServerClick)
        }
    }
}

@Composable
private fun HomeButton(
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selected) {
            Box(
                Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                    .background(MaterialTheme.colors.onSurface)
            )
        } else {
            Spacer(Modifier.width(4.dp))
        }

        Box(
            modifier = Modifier
                .padding(start = 6.dp)
                .size(46.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Text("LB", color = MaterialTheme.colors.onPrimary)
        }
    }
}

@Composable
private fun ServerCircle(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selected) {
            Box(
                Modifier
                    .width(4.dp)
                    .height(36.dp)
                    .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                    .background(MaterialTheme.colors.onSurface)
            )
        } else {
            Spacer(Modifier.width(4.dp))
        }

        Box(
            modifier = Modifier
                .padding(start = 6.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Text(label, color = MaterialTheme.colors.onPrimary)
        }
    }
}

@Composable
private fun AddServerCircle(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(4.dp))

        Box(
            modifier = Modifier
                .padding(start = 6.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.onSurface.copy(alpha = 0.6f))
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Text("+", color = MaterialTheme.colors.onSurface)
        }
    }
}
