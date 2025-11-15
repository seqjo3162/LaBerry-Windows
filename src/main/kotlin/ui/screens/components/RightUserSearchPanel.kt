package ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RightUserSearchPanel() {

    var search by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .width(280.dp)
            .fillMaxHeight()
            .background(Color(0xFF2B2D31))
            .padding(12.dp)
    ) {

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            label = { Text("Поиск пользователя") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1E1F22),
                unfocusedContainerColor = Color(0xFF1E1F22),
                focusedBorderColor = Color(0xFF5865F2),
                unfocusedBorderColor = Color(0xFF80848E),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(Modifier.height(16.dp))

        Text("Результаты:", color = Color(0xFF80848E))
    }
}
