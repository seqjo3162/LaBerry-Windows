package ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ServerList() {
    Column(
        modifier = Modifier
            .width(80.dp)
            .fillMaxHeight()
            .background(Color(0xFF1E1F22)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(12.dp))

        // Main server circle
        // Main server circle + white stripe
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .background(Color.White)
            )
            Spacer(Modifier.width(8.dp))
            Surface(
                modifier = Modifier.size(54.dp),
                color = Color(0xFF5865F2),
                shape = CircleShape
            ) {}
        }

        Spacer(Modifier.height(20.dp))

        // Add server
        Surface(
            modifier = Modifier.size(44.dp),
            color = Color(0xFF2B2D31),
            shape = CircleShape
        ) {}

        Spacer(Modifier.height(20.dp))

        // 4 example server buttons
        repeat(4) {
            Surface(
                modifier = Modifier
                    .size(44.dp),
                color = Color(0xFF2B2D31),
                shape = CircleShape
            ) {}
            Spacer(Modifier.height(16.dp))
        }

        Spacer(Modifier.weight(1f))

        // bottom user panel area
        UserPanel()
    }
}
