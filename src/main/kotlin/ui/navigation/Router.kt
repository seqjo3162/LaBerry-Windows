package ui.navigation

import androidx.compose.runtime.*
import ui.screens.LoginScreen
import ui.screens.RegisterScreen
import ui.screens.AppShell

enum class Screen { LOGIN, REGISTER, APP }

@Composable
fun Router() {
    var current by remember { mutableStateOf(Screen.LOGIN) }

    when (current) {
        Screen.LOGIN -> LoginScreen(
            onLoginSuccess = { current = Screen.APP },
            onRegister = { current = Screen.REGISTER }
        )

        Screen.REGISTER -> RegisterScreen(
            onRegisterSuccess = { current = Screen.APP },
            goLogin = { current = Screen.LOGIN }
        )

        Screen.APP -> AppShell()
    }
}
