package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.widgets.ServerListUI
import ui.widgets.ChannelList
import ui.widgets.ChatArea
import ui.widgets.FriendsList
import ui.widgets.ProfileBar

@Composable
fun AppShell() {
    var showFriends by remember { mutableStateOf(false) }

    Surface(color = MaterialTheme.colors.background) {
        Row(Modifier.fillMaxSize()) {
            when (UiState.activeSection.value) {
                UiState.Section.FRIENDS -> FriendsScreen()
                UiState.Section.SERVERS -> {
                    Column(Modifier.weight(1f)) {
                        ProfileBar()
                        Row(Modifier.weight(1f)) {
                            ChannelList()
                            ChatArea()
                        }
                    }
                }
            }
            // Левая колонка — список «серверов»/DM
            ServerListUI(
                onHomeClick = { showFriends = false },
                onFriendsClick = { showFriends = true },
            )

            if (showFriends) {
                // Режим «Friends» как в Discord
                Column(
                    Modifier
                        .width(260.dp)
                        .fillMaxHeight()
                        .background(Color(0xFF2B2D31))
                ) {
                    FriendsList()
                    Spacer(Modifier.weight(1f))
                    ProfileBar()
                }

                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFF313338))
                ) {
                    // Тут позже сделаем детальный экран друга / список запросов
                }
            } else {
                // Обычный серверный режим: каналы + чат
                Column(
                    Modifier
                        .width(260.dp)
                        .fillMaxHeight()
                        .background(Color(0xFF2B2D31))
                ) {
                    ChannelList()
                    Spacer(Modifier.weight(1f))
                    ProfileBar()
                }

                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFF313338))
                ) {
                    ChatArea()
                }
            }
        }
    }
}
