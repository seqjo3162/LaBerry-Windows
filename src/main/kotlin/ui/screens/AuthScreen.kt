package ui.screens

import androidx.compose.runtime.*
import androidx.compose.material.*
import androidx.compose.ui.unit.dp
import androidx.compose.animation.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import api.ApiClient
import state.SessionState


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthScreen(onSuccess: () -> Unit) {
    var tab by remember { mutableStateOf(AuthTab.LOGIN) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(420.dp)
        ) {
            // переключатель вкладок
            Row {
                AuthTabButton(
                    text = "Login",
                    isActive = tab == AuthTab.LOGIN,
                    onClick = { tab = AuthTab.LOGIN }
                )
                Spacer(Modifier.width(16.dp))
                AuthTabButton(
                    text = "Register",
                    isActive = tab == AuthTab.REGISTER,
                    onClick = { tab = AuthTab.REGISTER }
                )
            }

            Spacer(Modifier.height(28.dp))

            AnimatedContent(
                targetState = tab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(250)) togetherWith fadeOut(animationSpec = tween(250))
                }
            ) { current ->
                when (current) {
                    AuthTab.LOGIN -> LoginPanel(onSuccess = onSuccess) { tab = AuthTab.REGISTER }
                    AuthTab.REGISTER -> RegisterPanel(onSuccess = onSuccess) { tab = AuthTab.LOGIN }
                }
            }
        }
    }
}

enum class AuthTab { LOGIN, REGISTER }

@Composable
fun AuthTabButton(text: String, isActive: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) { onClick() },
        color = if (isActive) Color(0xFF5865F2) else Color(0xFF999999),
        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
    )
}

@Composable
fun DiscordField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    var focused by remember { mutableStateOf(false) }
    var pressed by remember { mutableStateOf(false) }

    val activeColor = Color(0xFF5865F2)
    val pressedColor = Color(0xFF505050)
    val normalBorder = Color(0xFFCCCCCC)

    val borderColor by animateColorAsState(
        targetValue = when {
            pressed -> pressedColor
            focused -> activeColor
            else -> normalBorder
        },
        animationSpec = tween(160)
    )

    Column {
        Text(
            placeholder.uppercase(),
            color = Color(0xFF555555),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                color = activeColor,
                fontSize = MaterialTheme.typography.body1.fontSize
            ),
            cursorBrush = SolidColor(activeColor),

            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focused = it.isFocused }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            pressed = true
                            tryAwaitRelease()
                            pressed = false
                        }
                    )
                }
                .background(Color.White, RoundedCornerShape(8.dp))
                .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 12.dp),

            decorationBox = { innerTextField: @Composable () -> Unit ->
            Box(
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color(0xFF777777)
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun AnimatedErrorText(full: String?) {
    if (full.isNullOrBlank()) return

    var showText by remember { mutableStateOf("") }

    LaunchedEffect(full) {
        showText = ""
        for (i in full.indices) {
            showText = full.substring(0, i + 1)
            delay(12) // скорость "печати"
        }
    }

    Text(
        showText,
        color = Color(0xFFFF5555),
        modifier = Modifier.padding(top = 12.dp)
    )
}

@Composable
fun LoginPanel(onSuccess: () -> Unit, goRegister: () -> Unit) {
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var code2FA by remember { mutableStateOf("") }

    val isLoading = SessionState.isLoading
    val error = SessionState.error
    val pending2FA = SessionState.pending2FAUserId != null

    Column(
        modifier = Modifier.width(420.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!pending2FA) {
            DiscordField(username, { username = it }, "Username")
            Spacer(Modifier.height(12.dp))
            DiscordField(password, { password = it }, "Password")

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    scope.launch {
                        val ok = SessionState.login(username, password)
                        if (ok) onSuccess()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) { Text("Login") }

        } else {
            DiscordField(code2FA, { code2FA = it }, "2FA Code")

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    scope.launch {
                        val ok = SessionState.verify2FA(code2FA)
                        if (ok) onSuccess()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) { Text("Verify Code") }
        }

        TextButton(onClick = goRegister) {
            Text("Create account")
        }

        AnimatedErrorText(error)
    }
}

@Composable
fun RegisterPanel(onSuccess: () -> Unit, goLogin: () -> Unit) {
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.width(420.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DiscordField(username, { username = it }, "Username")
        Spacer(Modifier.height(12.dp))
        DiscordField(password, { password = it }, "Password")

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                scope.launch {
                    val ok = ApiClient.register(username, password)
                    if (ok) {
                        SessionState.login(username, password)
                        onSuccess()
                    } else {
                        localError = "Registration failed"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        TextButton(onClick = goLogin) {
            Text("Already have an account? Login")
        }

        AnimatedErrorText(localError)
    }
}

@Composable
fun ThemeSwitcher(onToggle: () -> Unit) {
    Text(
        "Theme",
        modifier = Modifier
            .padding(16.dp)
            .clickable { onToggle() },
        color = Color(0xFF5865F2),
        fontWeight = FontWeight.Bold
    )
}
