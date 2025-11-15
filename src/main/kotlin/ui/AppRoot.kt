package ui

import androidx.compose.runtime.*
import ui.screens.AutoLoginScreen
import ui.screens.AuthScreen
import ws.WebSocketChatManager
import ws.WebSocketPresenceManager
import state.SessionState

@Composable
fun AppRoot(
    mainScreen: @Composable () -> Unit
) {
    var screen by remember { mutableStateOf("auto") }

    // -----------------------------
    // SCREEN SWITCHER
    // -----------------------------
    when (screen) {

        // -----------------------------
        // AUTO LOGIN
        // -----------------------------
        "auto" -> AutoLoginScreen(
            onSuccess = {
                // after auto login success → load sockets
                WebSocketPresenceManager.start()
                screen = "main"
            },
            onFail = { screen = "auth" }
        )

        // -----------------------------
        // AUTH SCREEN (Login/Register)
        // -----------------------------
        "auth" -> AuthScreen(
            onSuccess = {
                // login success → start sockets
                WebSocketPresenceManager.start()
                screen = "main"
            }
        )

        // -----------------------------
        // MAIN SCREEN (your app)
        // -----------------------------
        "main" -> {
            // if user logs out while already here
            LaunchedEffect(SessionState.token) {
                if (SessionState.token == null) {
                    WebSocketChatManager.stop()
                    WebSocketPresenceManager.stop()
                    screen = "auth"
                }
            }

            mainScreen()
        }
    }
}
