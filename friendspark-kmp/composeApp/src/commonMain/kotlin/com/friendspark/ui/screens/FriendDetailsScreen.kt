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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import friendspark_kmp.composeapp.generated.resources.ic_chat
import friendspark_kmp.composeapp.generated.resources.ic_home
import friendspark_kmp.composeapp.generated.resources.ic_profile
import friendspark_kmp.composeapp.generated.resources.ic_search
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

class FriendDetailsScreen(
    val friendId: String? = null,
    val friendName: String = "Ethan",
    val friendInterests: List<String> = listOf("Hiking", "Photography"),
    val lastActive: String = "2 hours ago",
    val friendDescription: String = "Ethan is an avid hiker and photographer who loves exploring new trails and capturing stunning landscapes. He's always up for an adventure and enjoys connecting with like-minded individuals.",
    val profileImageUrl: String? = null
) : Screen {

    @Preview
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Navigation Bar
                TopNavigationBar(
                    title = "Friend Details",
                    onBackClick = { navigator.pop() }
                )

                Spacer(Modifier.height(24.dp))

                // Profile Section
                ProfileSection(
                    name = friendName,
                    interests = friendInterests,
                    lastActive = lastActive,
                    description = friendDescription,
                    profileImageUrl = profileImageUrl
                )

                Spacer(Modifier.height(32.dp))

                // Shared Events Section
                SharedEventsSection()

                Spacer(Modifier.height(24.dp))

                // Action Buttons
                ActionButtons(
                    onSendMessageClick = { /* TODO: Navigate to chat */ },
                    onViewEventsClick = { /* TODO: Navigate to events */ }
                )

                Spacer(Modifier.height(100.dp)) // Space for bottom nav
            }

            // Bottom Navigation Bar
            BottomNavigationBar(
                selectedTab = BottomNavTab.PROFILE,
                onTabSelected = { tab ->
                    when (tab) {
                        BottomNavTab.HOME -> { /* Navigate to home */
                        }

                        BottomNavTab.SEARCH -> { /* Navigate to search */
                        }

                        BottomNavTab.PROFILE -> { /* Navigate to profile */
                        }

                        BottomNavTab.MESSAGES -> { /* Navigate to messages */
                        }

                        BottomNavTab.SETTINGS -> { /* Navigate to settings */
                        }
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun TopNavigationBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(48.dp)
                .background(CardDark, CircleShape)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_back),
                contentDescription = "Back",
                tint = TextLight
            )
        }

        Spacer(Modifier.width(16.dp))

        Text(
            text = title,
            color = TextLight,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ProfileSection(
    name: String,
    interests: List<String>,
    lastActive: String,
    description: String,
    profileImageUrl: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(CardDark),
            contentAlignment = Alignment.Center
        ) {
            if (profileImageUrl != null) {
                // TODO: Load image from URL using Coil
                Text(
                    text = name.take(1),
                    color = TextLight,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                // Placeholder avatar
                Text(
                    text = name.take(1),
                    color = TextLight,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Name
        Text(
            text = name,
            color = TextLight,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        // Shared Interests
        Text(
            text = "Shared Interests: ${interests.joinToString(", ")}",
            color = TextMuted,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(8.dp))

        // Last Active
        Text(
            text = "Last active $lastActive",
            color = TextMuted,
            fontSize = 12.sp
        )

        Spacer(Modifier.height(24.dp))

        // Description
        Text(
            text = description,
            color = TextLight,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SharedEventsSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "Shared Events",
            color = TextLight,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Event Card 1 - Hiking Trip
            EventCard(
                title = "Hiking Trip",
                description = "Join us for a scenic hike",
                imageUrl = null,
                modifier = Modifier.weight(1f)
            )

            // Event Card 2 - Photography Workshop
            EventCard(
                title = "Photography Workshop",
                description = "Learn photography basics",
                imageUrl = null,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun EventCard(
    title: String,
    description: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(12.dp))
            .background(CardDark)
    ) {
        // Event Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color(0xFF2A3F4F)),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder for event image
            Text(
                text = "📸",
                fontSize = 32.sp
            )
        }

        // Event Info
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title,
                color = TextLight,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = description,
                color = TextMuted,
                fontSize = 12.sp,
                maxLines = 2
            )
        }
    }
}

@Composable
fun ActionButtons(
    onSendMessageClick: () -> Unit,
    onViewEventsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onSendMessageClick,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Send Message",
                color = TextLight,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = onViewEventsClick,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CardDark
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "View Events",
                color = TextLight,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

enum class BottomNavTab {
    HOME, SEARCH, PROFILE, MESSAGES, SETTINGS
}

@Composable
fun BottomNavigationBar(
    selectedTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(BackgroundDark)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            icon = Res.drawable.ic_home,
            label = "Home",
            isSelected = selectedTab == BottomNavTab.HOME,
            onClick = { onTabSelected(BottomNavTab.HOME) }
        )

        BottomNavItem(
            icon = Res.drawable.ic_search,
            label = "Search",
            isSelected = selectedTab == BottomNavTab.SEARCH,
            onClick = { onTabSelected(BottomNavTab.SEARCH) }
        )

        BottomNavItem(
            icon = Res.drawable.ic_profile,
            label = "Profile",
            isSelected = selectedTab == BottomNavTab.PROFILE,
            onClick = { onTabSelected(BottomNavTab.PROFILE) }
        )

        BottomNavItem(
            icon = Res.drawable.ic_chat,
            label = "Messages",
            isSelected = selectedTab == BottomNavTab.MESSAGES,
            onClick = { onTabSelected(BottomNavTab.MESSAGES) }
        )

        // Settings - using text icon
        Column(
            modifier = Modifier
                .clickable { onTabSelected(BottomNavTab.SETTINGS) }
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "⚙️",
                fontSize = 24.sp
            )
            Text(
                text = "Settings",
                color = if (selectedTab == BottomNavTab.SETTINGS) PrimaryBlue else TextMuted,
                fontSize = 12.sp,
                fontWeight = if (selectedTab == BottomNavTab.SETTINGS) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: org.jetbrains.compose.resources.DrawableResource,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = label,
            tint = if (isSelected) PrimaryBlue else TextMuted,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = label,
            color = if (isSelected) PrimaryBlue else TextMuted,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

