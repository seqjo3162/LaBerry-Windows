
package ui.theme

import androidx.compose.material.*
import androidx.compose.runtime.*

private val DarkColors = darkColors(
    primary = androidx.compose.ui.graphics.Color(0xFF5865F2),
    background = androidx.compose.ui.graphics.Color(0xFF2B2D31),
    surface = androidx.compose.ui.graphics.Color(0xFF313338),
)

@Composable
fun LaBerryTheme(content: @Composable () -> Unit) {
    MaterialTheme(colors = DarkColors, content = content)
}
