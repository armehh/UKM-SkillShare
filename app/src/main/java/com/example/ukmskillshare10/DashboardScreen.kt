package com.example.ukmskillshare10

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen(
    name: String = "Armin",
    subtitle: String = "Student at Faculty of Information Science & Technology",
    onBrowseClick: () -> Unit,
    onBookingsClick: () -> Unit,
    onFeedbackClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Welcome, $name",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF445BA5)
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = Color(0xFF5C6DA7)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.personal),
                contentDescription = "avatar",
                modifier = Modifier
                    .size(56.dp)
                    .border(2.dp, Color(0xFF445BA5), RoundedCornerShape(28.dp))
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reward points card
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE9ECF8)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Reward Points", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E3A74))
                    Spacer(Modifier.height(4.dp))
                    Text("Keep learning to earn more!", fontSize = 12.sp, color = Color(0xFF5C6DA7))
                    Spacer(Modifier.height(12.dp))
                    Text("View rewards catalog", fontSize = 12.sp, color = Color(0xFF2E3A74), fontWeight = FontWeight.Medium)
                }
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(Color.White, RoundedCornerShape(36.dp))
                        .border(1.dp, Color(0xFFBDC6F3), RoundedCornerShape(36.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "0", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E3A74))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Upcoming Sessions", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E3A74))


        Spacer(modifier = Modifier.height(300.dp))

        Text("Quick Access", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E3A74))

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                QuickAction("Browse\nSkills", R.drawable.browse_skills, onBrowseClick, Modifier.weight(1f))
                QuickAction("Bookings", R.drawable.booking, onBookingsClick, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                QuickAction("Feedback", R.drawable.feedback, onFeedbackClick, Modifier.weight(1f))
                QuickAction("History", R.drawable.history, onHistoryClick, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SessionCard(
    title: String,
    subtitle: String,
    timeLabel: String,
    time: String,
    location: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5FA)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.Bold, color = Color(0xFF2E3A74))
                    Text(subtitle, fontSize = 12.sp, color = Color(0xFF5C6DA7))
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(timeLabel, fontSize = 12.sp, color = Color(0xFF5C6DA7), fontWeight = FontWeight.Medium)
                    Text(time, fontSize = 12.sp, color = Color(0xFF2E3A74))
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.location), contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text(location, fontSize = 12.sp, color = Color(0xFF5C6DA7))
            }
        }
    }
}

@Composable
private fun QuickAction(
    label: String,
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(110.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(2.dp, Color(0xFF2E3A74), RoundedCornerShape(12.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(id = iconRes), contentDescription = label, modifier = Modifier.size(32.dp))
            Spacer(Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF2E3A74))
        }
    }
}


