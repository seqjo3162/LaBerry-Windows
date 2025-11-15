package state

import androidx.compose.runtime.*
import models.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.Text

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import api.ApiClient


object FriendsState {

    var searchResults by mutableStateOf<List<User>>(emptyList())
    var searchLoading by mutableStateOf(false)
    // userId → online(true)/offline(false)
    val presence = mutableStateMapOf<Int, Boolean>()

    fun setOnline(userId: Int, isOnline: Boolean) {
        presence[userId] = isOnline
    }

    fun isOnline(userId: Int): Boolean {
        return presence[userId] == true
    }
    @Composable
    fun FriendsList() {
        val presence = FriendsState.presence

        LazyColumn {
            items(presence.keys.toList()) { userId ->
                val isOnline = FriendsState.isOnline(userId)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(if (isOnline) Color(0xFF43B581) else Color(0xFF747F8D), shape = CircleShape)
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        "User #$userId",
                        color = Color.White
                    )
                }
            }
        }
    }

    suspend fun searchUsers(query: String) {
        if (query.isBlank()) {
            searchResults = emptyList()
            return
        }

        searchLoading = true

        try {
            val result = ApiClient.authGet<List<User>>(
                path = "/api/users/search?query=$query",
                token = SessionState.token ?: ""
            )

            searchResults = result ?: emptyList()

        } catch (_: Exception) {
            searchResults = emptyList()
        }

        searchLoading = false
    }
}
