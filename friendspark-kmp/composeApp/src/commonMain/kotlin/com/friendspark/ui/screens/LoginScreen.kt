package com.friendspark.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.friendspark.ui.AuthEvent
import com.friendspark.ui.AuthViewModel
import com.friendspark.ui.screens.theme.BackgroundDark
import com.friendspark.util.AppLogger
import friendspark_kmp.composeapp.generated.resources.Res
import friendspark_kmp.composeapp.generated.resources.ic_apple
import friendspark_kmp.composeapp.generated.resources.ic_back
import friendspark_kmp.composeapp.generated.resources.ic_google
import friendspark_kmp.composeapp.generated.resources.ic_visibility
import friendspark_kmp.composeapp.generated.resources.ic_visibility_off
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var isPasswordVisible by remember { mutableStateOf(false) }
        val viewModel: AuthViewModel = koinInject()
        val state = viewModel.state

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark)
        ) {
            // Back Button
            IconButton(
                onClick = { navigator.pop() },
                modifier = Modifier
                    .padding(16.dp)
                    .size(48.dp)
                    .background(Color(0xFF1B263B), CircleShape)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_back),
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.height(80.dp))

                // Title
                Text(
                    text = "Welcome Back!",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Log in to find your crew.",
                    color = Color(0xFFB0BEC5),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(Modifier.height(48.dp))

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        // Clear email error when user starts typing
                        if (state.emailError != null) {
                            viewModel.clearEmailError()
                        }
                    },
                    label = { Text("Email", color = Color(0xFFB0BEC5)) },
                    singleLine = true,
                    isError = state.emailError != null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = if (state.emailError != null) Color(0xFFEF5350) else Color(
                            0xFF778DA9
                        ),
                        unfocusedBorderColor = if (state.emailError != null) Color(0xFFEF5350) else Color(
                            0xFF415A77
                        ),
                        focusedLabelColor = Color(0xFF778DA9),
                        cursorColor = Color(0xFF00D4FF),
                        focusedContainerColor = Color(0xFF1B263B),
                        unfocusedContainerColor = Color(0xFF1B263B),
                        errorTextColor = Color(0xFFEF5350),
                        errorCursorColor = Color(0xFFEF5350)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                )

                // Email error message
                if (state.emailError != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = state.emailError,
                        color = Color(0xFFEF5350),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Spacer(Modifier.height(20.dp))

                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        // Clear password error when user starts typing
                        if (state.passwordError != null) {
                            viewModel.clearPasswordError()
                        }
                    },
                    label = { Text("Password", color = Color(0xFFB0BEC5)) },
                    singleLine = true,
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = state.passwordError != null,
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                painter = painterResource(if (isPasswordVisible) Res.drawable.ic_visibility else Res.drawable.ic_visibility_off),
                                contentDescription = null,
                                tint = Color(0xFFB0BEC5)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = if (state.passwordError != null) Color(0xFFEF5350) else Color(
                            0xFF778DA9
                        ),
                        unfocusedBorderColor = if (state.passwordError != null) Color(0xFFEF5350) else Color(
                            0xFF415A77
                        ),
                        focusedLabelColor = Color(0xFF778DA9),
                        cursorColor = Color(0xFF00D4FF),
                        focusedContainerColor = Color(0xFF1B263B),
                        unfocusedContainerColor = Color(0xFF1B263B),
                        errorTextColor = Color(0xFFEF5350),
                        errorCursorColor = Color(0xFFEF5350)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                )

                // Password error message
                if (state.passwordError != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = state.passwordError,
                        color = Color(0xFFEF5350),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                // Forgot Password
                Text(
                    text = "Forgot Password?",
                    color = Color(0xFFB0BEC5),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 12.dp)
                        .clickable { /* TODO */ }
                )

                Spacer(Modifier.height(32.dp))

                // Log In Button
                Button(
                    onClick = {
                        AppLogger.logUI("Login button clicked")
                        viewModel.onEvent(AuthEvent.Login(email, password))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00D4FF),
                        contentColor = Color.Black
                    )
                ) {
                    Text("Log In", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(32.dp))

                // OR Divider
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = Color(0xFF415A77),
                        thickness = 1.dp
                    )
                    Text(
                        " OR ",
                        color = Color(0xFFB0BEC5),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = Color(0xFF415A77),
                        thickness = 1.dp
                    )
                }

                Spacer(Modifier.height(32.dp))

                // Google Button
                OutlinedButton(
                    onClick = { /* Google Sign-In */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFF1B263B),
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color(0xFF415A77))
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_google),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Text("Continue with Google", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(Modifier.height(16.dp))

                // Apple Button
                OutlinedButton(
                    onClick = { /* Apple Sign-In (iOS only) */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFF1B263B),
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color(0xFF415A77))
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_apple),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Text("Continue with Apple", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(Modifier.height(48.dp))

                // Sign Up Link
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Don't have an account? ",
                        color = Color(0xFFB0BEC5),
                        fontSize = 16.sp
                    )
                    Text(
                        "Sign Up",
                        color = Color(0xFF00D4FF),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { navigator.push(RegisterScreen()) }
                    )
                }

                Spacer(Modifier.height(40.dp))
            }
        }

        if (state.isSuccess) {
            LaunchedEffect(state.isSuccess) {
                AppLogger.logNavigation("Login successful, navigating to HomeScreen")
                // Navigate to HomeScreen after successful login
                navigator.push(FriendDetailsScreen())
            }
        }

        // Log errors
        if (state.error != null) {
            LaunchedEffect(state.error) {
                AppLogger.logAuthError("Login error displayed to user: ${state.error}")
            }
        }
    }
}
