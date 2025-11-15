package ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RightActivesPanel() {
    Column(
        modifier = Modifier
            .width(260.dp)
            .fillMaxHeight()
            .background(Color(0xFF2B2D31))
            .padding(12.dp)
    ) {
        Text("Friends Actives", color = Color.White)

        Spacer(Modifier.height(16.dp))

        repeat(5) { idx ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                color = Color(0xFF3A3C42),
                tonalElevation = 2.dp
            ) {
                Text(
                    text = "User $idx play something",
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}
