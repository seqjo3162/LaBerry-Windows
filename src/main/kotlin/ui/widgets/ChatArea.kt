
package ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import state.ChatState
import ws.WebSocketManager

@Composable
fun ChatArea() {
    val scope = rememberCoroutineScope()
    val msgs = ChatState.messages
    val chat = ChatState.currentChat
    var text by remember { mutableStateOf("") }
    val scroll = rememberScrollState()

    Column(Modifier.fillMaxSize().padding(8.dp)) {

        Text(chat?.name ?: "Chat", color = MaterialTheme.colors.onSurface)

        Spacer(Modifier.height(8.dp))

        Column(
            Modifier.weight(1f).verticalScroll(scroll)
        ) {
            msgs.forEach {
                Text("${it.sender_username}: ${it.content}", color = MaterialTheme.colors.onSurface)
                Spacer(Modifier.height(4.dp))
            }

            LaunchedEffect(msgs.size) {
                scroll.animateScrollTo(scroll.maxValue)
            }
        }

        Row {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    scope.launch { WebSocketManager.send(text) }
                    text = ""
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Send")
            }
        }
    }
}
