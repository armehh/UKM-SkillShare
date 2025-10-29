package com.example.ukmskillshare10

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavigationBar(
    currentScreen: String = "home",
    onNavigate: (String) -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                icon = R.drawable.home,
                label = "Home",
                isSelected = currentScreen == "home",
                onClick = { onNavigate("home") }
            )
            NavItem(
                icon = R.drawable.browse,
                label = "Browse",
                isSelected = currentScreen == "browse",
                onClick = { onNavigate("browse") }
            )
            NavItem(
                icon = R.drawable.sessions,
                label = "Sessions",
                isSelected = currentScreen == "sessions",
                onClick = { onNavigate("sessions") }
            )
            NavItem(
                icon = R.drawable.profile,
                label = "Profile",
                isSelected = currentScreen == "profile",
                onClick = { onNavigate("profile") }
            )
        }
    }
}

@Composable
fun NavItem(
    icon: Int,
    label: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = if (isSelected) Color(0xFF445BA5) else Color(0xFF666666),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color(0xFF445BA5) else Color(0xFF666666)
        )
    }
}
