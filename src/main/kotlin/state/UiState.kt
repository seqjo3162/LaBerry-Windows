package state

import androidx.compose.runtime.mutableStateOf

object UiState {
    enum class Section {
        FRIENDS,  // домашний экран с друзьями
        SERVERS   // обычный вид с серверами / каналами
    }

    val activeSection = mutableStateOf(Section.SERVERS)
}
