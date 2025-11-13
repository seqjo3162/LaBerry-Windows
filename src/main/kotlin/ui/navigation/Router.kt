
package ui.navigation

import androidx.compose.runtime.*
import ui.screens.AppShell
import ui.screens.LoginScreen

enum class Screen { LOGIN, APP }

@Composable
fun Router() {
    var current by remember { mutableStateOf(Screen.LOGIN) }

    when (current) {
        Screen.LOGIN -> LoginScreen(onLoginSuccess = { current = Screen.APP })
        Screen.APP -> AppShell()
    }
}
