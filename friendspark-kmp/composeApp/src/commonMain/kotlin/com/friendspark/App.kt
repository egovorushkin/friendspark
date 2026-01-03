package com.friendspark

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.friendspark.di.appModule
import com.friendspark.ui.screens.OnboardingScreen
import com.friendspark.ui.theme.FriendSparkTheme
import com.friendspark.util.AppLogger
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    // Initialize logging
    Napier.base(DebugAntilog())
    AppLogger.info("FriendSpark app initialized", AppLogger.Category.GENERAL)

    KoinApplication(application = {
        modules(appModule)
        AppLogger.debug("Koin dependency injection initialized", AppLogger.Category.GENERAL)
    }) {
        FriendSparkTheme {
            Navigator(
                screen = OnboardingScreen()
            )
            AppLogger.logNavigation("Navigated to OnboardingScreen")
        }
    }
}