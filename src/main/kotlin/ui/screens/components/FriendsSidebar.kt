package ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FriendsSidebar() {
    Column(
        modifier = Modifier
            .width(220.dp)
            .fillMaxHeight()
            .background(Color(0xFF2B2D31))
    ) {
        Text(
            "Friends",
            color = Color.White,
            modifier = Modifier.padding(12.dp)
        )

        Spacer(Modifier.height(12.dp))

        repeat(4) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .height(36.dp)
                    .fillMaxWidth()
                    .background(Color(0xFF3A3C42))
            )
        }
    }
}
