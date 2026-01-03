package com.friendspark.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.friendspark.ui.screens.theme.BackgroundDark
import com.friendspark.ui.screens.theme.CardDark
import com.friendspark.ui.screens.theme.PrimaryBlue
import com.friendspark.ui.screens.theme.TextLight
import com.friendspark.ui.screens.theme.TextMuted
import friendspark_kmp.composeapp.generated.resources.Res
import friendspark_kmp.composeapp.generated.resources.ic_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

class CompleteProfileScreen(
    val onComplete: () -> Unit = {},
    val onSkip: () -> Unit = {}
) : Screen {

    @Preview
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var bioText by remember { mutableStateOf("") }
        var profileImageUri by remember { mutableStateOf<String?>(null) }

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
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Header
                TopHeader(
                    onCloseClick = { navigator.pop() }
                )

                Spacer(Modifier.height(32.dp))

                // Main Title Section
                TitleSection()

                Spacer(Modifier.height(40.dp))

                // Profile Picture Section
                ProfilePictureSection(
                    profileImageUri = profileImageUri,
                    onImageSelected = { uri -> profileImageUri = uri }
                )

                Spacer(Modifier.height(40.dp))

                // Bio Section
                BioSection(
                    bioText = bioText,
                    onBioChange = { bioText = it }
                )

                Spacer(Modifier.height(48.dp))

                // Action Buttons
                ProfileActionButtons(
                    onContinueClick = {
                        onComplete()
                        // Navigate to next screen
                    },
                    onSkipClick = {
                        onSkip()
                        // Navigate to next screen
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun TopHeader(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Empty space for centering
        Spacer(Modifier.width(48.dp))

        // FriendSpark Title
        Text(
            text = "FriendSpark",
            color = TextLight,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        // Close Button
        IconButton(
            onClick = onCloseClick,
            modifier = Modifier.size(48.dp)
        ) {
            Text(
                text = "✕",
                color = TextLight,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TitleSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Complete Your Profile",
            color = TextLight,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "A great profile helps you connect with the right people.",
            color = TextMuted,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun ProfilePictureSection(
    profileImageUri: String?,
    onImageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Heading
        Text(
            text = "Add a profile picture",
            color = TextLight,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Instruction
        Text(
            text = "Upload a photo so your friends can recognize you.",
            color = TextMuted,
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        // Profile Picture Placeholder
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(CardDark),
            contentAlignment = Alignment.Center
        ) {
            if (profileImageUri != null) {
                // TODO: Load image from URI using Coil
                Text(
                    text = "📷",
                    fontSize = 48.sp
                )
            } else {
                // Camera icon with plus
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📷+",
                        color = TextLight,
                        fontSize = 32.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Upload Photo Button
        Button(
            onClick = {
                // TODO: Open image picker
                // For now, simulate selection
                onImageSelected("placeholder_uri")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CardDark
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Upload Photo",
                color = TextLight,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun BioSection(
    bioText: String,
    onBioChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        // Heading
        Text(
            text = "Write a short bio",
            color = TextLight,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Instruction
        Text(
            text = "Tell others a little about yourself.",
            color = TextMuted,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Bio Text Field
        OutlinedTextField(
            value = bioText,
            onValueChange = onBioChange,
            placeholder = {
                Text(
                    text = "Write your bio here...",
                    color = TextMuted
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextLight,
                unfocusedTextColor = TextLight,
                focusedBorderColor = Color(0xFF778DA9),
                unfocusedBorderColor = Color(0xFF415A77),
                focusedContainerColor = CardDark,
                unfocusedContainerColor = CardDark,
                cursorColor = PrimaryBlue
            ),
            shape = RoundedCornerShape(12.dp),
            maxLines = 5,
            minLines = 5
        )
    }
}

@Composable
fun ProfileActionButtons(
    onContinueClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Continue Button
        Button(
            onClick = onContinueClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Continue",
                color = TextLight,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(16.dp))

        // Skip Link
        Text(
            text = "Skip for now",
            color = PrimaryBlue,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable(onClick = onSkipClick)
        )
    }
}

