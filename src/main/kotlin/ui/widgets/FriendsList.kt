package ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FriendsList() {
    Column(
        Modifier
            .fillMaxHeight()
            .width(240.dp)
            .background(Color(0xFF2B2D31))
    ) {
        Text("Friends", color = Color.White, modifier = Modifier.padding(16.dp))

        // Mock data — заменим на реальное API на Step 5
        val friends = listOf(
            "jenya_online",
            "fox_offline",
            "nebula_online",
            "kira_offline"
        )

        friends.forEach {
            Text(
                text = it,
                color = if (it.contains("online")) Color(0xFF57F287) else Color(0xFFED4245),
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}
