package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FriendsScreen() {
    var query by remember { mutableStateOf("") }

    // Заглушка, потом подставишь реальный список друзей
    val friends = listOf("User-001", "Berry", "TestUser")
    val filtered = friends.filter { it.contains(query.trim(), ignoreCase = true) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth()) {
            Text(
                "Друзья",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface
            )
            Spacer(Modifier.weight(1f))
            Button(onClick = { /* окно добавления в друзья */ }) {
                Text("Добавить в друзья")
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Поиск по нику") },
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        if (filtered.isEmpty()) {
            Text(
                "Ничего не найдено. Попробуйте другой ник.",
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        } else {
            LazyColumn {
                items(filtered) { name ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                                    color = MaterialTheme.colors.onPrimary
                                )
                            }
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(name, color = MaterialTheme.colors.onSurface)
                    }
                }
            }
        }
    }
}
