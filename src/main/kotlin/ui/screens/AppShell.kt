package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import ui.widgets.ServerListUI
import ui.widgets.ChannelList
import ui.widgets.ChatArea

@Composable
fun AppShell() {
    Surface(color = MaterialTheme.colors.background) {

        Row(Modifier.fillMaxSize()) {

            // Discord LEFT SERVER BAR
            ServerListUI()

            // Discord MIDDLE CHANNEL LIST
            ChannelList()

            // MAIN CHAT
            Box(Modifier.weight(1f)) {
                ChatArea()
            }
        }
    }
}
