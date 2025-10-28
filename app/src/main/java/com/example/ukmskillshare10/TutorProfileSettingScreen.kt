package com.example.ukmskillshare10

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TutorProfileSettingScreen(
    onBackClick: () -> Unit,
    onSaved: () -> Unit,
    context: android.content.Context
) {
    var fullName by remember { mutableStateOf("Michael Johnson") }
    var phone by remember { mutableStateOf("+60 12-345-6789") }
    var newSkill by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf(listOf("Mathematics", "Physics", "Algebra")) }
    var price by remember { mutableStateOf("50") }
    var allowNegotiable by remember { mutableStateOf(true) }

    // availability simple toggles (dummy)
    data class DayAvail(var enabled: Boolean, var slot: String)
    var availability by remember {
        mutableStateOf(
            listOf(
                DayAvail(true, "9:00 AM - 5:00 PM"),
                DayAvail(true, "9:00 AM - 5:00 PM"),
                DayAvail(false, "Not Available"),
                DayAvail(true, "1:00 PM - 6:00 PM"),
                DayAvail(true, "9:00 AM - 3:00 PM"),
                DayAvail(false, "Not Available"),
                DayAvail(false, "Not Available")
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) { Text("✕", fontSize = 24.sp, color = Color.Black) }
            Text(
                text = "Tutor Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF445BA5),
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // Header avatar
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.personal),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(Modifier.height(8.dp))

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(fullName, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF445BA5))
            Text("Mathematics & Physics Tutor", fontSize = 14.sp, color = Color.Gray)
        }

        Spacer(Modifier.height(16.dp))

        // Personal Info
        SectionCard(title = "Personal Info", iconRes = R.drawable.manage_profile) {
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = "michael.johnson@ukm.edu",
                onValueChange = {},
                label = { Text("Email (readonly)") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Skills
        SectionCard(title = "Skills", iconRes = R.drawable.skills) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                skills.forEach { s -> Chip(text = s) }
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newSkill,
                    onValueChange = { newSkill = it },
                    label = { Text("Add new skill") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    if (newSkill.isNotBlank()) {
                        skills = skills + newSkill.trim()
                        newSkill = ""
                    }
                }) { Text("Add") }
            }
        }

        // Availability
        SectionCard(title = "Availability", iconRes = R.drawable.sessions) {
            val days = listOf("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")
            availability.forEachIndexed { index, day ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(days[index], fontWeight = FontWeight.Medium)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(day.slot, color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(end = 12.dp))
                        Switch(checked = day.enabled, onCheckedChange = {
                            availability = availability.toMutableList().also { it[index] = it[index].copy(enabled = !it[index].enabled) }
                        })
                    }
                }
            }
        }

        // Rate Settings
        SectionCard(title = "Rate Settings", iconRes = R.drawable.booking) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("RM", modifier = Modifier.padding(end = 8.dp))
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it.filter { ch -> ch.isDigit() } },
                    modifier = Modifier.width(120.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = allowNegotiable, onCheckedChange = { allowNegotiable = it })
                Text("Allow negotiable rates")
            }
        }

        // Security & Support
        SectionCard(title = "Security & Support", iconRes = R.drawable.security) {
            TextButton(onClick = { }) { Text("Change Password") }
            TextButton(onClick = { }) { Text("Contact Help") }
            TextButton(onClick = { }, colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)) { Text("Delete Account") }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { onSaved() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(12.dp)
        ) { Text("Save Changes", color = Color.White) }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun SectionCard(title: String, iconRes: Int, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(title, fontWeight = FontWeight.Bold, color = Color(0xFF445BA5))
        }
        Spacer(Modifier.height(8.dp))
        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))) {
            Column(modifier = Modifier.padding(16.dp), content = content)
        }
    }
}

@Composable
private fun Chip(text: String) {
    Box(
        modifier = Modifier
            .border(1.dp, Color(0xFF2E3A74), RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(text, fontSize = 12.sp, color = Color(0xFF2E3A74))
    }
}


