package ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UserPanel() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color(0xFF232428))
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFF5865F2))
        )

        Spacer(Modifier.width(12.dp))

        Column {
            Text("CurrentUser", color = Color.White)
            Text("Online", color = Color(0xFF3BA55D))
        }
    }
}
