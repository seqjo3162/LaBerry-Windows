import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import state.SessionState
import ui.AppShell
import ui.screens.LoginScreen

private val LaBerryDarkColors = darkColors(
    primary = Color(0xFF5865F2),
    secondary = Color(0xFF57F287),
    background = Color(0xFF313338),
    surface = Color(0xFF2B2D31),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFE3E5E8),
    onSurface = Color(0xFFE3E5E8),
)

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "LaBerry-Windows") {
        MaterialTheme(colors = LaBerryDarkColors) {
            var authPassed by remember { mutableStateOf(false) }

            if (SessionState.token == null && !authPassed) {
                LoginScreen(onLoginSuccess = { authPassed = true })
            } else {
                AppShell()
            }
        }
    }
}
