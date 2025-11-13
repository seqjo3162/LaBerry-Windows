package ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import state.SessionState

@Composable
fun ProfileBar() {
    val user = SessionState.currentUser

    Row(
        Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Color(0xFF232428))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row {
            Box(
                Modifier
                    .size(40.dp)
                    .background(Color(0xFF5865F2), CircleShape)
            )

            Column(Modifier.padding(start = 8.dp)) {
                Text(user?.username ?: "Unknown", color = Color.White)
                Text(user?.status ?: "offline", color = Color(0xFF898A8C), fontSize = MaterialTheme.typography.caption.fontSize)
            }
        }

        Button(
            onClick = { SessionState.logout() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            modifier = Modifier.height(40.dp)
        ) {
            Text("Logout")
        }
    }
}
