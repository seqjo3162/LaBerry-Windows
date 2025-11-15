package ui.screens.friends

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import state.FriendsState
import ui.screens.components.FriendsSearchField
import ui.screens.components.UserSearchItem

@Composable
fun AddFriendsScreen() {

    var query by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        FriendsSearchField(
            text = query,
            onTextChange = {
                query = it
                scope.launch { FriendsState.searchUsers(it) }
            }
        )

        if (FriendsState.searchLoading) {
            Text(
                "Поиск...",
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
            return
        }

        if (FriendsState.searchResults.isEmpty() && query.isNotBlank()) {
            Text(
                "Никто не найден",
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
            return
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(FriendsState.searchResults.size) { index ->
                val user = FriendsState.searchResults[index]
                UserSearchItem(user)
            }
        }
    }
}
