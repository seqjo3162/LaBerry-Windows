package ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UserPanelBottom() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color(0xFF232428))
            .padding(10.dp),
        horizontalArrangement = Arrangement.Start
    ) {

        Surface(
            color = Color(0xFF5865F2),
            shape = CircleShape,
            modifier = Modifier.size(40.dp)
        ) {}

        Spacer(Modifier.width(10.dp))

        Column {
            Text("Username", color = Color.White)
            Text("Online", color = Color(0xFF3BA55D))
        }
    }
}
