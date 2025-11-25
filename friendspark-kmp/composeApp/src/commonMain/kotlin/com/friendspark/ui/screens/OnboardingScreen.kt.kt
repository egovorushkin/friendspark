package com.friendspark.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.friendspark.ui.screens.theme.BackgroundDark
import com.friendspark.ui.screens.theme.CardDark
import com.friendspark.ui.screens.theme.PrimaryBlue
import com.friendspark.ui.screens.theme.TextLight
import com.friendspark.ui.screens.theme.TextMuted
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import friendspark_kmp.composeapp.generated.resources.Res
import friendspark_kmp.composeapp.generated.resources.onboarding_hero
import org.jetbrains.compose.resources.painterResource

class OnboardingScreen : Screen {
    @Composable
    override fun Content() {
        //        OnboardingComposable(
//            onRegisterClick = { println("Register clicked");},
//            onLoginClick = { println("Login clicked"); }
//        )
        val navigator = LocalNavigator.currentOrThrow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark)
        ) {
            // Hero image (top half)
            Image(
                painter = painterResource(Res.drawable.onboarding_hero),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(300.dp)) // push content below image

                // Title & subtitle
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Welcome to\nFriendSpark",
                        color = TextLight,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 38.sp
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Discover new friends and exciting events nearby,\nall while keeping your exact location private.",
                        color = TextLight,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )
                }

                // Buttons
                Column {
                    Button(
                        onClick = { navigator.push(RegisterScreen()) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Register", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CardDark),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Log In", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                // Footer
                Text(
                    text = "By continuing, you agree to our Terms of Service and Privacy Policy.",
                    color = TextMuted,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            }
        }
    }


}

//@Composable
//fun OnboardingScreen(
//    onRegisterClick: () -> Unit,
//    onLoginClick: () -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(BackgroundDark)
//    ) {
//        // Hero image (top half)
//        Image(
//            painter = painterResource(Res.drawable.onboarding_hero),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(380.dp)
//        )
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 24.dp),
//            verticalArrangement = Arrangement.SpaceBetween,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Spacer(Modifier.height(300.dp)) // push content below image
//
//            // Title & subtitle
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Text(
//                    text = "Welcome to\nFriendSpark",
//                    color = TextLight,
//                    fontSize = 32.sp,
//                    fontWeight = FontWeight.Bold,
//                    textAlign = TextAlign.Center,
//                    lineHeight = 38.sp
//                )
//                Spacer(Modifier.height(16.dp))
//                Text(
//                    text = "Discover new friends and exciting events nearby,\nall while keeping your exact location private.",
//                    color = TextLight,
//                    fontSize = 16.sp,
//                    textAlign = TextAlign.Center,
//                    lineHeight = 24.sp
//                )
//            }
//
//            // Buttons
//            Column {
//                Button(
//                    onClick = onRegisterClick,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(56.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Text("Register", fontWeight = FontWeight.Bold, fontSize = 16.sp)
//                }
//                Spacer(Modifier.height(12.dp))
//                Button(
//                    onClick = onLoginClick,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(56.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = CardDark),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Text("Log In", fontWeight = FontWeight.Bold, fontSize = 16.sp)
//                }
//            }
//
//            // Footer
//            Text(
//                text = "By continuing, you agree to our Terms of Service and Privacy Policy.",
//                color = TextMuted,
//                fontSize = 12.sp,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.padding(vertical = 24.dp)
//            )
//        }
//    }
//}