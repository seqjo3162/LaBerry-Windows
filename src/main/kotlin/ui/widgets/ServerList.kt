
package ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ServerList() {
    Column(
        modifier = Modifier.width(80.dp).fillMaxHeight().background(Color(0xFF202225)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(12.dp))
        repeat(6) {
            Box(
                Modifier
                    .size(48.dp)
                    .padding(8.dp)
                    .background(Color(0xFF5865F2), CircleShape)
            )
        }

        Box(
            Modifier
                .size(48.dp)
                .padding(8.dp)
                .background(Color(0xFF57F287), CircleShape)
                .clickable { onFriendsClick() }
        ) {}

        Spacer(Modifier.height(8.dp))

        repeat(5) {
            Box(
                Modifier
                    .size(48.dp)
                    .padding(8.dp)
                    .background(Color(0xFF5865F2), CircleShape)
            )
        }
    }
}
