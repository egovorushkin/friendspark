package com.friendspark.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.friendspark.ui.AuthEvent
import com.friendspark.ui.AuthViewModel
import com.friendspark.ui.screens.theme.BackgroundDark
import com.friendspark.ui.screens.theme.PlaceholderLabel
import com.friendspark.ui.screens.theme.PrimaryBlue
import com.friendspark.util.AppLogger
import friendspark_kmp.composeapp.generated.resources.Res
import friendspark_kmp.composeapp.generated.resources.ic_apple
import friendspark_kmp.composeapp.generated.resources.ic_back
import friendspark_kmp.composeapp.generated.resources.ic_google
import friendspark_kmp.composeapp.generated.resources.ic_visibility
import friendspark_kmp.composeapp.generated.resources.ic_visibility_off
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject


data class Location(val latitude: Double, val longitude: Double)

class RegisterScreen : Screen {
    @Preview
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val passwordVisible = remember { mutableStateOf(false) }
        val viewModel: AuthViewModel = koinInject()
        val state = viewModel.state

        Box(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header with Back Button
                Row(
                    modifier = Modifier.padding(
                        top = 48.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navigator?.pop() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.Transparent, CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_back),
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        "Register",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 40.dp)
                    )
                    Spacer(Modifier.weight(1f))
                }

                Spacer(Modifier.height(48.dp))

                // Form
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    FloatingLabelInput(
                        value = email,
                        onValueChange = {
                            email = it
                            // Clear email error when user starts typing
                            if (state.emailError != null) {
                                viewModel.clearEmailError()
                            }
                        },
                        label = "Email",
                        placeholder = "Email",
                        keyboardType = KeyboardType.Email,
                        errorMessage = state.emailError
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

                    Spacer(Modifier.height(24.dp))
                    FloatingLabelInput(
                        value = password,
                        onValueChange = {
                            password = it
                            // Clear password error when user starts typing
                            if (state.passwordError != null) {
                                viewModel.clearPasswordError()
                            }
                        },
                        label = "Password",
                        placeholder = "Password",
                        keyboardType = KeyboardType.Password,
                        visualTransformation = if (passwordVisible.value)
                            VisualTransformation.None else PasswordVisualTransformation(),
                        errorMessage = state.passwordError,
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisible.value = !passwordVisible.value
                            }) {
                                Icon(
                                    painter = painterResource(if (passwordVisible.value) Res.drawable.ic_visibility else Res.drawable.ic_visibility_off),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
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
                    // Add name, interests, location inputs as needed
                    Spacer(Modifier.height(32.dp))
                    Button(
                        onClick = {
                            AppLogger.logUI("Register button clicked")
                            viewModel.onEvent(
                                AuthEvent.Register(
                                    email = email,
                                    password = password,
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Register", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Spacer(Modifier.height(24.dp))

                    // OR Divider
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color(0xFF37474F)
                        )
                        Text(
                            " OR ",
                            color = PlaceholderLabel,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color(0xFF37474F)
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    // Google Button
                    OutlinedButton(
                        onClick = { /* TODO:Google Sign-In */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        border = BorderStroke(2.dp, Color(0xFF37474F)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.ic_google),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text("Continue with Google", fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(16.dp))

                    // Apple Button
                    Button(
                        onClick = { /* TODO: Apple Sign-In */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.ic_apple),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            "Continue with Apple",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.weight(1f))
            }

        }

        // After successful registration, navigate to SelectAgeScreen
        if (state.isSuccess) {
            LaunchedEffect(state.isSuccess) {
                AppLogger.logNavigation("Registration successful, navigating to SelectAgeScreen")
                navigator?.push(SelectAgeScreen())
            }
        }

        // Log errors
        if (state.error != null) {
            LaunchedEffect(state.error) {
                AppLogger.logAuthError("Registration error displayed to user: ${state.error}")
            }
        }
    }
}

@Composable
fun FloatingLabelInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    errorMessage: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        val isFocused = remember { mutableStateOf(false) }
        val showLabel = value.isNotEmpty() || isFocused.value
        val hasError = errorMessage != null
        val borderColor = when {
            hasError -> Color(0xFFEF5350) // Red for error
            isFocused.value -> PrimaryBlue
            else -> Color(0xFF37474F)
        }

        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(
                    2.dp,
                    borderColor,
                    RoundedCornerShape(12.dp)
                )
                .background(Color.Transparent),
            placeholder = {
                if (!showLabel) {
                    Text(placeholder, color = PlaceholderLabel)
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = PrimaryBlue,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                errorTextColor = Color(0xFFEF5350),
                errorCursorColor = Color(0xFFEF5350)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
            singleLine = true,
            trailingIcon = trailingIcon,
            isError = hasError,
//            onFocusChanged = { isFocused.value = it.isFocused },

        )

        AnimatedVisibility(
            visible = showLabel,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = (-10).dp)
                .padding(start = 16.dp)
        ) {
            Text(
                text = label,
                color = if (isFocused.value) PrimaryBlue else PlaceholderLabel,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

enum class BottomTab { HOME, EXPLORE, PROFILE, CHATS }

//@Composable
//fun BottomNavigationBar(
//    selectedTab: BottomTab,
//    onTabSelected: (BottomTab) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    NavigationBar(
//        containerColor = BackgroundDark,
//        contentColor = Color.White,
//        modifier = modifier
//            .height(80.dp)
//            .border(1.dp, Color(0xFF37474F), RectangleShape)
//    ) {
//        BottomTab.entries.forEach { tab ->
//            NavigationBarItem(
//                selected = selectedTab == tab,
//                onClick = { onTabSelected(tab) },
//                icon = {
//                    Icon(
//                        painter = painterResource(
//                            when (tab) {
//                                BottomTab.HOME -> Res.drawable.ic_home
//                                BottomTab.EXPLORE -> Res.drawable.ic_search
//                                BottomTab.PROFILE -> Res.drawable.ic_profile
//                                BottomTab.CHATS -> Res.drawable.ic_chat
//                            }
//                        ),
//                        contentDescription = null,
//                        tint = if (selectedTab == tab) PrimaryBlue else PlaceholderLabel
//                    )
//                },
//                label = {
//                    Text(
//                        text = tab.name.lowercase()
//                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
//                        fontSize = 10.sp,
//                        color = if (selectedTab == tab) PrimaryBlue else PlaceholderLabel
//                    )
//                },
//                alwaysShowLabel = true
//            )
//        }
//    }
//}