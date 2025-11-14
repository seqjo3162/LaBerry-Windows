package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import state.UiState
import ui.widgets.*

@Composable
fun AppShell() {
    val section = UiState.activeSection.value

    Surface(color = MaterialTheme.colors.background) {
        Row(Modifier.fillMaxSize()) {

            ServerListUI(
                onAddServerClick = {
                    // TODO: открыть окно "создать / присоединиться по коду"
                }
            )

            when (section) {
                UiState.Section.FRIENDS -> {
                    // Левый столбец друзей
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

                    // Правая часть – детали / заглушка
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(Color(0xFF313338))
                    ) {
                        FriendsScreen() // или заглушка
                    }
                }

                UiState.Section.SERVERS -> {
                    // Столбец каналов
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

                    // Чат
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
}
