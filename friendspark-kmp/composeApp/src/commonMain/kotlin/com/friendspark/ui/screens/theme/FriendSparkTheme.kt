package com.friendspark.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE), // Vibrant purple
    secondary = Color(0xFF03DAC5), // Teal
    background = Color(0xFFFFFFFF)
)

@Composable
fun FriendSparkTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ColorScheme, content = content)
}