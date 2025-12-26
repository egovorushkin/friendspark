package com.friendspark.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalResourceApi::class)
@Preview
@Composable
fun OnboardingScreen(
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Place Onboarding1.png in composeApp/src/commonMain/resources
//        Image(
//            painter = painterResource("Onboarding1.png"),
//            contentDescription = "Onboarding Illustration",
//            modifier = Modifier.size(220.dp)
//        )
        Spacer(Modifier.height(32.dp))
        Text("Meet new friends!", fontSize = 28.sp)
        Spacer(Modifier.height(16.dp))
        Text("Find people with similar interests and start chatting.", fontSize = 18.sp)
        Spacer(Modifier.height(32.dp))
        Button(onClick = onContinue, modifier = Modifier.fillMaxWidth()) {
            Text("Get Started")
        }
    }
}
