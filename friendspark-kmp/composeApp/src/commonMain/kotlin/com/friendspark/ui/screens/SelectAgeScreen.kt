package com.friendspark.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

class SelectAgeScreen(
    val initialAge: Int = 18,
    val minAge: Int = 13,
    val maxAge: Int = 100,
    val onNext: (Int) -> Unit = {},
    val onEnterDateOfBirth: () -> Unit = {}
) : Screen {

    @Preview
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var selectedAge by remember { mutableStateOf(initialAge) }

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
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(60.dp))

                // Title
                Text(
                    text = "What's your age?",
                    color = TextLight,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))

                // Description
                Text(
                    text = "To ensure a safe and age-appropriate experience, please provide your age. This information will be kept private.",
                    color = TextMuted,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )

                Spacer(Modifier.height(48.dp))

                // Age Picker
                AgePicker(
                    selectedAge = selectedAge,
                    onAgeSelected = { age -> selectedAge = age },
                    minAge = minAge,
                    maxAge = maxAge,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                Spacer(Modifier.height(40.dp))

                // Action Buttons
                AgeActionButtons(
                    onNextClick = {
                        onNext(selectedAge)
                        // Navigate to next screen
                    },
                    onEnterDateOfBirthClick = {
                        onEnterDateOfBirth()
                        // Navigate to date of birth screen
                    }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun AgePicker(
    selectedAge: Int,
    onAgeSelected: (Int) -> Unit,
    minAge: Int,
    maxAge: Int,
    modifier: Modifier = Modifier
) {
    val ages = (minAge..maxAge).toList()
    val scrollState = rememberScrollState()
    val itemHeight = 80.dp

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Background highlight for selected item area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Transparent)
        )

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Add padding at top to center items
            Spacer(Modifier.height(200.dp))

            ages.forEach { age ->
                AgeItem(
                    age = age,
                    isSelected = age == selectedAge,
                    onClick = { onAgeSelected(age) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                )
            }

            // Add padding at bottom to center items
            Spacer(Modifier.height(200.dp))
        }
    }
}

@Composable
fun AgeItem(
    age: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) PrimaryBlue else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 32.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = age.toString(),
            color = if (isSelected) TextLight else TextMuted,
            fontSize = if (isSelected) 36.sp else 18.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun AgeActionButtons(
    onNextClick: () -> Unit,
    onEnterDateOfBirthClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Next Button
        Button(
            onClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Next",
                color = TextLight,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Enter Date of Birth Button
        Button(
            onClick = onEnterDateOfBirthClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CardDark
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Enter Date of Birth Instead",
                color = TextLight,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

