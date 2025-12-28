package com.friendspark.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        Text("Home")
    }
}
