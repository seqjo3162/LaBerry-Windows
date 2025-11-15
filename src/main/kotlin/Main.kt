import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import ui.AppRoot
import ui.screens.MainScreen
import ui.splash.SplashScreen

private val LaBerryDarkColors = darkColorScheme(
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

    var showSplash by remember { mutableStateOf(true) }
    var showMain by remember { mutableStateOf(false) }

    // --- SPLASH WINDOW ---
    if (showSplash) {
        Window(
            onCloseRequest = {},
            title = "",
            undecorated = true,
            alwaysOnTop = true,
            resizable = false,
            state = rememberWindowState(
                width = 450.dp,
                height = 350.dp,
                position = WindowPosition.Aligned(Alignment.Center)
            )
        ) {
            SplashScreen {
                showSplash = false
                showMain = true
            }
        }
    }

    // --- MAIN WINDOW ---
    if (showMain) {
        Window(
            onCloseRequest = ::exitApplication,
            title = "LaBerry",
            state = rememberWindowState(
                width = 1280.dp,
                height = 800.dp,
                position = WindowPosition.Aligned(Alignment.Center)
            )
        ) {
            MaterialTheme(colorScheme = LaBerryDarkColors) {
                AppRoot(
                    mainScreen = { MainScreen() }
                )
            }
        }
    }
}
