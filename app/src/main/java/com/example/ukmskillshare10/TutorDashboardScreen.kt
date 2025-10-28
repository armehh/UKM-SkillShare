package com.example.ukmskillshare10

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
fun TutorDashboardScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Header
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("I'm signed in as", fontSize = 12.sp, color = Color.Gray)
                Text("Tutor", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF445BA5))
            }
            Image(painter = painterResource(id = R.drawable.personal), contentDescription = null, modifier = Modifier.size(40.dp))
        }

        Spacer(Modifier.height(12.dp))

        // Stats row
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatBox("128", "Total Points", modifier = Modifier.weight(1f))
            StatBox("24", "Sessions", modifier = Modifier.weight(1f))
            StatBox("4.8", "Avg. Rating", modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(12.dp))

        // Action list
        ActionTile(R.drawable.skills, "My Skills")
        ActionTile(R.drawable.sessions, "Schedule")
        ActionTile(R.drawable.session_request, "Session Requests")
        ActionTile(R.drawable.feedback, "Feedback")
        ActionTile(R.drawable.manage_profile, "Profile")

        Spacer(Modifier.height(12.dp))
        Text("Recent Activity", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E3A74))

        Spacer(Modifier.height(6.dp))
        RecentItem(title = "JavaScript Basics", subtitle = "Session completed with 4 students\nYesterday, 3:30 PM", rating = "4.9")
        Spacer(Modifier.height(8.dp))
        RecentItem(title = "Python for Data Science", subtitle = "Session completed with 6 students\nMonday, 5:00 PM", rating = "4.7")
    }
}

@Composable
private fun StatBox(value: String, label: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5FA)), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E3A74))
            Text(label, fontSize = 12.sp, color = Color(0xFF5C6DA7))
        }
    }
}

@Composable
private fun ActionTile(icon: Int, label: String) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)), shape = RoundedCornerShape(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = icon), contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(10.dp))
            Text(label, fontSize = 14.sp, color = Color(0xFF2E3A74), modifier = Modifier.weight(1f))
            Text("›", fontSize = 18.sp, color = Color(0xFF2E3A74))
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun RecentItem(title: String, subtitle: String, rating: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)), shape = RoundedCornerShape(10.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(title, fontWeight = FontWeight.Medium, color = Color(0xFF2E3A74))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("★ ", color = Color(0xFF2E3A74))
                    Text(rating, color = Color(0xFF2E3A74))
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(subtitle, fontSize = 12.sp, color = Color(0xFF5C6DA7))
        }
    }
}


