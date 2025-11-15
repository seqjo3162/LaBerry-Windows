package ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TopFriendsBar() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(0xFF2B2D31))
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TopTabButton("В сети")
        TopTabButton("Все")
        TopTabButton("Добавить в друзья")
    }
}

@Composable
fun TopTabButton(text: String) {
    Text(
        text,
        color = Color(0xFFB9BBBE),
        modifier = Modifier.padding(vertical = 14.dp)
    )
}
