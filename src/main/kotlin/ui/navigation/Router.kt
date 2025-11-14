package ui.navigation

import androidx.compose.runtime.*
import ui.screens.AuthScreen
import ui.screens.AppShell

enum class Screen { AUTH, APP }

@Composable
fun Router() {
    var current by remember { mutableStateOf(Screen.AUTH) }

    when (current) {
        Screen.AUTH -> AuthScreen(
            onSuccess = { current = Screen.APP }
        )

        Screen.APP -> AppShell()
    }
}
