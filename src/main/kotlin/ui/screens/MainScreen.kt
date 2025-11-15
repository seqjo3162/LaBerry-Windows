package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.screens.components.*
import ui.screens.friends.*

@Composable
fun MainScreen() {

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF313338))
    ) {

        // 1) SERVER LIST (левый столбец)
        ServerList()

        // 2) FRIENDS SIDEBAR (второй столбец)
        FriendsSidebar()

        // 3) MAIN CONTENT (центр)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color(0xFF313338))
        ) {
            var selectedTab by remember { mutableStateOf(1) }

            TopTabs(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            // 👇 вот это самое важное — router:
            when (selectedTab) {
                0 -> FriendsOnlineScreen()   // Online
                1 -> FriendsAllScreen()      // All friends
                2 -> AddFriendsScreen()      // Add Friends
            }
        }

        // 4) RIGHT SIDE (пока отключено)
        // RightActivesPanel()
    }
}
