
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "LaBerry-Windows") {
        MaterialTheme { Text("LaBerry-Windows placeholder") }
    }
}
