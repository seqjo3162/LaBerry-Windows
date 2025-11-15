package ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FriendsSearchField(
    text: String,
    onTextChange: (String) -> Unit,
    maxLength: Int = 64
) {
    TextField(
        value = text,
        onValueChange = { if (it.length <= maxLength) onTextChange(it) },
        placeholder = {
            Text(
                "Поиск",
                color = Color(0xFF949BA4)   // Discord placeholder
            )
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp)
            .height(64.dp),
        shape = RoundedCornerShape(6.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF1E1F22),
            unfocusedContainerColor = Color(0xFF1E1F22),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
        )
    )
}
