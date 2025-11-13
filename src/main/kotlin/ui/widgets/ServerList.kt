package ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ServerListUI(
    onHomeClick: () -> Unit = {},
    onFriendsClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .fillMaxHeight()
            .background(Color(0xFF202225)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(12.dp))

        // «Дом» / главная (можно потом подвязать на список серверов/DM)
        Box(
            Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF5865F2))
                .clickable { onHomeClick() }
        )

        Spacer(Modifier.height(16.dp))

        // Кнопка «Friends» (зелёный кружок)
        Box(
            Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF57F287))
                .clickable { onFriendsClick() }
        )

        Spacer(Modifier.height(16.dp))

        // Заглушки серверов (потом заменим данными из ServerState)
        repeat(6) {
            Box(
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF313338))
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}
