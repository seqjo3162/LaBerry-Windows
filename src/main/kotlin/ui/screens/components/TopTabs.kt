package ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopTabs(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(0xFF2B2D31))
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        DiscordTab("Online", 0, selectedTab, onTabSelected)
        DiscordTab("All", 1, selectedTab, onTabSelected)
        DiscordTab("Add Friends", 2, selectedTab, onTabSelected)
    }
}

@Composable
fun DiscordTab(
    text: String,
    index: Int,
    selectedTab: Int,
    onClick: (Int) -> Unit
) {
    val isSelected = selectedTab == index

    val bg = if (isSelected) Color(0xFF3F4147) else Color.Transparent
    val color = if (isSelected) Color.White else Color(0xFFB9BBBE)

    Box(
        modifier = Modifier
            .padding(end = 8.dp)
            .height(32.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .clickable { onClick(index) }
            .padding(horizontal = 12.dp),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 14.sp
        )
    }
}
