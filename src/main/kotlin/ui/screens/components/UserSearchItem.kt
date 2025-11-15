package ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp   // ← добавили
import models.User

@Composable
fun UserSearchItem(
    user: User,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .background(Color(0xFF2B2D31), RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFF5865F2), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = (user.username.firstOrNull()?.uppercase() ?: "?"),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.width(12.dp))

        Column {
            Text(user.username, color = Color.White, fontWeight = FontWeight.SemiBold)
            Text("#${user.id}", color = Color(0xFFB9BBBE), fontSize = 12.sp)
        }
    }
}
