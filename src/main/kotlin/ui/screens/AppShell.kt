
package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ui.widgets.ChannelList
import ui.widgets.ChatArea
import ui.widgets.ServerList

@Composable
fun AppShell() {
    Row(Modifier.fillMaxSize().background(Color(0xFF1E1F22))) {
        Box(Modifier.width(80.dp).fillMaxHeight().background(Color(0xFF202225)))
        Box(Modifier.width(240.dp).fillMaxHeight().background(Color(0xFF2B2D31)))
        Box(Modifier.fillMaxSize().background(Color(0xFF313338)))
        ServerList(onFriendsClick = { showFriends = true })

        Row(Modifier.fillMaxSize()) {
            ChatListUI()
            Box(Modifier.weight(1f)) {
                ChatArea()
            }
        }

        if (showFriends)
            FriendsList()
        else
            ChannelList()

        Box(Modifier.fillMaxSize()) {
            ChatArea()
        }
        ChannelList()
        ChatArea()
    }
}
