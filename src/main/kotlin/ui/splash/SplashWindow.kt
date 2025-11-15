package ui.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onLoaded: () -> Unit) {

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(150)
        visible = true

        // имитация загрузки (здесь будет SessionState.tryAutoLogin())
        delay(1600)

        visible = false
        delay(400)

        onLoaded()
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(600)),
        exit = fadeOut(tween(600))
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E1F22)),
            contentAlignment = Alignment.Center
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                AnimatedBerry()

                Spacer(Modifier.height(20.dp))

                Text(
                    "Загрузка LaBerry…",
                    color = Color(0xFFB5BAC1),
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun AnimatedBerry() {
    val scale by rememberInfiniteTransition().animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val glow by rememberInfiniteTransition().animateFloat(
        initialValue = 6f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier.size(150.dp),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .size(130.dp)
                .background(Color(0xFF8A2BE2).copy(alpha = 0.25f), CircleShape)
                .scale(scale)
        )

        Box(
            modifier = Modifier
                .size(110.dp)
                .background(Color(0xFF8A2BE2), CircleShape)
                .scale(scale),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "LB",
                color = Color.White,
                fontSize = 38.sp
            )
        }
    }
}
