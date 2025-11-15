package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import state.SessionState

@Composable
fun AuthScreen(
    onSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isRegister by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val error = SessionState.error
    val loading = SessionState.isLoading
    val rememberMe = SessionState.rememberMe

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF313338)),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .width(380.dp)
                .shadow(18.dp, RoundedCornerShape(12.dp))
                .background(Color(0xFF2B2D31), RoundedCornerShape(12.dp))
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ------------ TITLE ------------
            Text(
                if (!isRegister) "Welcome back!"
                else "Create your account",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                if (!isRegister) "We're so excited to see you again!"
                else "Join the LaBerry community!",
                color = Color(0xFFB9BBBE),
                fontSize = 14.sp
            )

            Spacer(Modifier.height(24.dp))

            // ------------ USERNAME LABEL ------------
            Text(
                "USERNAME",
                color = Color(0xFFB9BBBE),
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(6.dp))

            // ------------ USERNAME INPUT ------------
            TextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1E1F22),
                    unfocusedContainerColor = Color(0xFF1E1F22),
                    focusedIndicatorColor = Color(0xFF5865F2),
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(Modifier.height(16.dp))

            // ------------ PASSWORD LABEL ------------
            Text(
                "PASSWORD",
                color = Color(0xFFB9BBBE),
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(6.dp))

            // ------------ PASSWORD INPUT ------------
            TextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1E1F22),
                    unfocusedContainerColor = Color(0xFF1E1F22),
                    focusedIndicatorColor = Color(0xFF5865F2),
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(Modifier.height(10.dp))

            // ------------ REMEMBER ME (only login) ------------
            if (!isRegister) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { SessionState.rememberMe = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF5865F2)
                        )
                    )
                    Text(
                        "Keep me logged in",
                        color = Color(0xFFB9BBBE)
                    )
                }

                Spacer(Modifier.height(20.dp))
            } else {
                Spacer(Modifier.height(30.dp))
            }

            // ------------ ACTION BUTTON (LOGIN or REGISTER) ------------
            Button(
                onClick = {
                    scope.launch {
                        val ok = if (!isRegister) {
                            // LOGIN
                            SessionState.login(username.trim(), password.trim())
                        } else {
                            // REGISTER → auto-login
                            val regOk = SessionState.register(username.trim(), password.trim())
                            if (regOk) SessionState.login(username.trim(), password.trim())
                            else false
                        }

                        if (ok) onSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5865F2)
                ),
                enabled = !loading
            ) {
                Text(
                    if (loading) "Loading..."
                    else if (!isRegister) "Login"
                    else "Register",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(20.dp))

            // ------------ MODE SWITCH (LOGIN <-> REGISTER) ------------
            Row {
                Text(
                    if (!isRegister) "Need an account? "
                    else "Already have an account? ",
                    color = Color(0xFFB9BBBE)
                )
                Text(
                    if (!isRegister) "Register"
                    else "Login",
                    color = Color(0xFF5865F2),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        isRegister = !isRegister
                    }
                )
            }

            // ------------ ERROR MESSAGE ------------
            if (error != null) {
                Spacer(Modifier.height(16.dp))
                Text(
                    error,
                    color = Color.Red,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
