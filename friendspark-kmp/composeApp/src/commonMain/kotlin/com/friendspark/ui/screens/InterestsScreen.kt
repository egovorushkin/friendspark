package com.friendspark.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

data class Interest(val name: String, val isSelected: Boolean = false)

object InterestsScreen : Screen {
    private val allInterests = listOf(
        "Hiking", "Photography", "Gaming", "Reading",
        "Cooking", "Travel", "Music", "Art",
        "Sports", "Movies", "Tech", "Volunteering"
    )

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var interests by remember {
            mutableStateOf(allInterests.map { Interest(it) })
        }
        val selectedCount = interests.count { it.isSelected }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0D1B2A))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(60.dp))

                // FriendSpark Logo / Title
                Text(
                    text = "FriendSpark",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 40.dp)
                )

                // Main Title
                Text(
                    text = "What are you\ninterested in?",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 44.sp
                )

                Spacer(Modifier.height(16.dp))

                // Subtitle
                Text(
                    text = "Select at least 3 interests to help us find your people.",
                    color = Color(0xFFB0BEC5),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 26.sp
                )

                Spacer(Modifier.height(48.dp))

                // Interests Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(interests) { interest ->
                        InterestChip(
                            interest = interest,
                            onClick = {
                                interests = interests.map {
                                    if (it.name == interest.name) it.copy(isSelected = !it.isSelected)
                                    else it
                                }
                            }
                        )
                    }
                }

                Spacer(Modifier.height(40.dp))

                // Continue Button
                Button(
                    onClick = {
                        if (selectedCount >= 3) {
                            // Save interests & navigate
                            navigator.push(HomeScreen)
                        }
                    },
                    enabled = selectedCount >= 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00D4FF),
                        contentColor = Color.Black,
                        disabledContainerColor = Color(0xFF415A77)
                    )
                ) {
                    Text(
                        "Continue",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(20.dp))

                // Skip for now
                Text(
                    text = "Skip for now",
                    color = Color(0xFF00D4FF),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clickable { navigator.push(HomeScreen) }
                        .padding(8.dp)
                )

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun InterestChip(
    interest: Interest,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp))
            .background(
                if (interest.isSelected) Color(0xFF00D4FF)
                else Color(0xFF1B263B)
            )
            .border(
                width = 1.5.dp,
                color = if (interest.isSelected) Color(0xFF00D4FF) else Color(0xFF415A77),
                shape = RoundedCornerShape(30.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = interest.name,
            color = if (interest.isSelected) Color.Black else Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}