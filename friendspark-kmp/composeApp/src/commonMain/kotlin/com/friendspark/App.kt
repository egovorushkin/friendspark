package com.friendspark

import androidx.compose.runtime.Composable
import com.friendspark.ui.screens.OnboardingScreen
import com.friendspark.ui.theme.FriendSparkTheme
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    FriendSparkTheme {
        Navigator(
            screen = OnboardingScreen()
        )
    }
}