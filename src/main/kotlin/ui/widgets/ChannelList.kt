
package ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChannelList() {
    Column(
        modifier = Modifier.width(240.dp).fillMaxHeight().background(Color(0xFF2B2D31))
    ) {
        Text("Channels", color = Color.White, modifier = Modifier.padding(16.dp))
        repeat(10){
            Text("# channel-$it", color = Color(0xFFCFCFD3), modifier = Modifier.padding(12.dp))
        }
    }
}
