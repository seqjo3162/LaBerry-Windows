package ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import state.SessionState

@Composable
fun AutoLoginScreen(
    onSuccess: () -> Unit,
    onFail: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var error by remember { mutableStateOf<String?>(null) }
    var loadingDone by remember { mutableStateOf(false) }

    // loading animation
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(Unit) {
        delay(250) // небольшая задержка для красоты
        val ok = SessionState.tryAutoLogin()

        if (ok) {
            delay(300)
            onSuccess()
        } else {
            error = "Автоматический вход не выполнен"
            delay(900)
            loadingDone = true
            onFail()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                "LaBerry",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5865F2).copy(alpha = alpha)
            )

            Spacer(Modifier.height(20.dp))

            if (error != null && loadingDone) {
                Text(
                    error!!,
                    color = Color.Red,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                Text(
                    "Вход...",
                    color = Color.Gray.copy(alpha = alpha),
                    fontSize = 16.sp
                )
            }
        }
    }
}
